#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>
#include <time.h>

#define MAX_TYPES 3
#define MAX_ABILITIES 6

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
typedef struct No{
    Pokemon elemento;
    struct No* dir;
    struct No* esq;
}No;

No* construtorNo(Pokemon elemento){
    No* i = (No*) malloc(sizeof(No));
    i->elemento = elemento;
    i->dir = i->esq = NULL;

    return i;
}

typedef struct Arvore{
    No* raiz;
}Arvore;

Arvore* arvoreConstrutor(){
    Arvore* i = (Arvore*) malloc(sizeof(Arvore));
    i->raiz = NULL;

    return i;
}

int compare(Pokemon a, Pokemon b){
    return strcmp(a.name, b.name);
} 

int comparar(char nome[], Pokemon b){
    return strcmp(nome, b.name);
} 

No* rotacaoDir(No* i){
        No* tmp = i->esq;
        i->esq = tmp->dir;
        tmp->dir = i;

        return tmp;
    }

No* rotacaoEsq(No* i){
        No* tmp = i->dir;
        i->dir = tmp->esq;
        tmp->esq = i;

        return tmp;
    }

int getAltura(No* i){
    if(i == NULL){
        return -1;
    }
    int alturaEsq = getAltura(i->esq) + 1;
    int alturaDir = getAltura(i->dir) + 1;

    int altura = (alturaEsq > alturaDir ? alturaEsq : alturaDir);
    return altura; 
}

int getFator(No* i){

        int alturaEsq = getAltura(i->esq) + 1;
        int alturaDir = getAltura(i->dir) + 1;

        return (alturaDir - alturaEsq);

}

No* inserir(No* i, Pokemon elemento){
    if(i == NULL){
        i = construtorNo(elemento);
    }else if(compare(elemento, i->elemento) > 0){
        i->dir = inserir(i->dir, elemento);
    }else if(compare(elemento, i->elemento) < 0){
        i->esq = inserir(i->esq, elemento);
    }else{
        printf("ERRO, VALOR INVALIDO");
    }

    if(getFator(i) == -2){
            if(getFator(i->esq) == 1){
                i->esq = rotacaoEsq(i->esq); 
            }
            i = rotacaoDir(i);

        }else if(getFator(i) == 2){
            if(getFator(i->dir) == -1){
                i->dir = rotacaoDir(i->dir);
            }
            i = rotacaoEsq(i);
        }



    return i;


}
void pesquisa(No* i, char nome[]){
    if(i == NULL){
        printf(" NAO\n");
    }else if(comparar(nome, i->elemento) > 0){
        printf(" dir");
        pesquisa(i->dir, nome);
    }else if(comparar(nome, i->elemento) < 0){
        printf(" esq");
        pesquisa(i->esq, nome);
    }else{
        printf( " SIM\n");
    }
}

void parseDate(char *dateStr, struct tm *date) {
    if (sscanf(dateStr, "%d/%d/%d", &date->tm_mday, &date->tm_mon, &date->tm_year) == 3) {
        date->tm_mon -= 1;
        date->tm_year -= 1900;
    }
}
int main() {
    No *root = NULL;
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
    int fieldCount = split_csv_line(line, fields, 12);

    if (fieldCount < 12) {
        printf("Erro: linha CSV incompleta\n");
        continue;
    }

    strncpy(p.id, fields[0], sizeof(p.id) - 1);
    p.generation = atoi(fields[1]);
    strncpy(p.name, fields[2], sizeof(p.name) - 1);
    strncpy(p.description, fields[3], sizeof(p.description) - 1);

    strncpy(p.types[0], fields[4], sizeof(p.types[0]) - 1);
    if (strlen(fields[5]) > 0) {
        strncpy(p.types[1], fields[5], sizeof(p.types[1]) - 1);
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
    while ((abilityToken = strtok(abilityIndex == 0 ? abilities_field : NULL, ",")) && abilityIndex < MAX_ABILITIES) {
        trim(abilityToken);
        strncpy(p.abilities[abilityIndex], abilityToken, sizeof(p.abilities[abilityIndex]) - 1);
        abilityIndex++;
    }

    p.weight = atof(fields[7]);
    p.height = atof(fields[8]);
    p.captureRate = atoi(fields[9]);
    p.isLegendary = atoi(fields[10]);

    parseDate(fields[11], &p.captureDate);

    for (int i = 0; i < idCount; i++) {
        if (strcasecmp(p.id, ids[i]) == 0) {
            root = inserir(root, p);
            break;
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
        pesquisa(root, name);
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
