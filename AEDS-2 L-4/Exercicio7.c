#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>
#include <time.h>

#define TAM_TAB 21

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

typedef struct Node {
    Pokemon elemento;
    struct Node* next;
} Node;

Node* hashTable[TAM_TAB];
int COMPARACOES = 0;

void trim(char *str) {
    char *end;
    while (isspace((unsigned char)*str)) str++;
    if (*str == 0) return;
    end = str + strlen(str) - 1;
    while (end > str && isspace((unsigned char)*end)) end--;
    end[1] = '\0';
}

int calculateHash(const char *name) {
    int sum = 0;
    for (int i = 0; name[i] != '\0'; i++) {
        sum += (unsigned char)name[i];
    }
    return sum % TAM_TAB;
}

Node* createNode(Pokemon pokemon) {
    Node* newNode = (Node*) malloc(sizeof(Node));
    newNode->elemento = pokemon;
    newNode->next = NULL;
    return newNode;
}

void insertPokemon(Pokemon pokemon) {
    int hash = calculateHash(pokemon.name);
    Node* newNode = createNode(pokemon);
    newNode->next = hashTable[hash];
    hashTable[hash] = newNode;
}

bool searchPokemon(const char *name) {
    int hash = calculateHash(name);
    Node* current = hashTable[hash];
    
    while (current) {
        COMPARACOES++;
        if (strcasecmp(name, current->elemento.name) == 0) {
            printf("(Posicao: %d) SIM\n", hash);
            return true;
        }
        current = current->next;
    }

    printf("NAO\n");
    return false;
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

int main() {
    memset(hashTable, 0, sizeof(hashTable));

    FILE *file = fopen("/tmp/pokemon.csv", "r");
    if (file == NULL) {
        perror("Erro ao abrir o arquivo");
        return 1;
    }

    char line[1024];
    fgets(line, sizeof(line), file); // Ignorar o cabeçalho

    char ids[100][256];
    int idCount = 0;

    while (fgets(line, sizeof(line), stdin) != NULL) {
        line[strcspn(line, "\n")] = '\0';
        if (strcasecmp(line, "FIM") == 0) {
            break;
        }
        trim(line);
        strncpy(ids[idCount++], line, 256);
    }

    while (fgets(line, sizeof(line), file) != NULL) {
        line[strcspn(line, "\n")] = '\0';
        Pokemon p;
        memset(&p, 0, sizeof(Pokemon));

        char *fields[12];
        if (split_csv_line(line, fields, 12) >= 3) {
            trim(fields[0]);
            trim(fields[2]);

            strncpy(p.id, fields[0], 255);
            strncpy(p.name, fields[2], 255);

            for (int i = 0; i < idCount; i++) {
                if (strcasecmp(p.id, ids[i]) == 0) {
                    insertPokemon(p);
                    break;
                }
            }
        }
    }
    fclose(file);

    char name[256];
    clock_t start = clock();
    while (1) {
        if (fgets(name, sizeof(name), stdin) == NULL) {
            break;
        }

        name[strcspn(name, "\n")] = '\0';
        if (strcasecmp(name, "FIM") == 0) {
            break;
        }
        trim(name);
        printf("=> %s: ", name);
        searchPokemon(name);
    }

    clock_t end = clock();
    double elapsed_time = ((double)(end - start)) / CLOCKS_PER_SEC * 1000;

    char *matricula = "1448840";
    char filename[50];
    snprintf(filename, sizeof(filename), "%s_hashIndireta.txt", matricula);

    FILE *logFile = fopen(filename, "w");
    if (logFile) {
        fprintf(logFile, "%s\t%.2fms\t%d\t", matricula, elapsed_time, COMPARACOES);
        fclose(logFile);
    } else {
        perror("Erro ao gravar o arquivo de log");
    }

    return 0;
}