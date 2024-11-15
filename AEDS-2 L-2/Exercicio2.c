#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>
#include <time.h>

typedef struct 
{
    char id[256];
    int generation;
    char name[256];
    char description[256];
    char types[3][256];
    char abilities[6][256];
    double weight;
    double height;
    int captureRate;
    bool isLegendary;
    struct tm captureDate;
} Pokemon;

const char* getId(Pokemon *poke);
const char* getName(Pokemon *poke);
const char* getDescription(Pokemon *poke);
const char* getType(Pokemon *poke, int index);
const char* getAbility(Pokemon *poke, int index);
double getWeight(Pokemon *poke);
double getHeight(Pokemon *poke);
bool getIsLegendary(Pokemon *poke);
int getGeneration(Pokemon *poke);
int getCaptureRate(Pokemon *poke);
struct tm getCaptureDate(Pokemon *poke);

void trim(char *str) 
{
    char *end;
    while (isspace((unsigned char)*str)) str++;
    if (*str == 0) return;
    end = str + strlen(str) - 1;
    while (end > str && isspace((unsigned char)*end)) end--;
    end[1] = '\0';
}

void parseDate(char *dateStr, struct tm *date) 
{
    if (sscanf(dateStr, "%d/%d/%d", &date->tm_mday, &date->tm_mon, &date->tm_year) != 3) 
    {
        return;
    }
    date->tm_mon -= 1;
    date->tm_year -= 1900;
}

int split_csv_line(char *line, char **fields, int max_fields) 
{
    int field_count = 0;
    char *ptr = line;
    int in_quotes = 0;
    char *field_start = ptr;

    while (*ptr && field_count < max_fields) 
    {
        if (*ptr == '"') 
        {
            in_quotes = !in_quotes;
        } 
        else if (*ptr == ',' && !in_quotes) 
        {
            *ptr = '\0';
            fields[field_count++] = field_start;
            field_start = ptr + 1;
        }
        ptr++;
    }
    if (field_count < max_fields) 
    {
        fields[field_count++] = field_start;
    }

    return field_count;
}

char *my_strdup(const char *str) 
{
    char *dup = malloc(strlen(str) + 1);
    if (dup) 
    {
        strcpy(dup, str);
    }
    return dup;
}

const char* getId(Pokemon *poke) 
{
    return poke->id;
}

const char* getName(Pokemon *poke) 
{
    return poke->name;
}

const char* getDescription(Pokemon *poke) 
{
    return poke->description;
}

const char* getType(Pokemon *poke, int index) 
{
    if (index >= 0 && index < 3) 
    {
        return poke->types[index];
    }
    return "";
}

const char* getAbility(Pokemon *poke, int index) 
{
    if (index >= 0 && index < 6) 
    {
        return poke->abilities[index];
    }
    return "";
}

double getWeight(Pokemon *poke) 
{
    return poke->weight;
}

double getHeight(Pokemon *poke) 
{
    return poke->height;
}

bool getIsLegendary(Pokemon *poke) 
{
    return poke->isLegendary;
}

int getGeneration(Pokemon *poke) 
{
    return poke->generation;
}

int getCaptureRate(Pokemon *poke) 
{
    return poke->captureRate;
}

struct tm getCaptureDate(Pokemon *poke) 
{
    return poke->captureDate;
}

void loadPokemon(FILE *file, Pokemon *pokedex, int *n) 
{
    char line[1024];

    fgets(line, sizeof(line), file);

    while (fgets(line, sizeof(line), file) != NULL) 
    {
        line[strcspn(line, "\n")] = '\0';

        Pokemon p;
        memset(&p, 0, sizeof(Pokemon));

        char *fields[12];
        int field_count = split_csv_line(line, fields, 12);

        strncpy(p.id, fields[0], 256);
        p.generation = atoi(fields[1]);
        strncpy(p.name, fields[2], 256);
        strncpy(p.description, fields[3], 256);
        strncpy(p.types[0], fields[4], 256);
        if (strlen(fields[5]) > 0) 
        {
            strncpy(p.types[1], fields[5], 256);
        }

        char *abilities_field = fields[6];
        if (abilities_field[0] == '"' && abilities_field[strlen(abilities_field) - 1] == '"') 
        {
            abilities_field[strlen(abilities_field) - 1] = '\0';
            abilities_field++;
        }
        if (abilities_field[0] == '[' && abilities_field[strlen(abilities_field) - 1] == ']') 
        {
            abilities_field[strlen(abilities_field) - 1] = '\0';
            abilities_field++;
        }

        char *abilityToken;
        char *restAbilities = abilities_field;
        int abilityIndex = 0;
        while ((abilityToken = strtok_r(restAbilities, ",", &restAbilities)) && abilityIndex < 6) 
        {
            while (*abilityToken == ' ' || *abilityToken == '\'') abilityToken++;
            char *tempEnd = abilityToken + strlen(abilityToken) - 1;
            while (tempEnd > abilityToken && (*tempEnd == ' ' || *tempEnd == '\'')) 
            {
                *tempEnd = '\0';
                tempEnd--;
            }
            if (strlen(abilityToken) > 0 && abilityIndex < 6) 
            {
                strncpy(p.abilities[abilityIndex], abilityToken, 256);
                abilityIndex++;
            }
        }

        p.weight = atof(fields[7]);
        p.height = atof(fields[8]);
        p.captureRate = atoi(fields[9]);
        p.isLegendary = atoi(fields[10]);

        struct tm captureDate = {0};
        parseDate(fields[11], &captureDate);
        p.captureDate = captureDate;

        pokedex[*n] = p;
        (*n)++;
    }
}

void printPokemon(Pokemon *pokemon) 
{
    char dateStr[11];
    strftime(dateStr, sizeof(dateStr), "%d/%m/%Y", &pokemon->captureDate);

    printf(
        "[#%s -> %s: %s - [", 
        getId(pokemon), 
        getName(pokemon), 
        getDescription(pokemon)
    );

    for (int j = 0; j < 3 && strlen(getType(pokemon, j)) > 0; j++) 
    {
        if (j > 0) printf(", ");
        printf("'%s'", getType(pokemon, j));
    }
    printf("] - [");

    for (int j = 0; j < 6 && strlen(getAbility(pokemon, j)) > 0; j++) 
    {
        if (j > 0) printf(", ");
        printf("'%s'", getAbility(pokemon, j));
    }
    printf("] - ");

    printf("%.1fkg - %.1fm - %d%% - %s - %d gen] - %s\n", 
        getWeight(pokemon), 
        getHeight(pokemon), 
        getCaptureRate(pokemon), 
        getIsLegendary(pokemon) ? "true" : "false", 
        getGeneration(pokemon), 
        dateStr
    );
}

int main() 
{
    FILE *file = fopen("/tmp/pokemon.csv", "r");
    if (file == NULL) 
    {
        perror("Erro ao abrir o arquivo");
        return 1;
    }

    Pokemon pokedex[1000];
    int count = 0;

    loadPokemon(file, pokedex, &count);

    fclose(file);

    char id[256];
    while (1) 
    {
        if (fgets(id, sizeof(id), stdin) == NULL) 
        {
            break;
        }
        trim(id);

        if (strcasecmp(id, "FIM") == 0) 
        {
            break;
        }

        bool found = false;
        for (int i = 0; i < count; i++) 
        {
            if (strcmp(pokedex[i].id, id) == 0) 
            {
                printPokemon(&pokedex[i]);
                found = true;
                break;
            }
        }

        if (!found) 
        {
            printf("Pokémon com ID %s não foi encontrado.\n", id);
        }
    }

    return 0;
}
