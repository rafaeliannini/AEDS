#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>
#include <time.h>

typedef struct {
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

const char* getId(Pokemon *poke) { return poke->id; }
const char* getName(Pokemon *poke) { return poke->name; }
const char* getDescription(Pokemon *poke) { return poke->description; }
const char* getType(Pokemon *poke, int index) { return (index >= 0 && index < 3) ? poke->types[index] : ""; }
const char* getAbility(Pokemon *poke, int index) { return (index >= 0 && index < 6) ? poke->abilities[index] : ""; }
double getWeight(Pokemon *poke) { return poke->weight; }
double getHeight(Pokemon *poke) { return poke->height; }
bool getIsLegendary(Pokemon *poke) { return poke->isLegendary; }
int getGeneration(Pokemon *poke) { return poke->generation; }
int getCaptureRate(Pokemon *poke) { return poke->captureRate; }
struct tm getCaptureDate(Pokemon *poke) { return poke->captureDate; }

void trim(char *str) {
    char *end;
    while (isspace((unsigned char)*str)) str++;
    if (*str == 0) return;
    end = str + strlen(str) - 1;
    while (end > str && isspace((unsigned char)*end)) end--;
    end[1] = '\0';
}

void parseDate(char *dateStr, struct tm *date) {
    if (sscanf(dateStr, "%d/%d/%d", &date->tm_mday, &date->tm_mon, &date->tm_year) != 3) {
        return;
    }
    date->tm_mon -= 1;
    date->tm_year -= 1900;
}

int split_csv_line(char *line, char **fields, int max_fields) {
    int field_count = 0;
    char *ptr = line;
    int in_quotes = 0;
    char *field_start = ptr;

    while (*ptr && field_count < max_fields) {
        if (*ptr == '"') {
            in_quotes = !in_quotes;
        } else if (*ptr == ',' && !in_quotes) {
            *ptr = '\0';
            fields[field_count++] = field_start;
            field_start = ptr + 1;
        }
        ptr++;
    }
    if (field_count < max_fields) {
        fields[field_count++] = field_start;
    }
    return field_count;
}

void loadPokemon(FILE *file, Pokemon *pokedex, int *n) {
    char line[1024];
    fgets(line, sizeof(line), file);

    while (fgets(line, sizeof(line), file) != NULL) {
        line[strcspn(line, "\n")] = '\0';
        Pokemon p;
        memset(&p, 0, sizeof(Pokemon));

        char *fields[12];
        split_csv_line(line, fields, 12);

        strncpy(p.id, fields[0], 256);
        p.generation = atoi(fields[1]);
        strncpy(p.name, fields[2], 256);
        strncpy(p.description, fields[3], 256);
        strncpy(p.types[0], fields[4], 256);
        if (strlen(fields[5]) > 0) {
            strncpy(p.types[1], fields[5], 256);
        }

        char *abilities_field = fields[6];
        if (abilities_field[0] == '"' && abilities_field[strlen(abilities_field) - 1] == '"') {
            abilities_field[strlen(abilities_field) - 1] = '\0';
            abilities_field++;
        }
        if (abilities_field[0] == '[' && abilities_field[strlen(abilities_field) - 1] == ']') {
            abilities_field[strlen(abilities_field) - 1] = '\0';
            abilities_field++;
        }

        char *abilityToken;
        char *restAbilities = abilities_field;
        int abilityIndex = 0;
        while ((abilityToken = strtok_r(restAbilities, ",", &restAbilities)) && abilityIndex < 6) {
            while (*abilityToken == ' ' || *abilityToken == '\'') abilityToken++;
            char *tempEnd = abilityToken + strlen(abilityToken) - 1;
            while (tempEnd > abilityToken && (*tempEnd == ' ' || *tempEnd == '\'')) {
                *tempEnd = '\0';
                tempEnd--;
            }
            if (strlen(abilityToken) > 0 && abilityIndex < 6) {
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

void printPokemon(Pokemon *pokemon) {
    char dateStr[11];
    strftime(dateStr, sizeof(dateStr), "%d/%m/%Y", &pokemon->captureDate);

    printf(
        "[#%s -> %s: %s - [", 
        pokemon->id, 
        pokemon->name, 
        pokemon->description
    );

    for (int j = 0; j < 3 && strlen(pokemon->types[j]) > 0; j++) {
        if (j > 0) printf(", ");
        printf("'%s'", pokemon->types[j]);
    }
    printf("] - [");

    for (int j = 0; j < 6 && strlen(pokemon->abilities[j]) > 0; j++) {
        if (j > 0) printf(", ");
        printf("'%s'", pokemon->abilities[j]);
    }
    printf("] - ");

    printf("%.1fkg - %.1fm - %d%% - %s - %d gen] - %s\n", 
        pokemon->weight, 
        pokemon->height, 
        pokemon->captureRate, 
        pokemon->isLegendary ? "true" : "false", 
        pokemon->generation, 
        dateStr
    );
}

void swap(Pokemon **a, Pokemon **b) {
    Pokemon *temp = *a;
    *a = *b;
    *b = temp;
}

void construir(Pokemon *array[], int tamHeap) {
    for (int i = tamHeap; i > 1 && array[i]->height > array[i / 2]->height; i /= 2) {
        swap(&array[i], &array[i / 2]);
    }
}

int getMaiorFilho(Pokemon *array[], int i, int tamHeap) {
    int filho;
    if (2 * i == tamHeap || array[2 * i]->height > array[2 * i + 1]->height) {
        filho = 2 * i;
    } else {
        filho = 2 * i + 1;
    }
    return filho;
}

void reconstruir(Pokemon *array[], int tamHeap) {
    int i = 1;
    while (i <= (tamHeap / 2)) {
        int filho = getMaiorFilho(array, i, tamHeap);
        if (array[i]->height < array[filho]->height) {
            swap(&array[i], &array[filho]);
            i = filho;
        } else {
            i = tamHeap;
        }
    }
}

void heapsortParcial(Pokemon *array[], int n, int k, int *comparisons, int *movements) {
    if (k > n) {
        k = n;
    }

    for (int tamHeap = 2; tamHeap <= k; tamHeap++) {
        (*comparisons)++;
        construir(array, tamHeap);
    }

    for (int i = k + 1; i <= n; i++) {
        (*comparisons)++;
        if (array[i]->height < array[1]->height) {
            (*movements)++;
            array[1] = array[i];
            reconstruir(array, k);
        }
    }

    for (int tamHeap = k; tamHeap > 1; tamHeap--) {
        swap(&array[1], &array[tamHeap]);
        (*movements)++;
        reconstruir(array, tamHeap - 1);
    }
}


    int main() {
        FILE *file = fopen("/tmp/pokemon.csv", "r");
        if (file == NULL) {
            perror("Erro ao abrir o arquivo");
            return 1;
        }
        int n = 0;
        Pokemon pokedex[1000];
        loadPokemon(file, pokedex, &n);
        fclose(file);

        clock_t start = clock();

        char id[256];
        Pokemon *foundList[100];
        int foundCount = 0;

        while (1) {
            if (fgets(id, sizeof(id), stdin) == NULL) {
                break;
            }
            trim(id);

            if (strcasecmp(id, "FIM") == 0) {
                break;
            }

            bool found = false;
            for (int i = 0; i < n; i++) {
                if (strcmp(pokedex[i].id, id) == 0) {
                    foundList[foundCount++] = &pokedex[i];
                    found = true;
                    break;
                }
            }

            if (!found) {
                printf("Pokémon com ID %s não foi encontrado.\n", id);
            }
        }
    int comparisons = 0;
    int movements = 0;
    Pokemon *arrayTmp[n + 1];
    for (int i = 0; i < n; i++) {
        arrayTmp[i + 1] = &pokedex[i];
    }

    int k = 10;
    heapsortParcial(arrayTmp, n, k, &comparisons, &movements);

    for (int i = 1; i <= k && i <= n; i++) {
        printPokemon(arrayTmp[i]);
    }

        clock_t end = clock();
        double elapsed_time = ((double)(end - start)) / CLOCKS_PER_SEC * 1000;

        char *matricula = "1448840";
        char filename[50];
        snprintf(filename, sizeof(filename), "%s_heapSortP.txt", matricula);

        FILE *logFile = fopen(filename, "w");
        if (logFile) {
            fprintf(logFile, "%s\t%.2fms\t%d\t%d\n", matricula, elapsed_time, comparisons, movements);
            fclose(logFile);
        } else {
            perror("Erro ao gravar o arquivo de log");
        }

        return 0;
    }

