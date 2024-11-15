#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>
#include <time.h>

typedef struct {
    char id[256];
    int generation;
    int idNumber;
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

typedef struct Celula {
    Pokemon elemento;
    struct Celula* prox;
} Celula;

typedef struct Lista {
    Celula* primeiro;
    Celula* ultimo;
} Lista;

void initLista(Lista *lista) {
    lista->primeiro = (Celula*) malloc(sizeof(Celula));
    lista->ultimo = lista->primeiro;
    lista->primeiro->prox = NULL;
}

void inserirInicio(Lista *lista, Pokemon pokemon) {
    Celula *tmp = (Celula*) malloc(sizeof(Celula));
    tmp->elemento = pokemon;
    tmp->prox = lista->primeiro->prox;
    lista->primeiro->prox = tmp;
    if (lista->primeiro == lista->ultimo) {
        lista->ultimo = tmp;
    }
}

void inserirFim(Lista *lista, Pokemon pokemon) {
    lista->ultimo->prox = (Celula*) malloc(sizeof(Celula));
    lista->ultimo = lista->ultimo->prox;
    lista->ultimo->elemento = pokemon;
    lista->ultimo->prox = NULL;
}

void inserir(Lista *lista, Pokemon pokemon, int pos) {
    int index = 0;
    Celula *i = lista->primeiro;

    while (i->prox != NULL && index < pos) {
        i = i->prox;
        index++;
    }

    Celula *tmp = (Celula*) malloc(sizeof(Celula));
    tmp->elemento = pokemon;
    tmp->prox = i->prox;
    i->prox = tmp;

    if (i == lista->ultimo) {
        lista->ultimo = tmp;
    }
}

void removerInicio(Lista *lista) {
    if (lista->primeiro == lista->ultimo) {
        printf("Erro: Lista vazia!\n");
        return;
    }

    Celula *tmp = lista->primeiro->prox;
    printf("(R) %s\n", tmp->elemento.name);
    lista->primeiro->prox = tmp->prox;

    if (tmp == lista->ultimo) {
        lista->ultimo = lista->primeiro;
    }

    free(tmp);
}

void removerFim(Lista *lista) {
    if (lista->primeiro == lista->ultimo) {
        printf("Erro: Lista vazia!\n");
        return;
    }

    Celula *i;
    for (i = lista->primeiro; i->prox != lista->ultimo; i = i->prox);
    
    printf("(R) %s\n", lista->ultimo->elemento.name); 
    free(lista->ultimo);

    lista->ultimo = i;
    lista->ultimo->prox = NULL;
}

void remover(Lista *lista, int pos) {
    int index = 0;
    Celula *i = lista->primeiro;

    while (i->prox != NULL && index < pos) {
        i = i->prox;
        index++;
    }

    if (i->prox == NULL) {
        printf("Erro: Posição inválida!\n");
        return;
    }

    Celula *tmp = i->prox;
    printf("(R) %s\n", tmp->elemento.name); 
    i->prox = tmp->prox;

    if (tmp == lista->ultimo) {
        lista->ultimo = i;
    }

    free(tmp);
}

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

void loadPokemon(FILE *file, Lista *pokedex) {
    char line[1024];
    fgets(line, sizeof(line), file);

    while (fgets(line, sizeof(line), file) != NULL) {
        line[strcspn(line, "\n")] = '\0'; 
        Pokemon p;
        memset(&p, 0, sizeof(Pokemon));

        char *fields[12];
        int field_count = split_csv_line(line, fields, 12);

        if (field_count < 12) {
            printf("Erro: linha CSV incompleta\n");
            continue;
        }

        strncpy(p.id, fields[0], 255);
        p.generation = atoi(fields[1]);
        strncpy(p.name, fields[2], 255);
        strncpy(p.description, fields[3], 255);
        
        strncpy(p.types[0], fields[4], 255);
        if (strlen(fields[5]) > 0) {
            strncpy(p.types[1], fields[5], 255);
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
        int abilityIndex = 0;
        while ((abilityToken = strtok(abilityIndex == 0 ? abilities_field : NULL, ",")) && abilityIndex < 6) {
            trim(abilityToken);
            strncpy(p.abilities[abilityIndex], abilityToken, 255);
            abilityIndex++;
        }

        p.weight = atof(fields[7]);
        p.height = atof(fields[8]);
        p.captureRate = atoi(fields[9]);
        p.isLegendary = atoi(fields[10]);

        struct tm captureDate = {0};
        parseDate(fields[11], &captureDate);
        p.captureDate = captureDate;

        inserirFim(pokedex, p);
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
        printf("%s", pokemon->abilities[j]);
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

int getCharAt(char *str, int pos) {
    if (pos < strlen(str)) {
        return (int)str[pos];
    }
    return 0; 
}

void mostrarLista(Lista *lista) {
    int index = 0;
    for (Celula *i = lista->primeiro->prox; i != NULL; i = i->prox) {
        printf("[%d] ", index++);
        printPokemon(&i->elemento);
    }
}

int main() {
    FILE *file = fopen("/tmp/pokemon.csv", "r");
    if (file == NULL) {
        perror("Erro ao abrir o arquivo");
        return 1;
    }

    Lista lista;
    initLista(&lista);
    loadPokemon(file, &lista);
    fclose(file);

    Lista foundList;
    initLista(&foundList);

    char id[256];

    while (1) {
        if (fgets(id, sizeof(id), stdin) == NULL) {
            break;
        }
        trim(id);

        if (strcasecmp(id, "FIM") == 0) {
            break;
        }

        bool found = false;
        for (Celula *i = lista.primeiro->prox; i != NULL; i = i->prox) {
            if (strcmp(i->elemento.id, id) == 0) {
                inserirFim(&foundList, i->elemento);
                found = true;
                break;
            }
        }

        if (!found) {
            printf("Pokémon com ID %s não foi encontrado.\n", id);
        }
    }

    int num;
    scanf("%d\n", &num);
    for (int i = 0; i < num; i++) {
        char command[256];
        fgets(command, sizeof(command), stdin);
        command[strcspn(command, "\n")] = 0;

        if (command[0] == 'I') {
            char id[256];
            int pos;
            bool found = false;
            Celula *pokemon = NULL;

            if (command[1] == 'I') { 
                sscanf(command, "II %s", id);
            } else if (command[1] == 'F') { 
                sscanf(command, "IF %s", id);
            } else if (command[1] == '*') { 
                sscanf(command, "I* %d %s", &pos, id);
            }

            for (Celula *i = lista.primeiro->prox; i != NULL; i = i->prox) {
                if (strcmp(i->elemento.id, id) == 0) {
                    pokemon = i;
                    found = true;
                    break;
                }
            }

            if (found) {
                if (command[1] == 'I') {
                    inserirInicio(&foundList, pokemon->elemento);
                } else if (command[1] == 'F') {
                    inserirFim(&foundList, pokemon->elemento);
                } else if (command[1] == '*') {
                    inserir(&foundList, pokemon->elemento, pos);
                }
            } else {
                printf("Pokémon com ID %s não foi encontrado.\n", id);
            }

        } else if (command[0] == 'R') {
            if (command[1] == 'I') { 
                removerInicio(&foundList);
            } else if (command[1] == 'F') { 
                removerFim(&foundList);
            } else if (command[1] == '*') { 
                int pos;
                sscanf(command, "R* %d", &pos);
                remover(&foundList, pos);
            }
        }
    }

    mostrarLista(&foundList);

    return 0;
}
