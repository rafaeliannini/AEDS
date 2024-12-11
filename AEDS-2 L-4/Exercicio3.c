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

typedef struct Node {
    Pokemon elemento;
    struct Node* left;
    struct Node* right;
    int height;
} Node;

int COMPARACOES = 0;

void trim(char *str) {
    char *end;
    while (isspace((unsigned char)*str)) str++;
    if (*str == 0) return;
    end = str + strlen(str) - 1;
    while (end > str && isspace((unsigned char)*end)) end--;
    end[1] = '\0';
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

Node* createNode(Pokemon pokemon) {
    Node* newNode = (Node*) malloc(sizeof(Node));
    newNode->elemento = pokemon;
    newNode->left = NULL;
    newNode->right = NULL;
    newNode->height = 1;
    return newNode;
}

int getHeight(Node* node) {
    return node ? node->height : 0;
}

int getBalanceFactor(Node* node) {
    return node ? getHeight(node->left) - getHeight(node->right) : 0;
}

Node* rotateRight(Node* y) {
    Node* x = y->left;
    Node* T2 = x->right;

    x->right = y;
    y->left = T2;

    y->height = 1 + (getHeight(y->left) > getHeight(y->right) ? getHeight(y->left) : getHeight(y->right));
    x->height = 1 + (getHeight(x->left) > getHeight(x->right) ? getHeight(x->left) : getHeight(x->right));

    return x;
}

Node* rotateLeft(Node* x) {
    Node* y = x->right;
    Node* T2 = y->left;

    y->left = x;
    x->right = T2;

    x->height = 1 + (getHeight(x->left) > getHeight(x->right) ? getHeight(x->left) : getHeight(x->right));
    y->height = 1 + (getHeight(y->left) > getHeight(y->right) ? getHeight(y->left) : getHeight(y->right));

    return y;
}

Node* insert(Node* node, Pokemon pokemon) {
    if (!node) return createNode(pokemon);

    if (strcmp(pokemon.id, node->elemento.id) < 0) {
        node->left = insert(node->left, pokemon);
    } else if (strcmp(pokemon.id, node->elemento.id) > 0) {
        node->right = insert(node->right, pokemon);
    } else {
        return node;
    }

    node->height = 1 + (getHeight(node->left) > getHeight(node->right) 
                        ? getHeight(node->left) 
                        : getHeight(node->right));

    int balance = getBalanceFactor(node);

    if (balance > 1 && strcmp(pokemon.id, node->left->elemento.id) < 0) {
        return rotateRight(node);
    }

    if (balance < -1 && strcmp(pokemon.id, node->right->elemento.id) > 0) {
        return rotateLeft(node);
    }

    if (balance > 1 && strcmp(pokemon.id, node->left->elemento.id) > 0) {
        node->left = rotateLeft(node->left);
        return rotateRight(node);
    }

    if (balance < -1 && strcmp(pokemon.id, node->right->elemento.id) < 0) {
        node->right = rotateRight(node->right);
        return rotateLeft(node);
    }

    return node;
}

bool search(Node *node, const char *name) {
    COMPARACOES++;
    if (node == NULL) {
        printf("NAO\n");
        return false;
    }
    COMPARACOES++;

    if (strcasecmp(name, node->elemento.name) == 0) {
        printf("SIM\n");
        return true;
    }
    COMPARACOES++;

    if (strcasecmp(name, node->elemento.name) < 0) {
        printf("esq ");
        return search(node->left, name);
    } else {
        printf("dir ");
        return search(node->right, name);
    }
}

int main() {
    Node *root = NULL;
    FILE *file = fopen("/tmp/pokemon.csv", "r");
    if (file == NULL) {
        perror("Erro ao abrir o arquivo");
        return 1;
    }

    char line[1024];
    fgets(line, sizeof(line), file);

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
                    root = insert(root, p);
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
        printf("%s\n", name);
        printf("raiz ");
        search(root, name);
    }

    int comparisons = COMPARACOES;
    clock_t end = clock();
    double elapsed_time = ((double)(end - start)) / CLOCKS_PER_SEC * 1000;

    char *matricula = "1448840";
    char filename[50];
    snprintf(filename, sizeof(filename), "%s_avl.txt", matricula);

    FILE *logFile = fopen(filename, "w");
    if (logFile) {
        fprintf(logFile, "%s\t%.2fms\t%d\t", matricula, elapsed_time, comparisons);
        fclose(logFile);
    } else {
        perror("Erro ao gravar o arquivo de log");
    }

    return 0;
}
