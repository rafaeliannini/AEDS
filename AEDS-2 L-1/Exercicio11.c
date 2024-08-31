#include <stdio.h>
#include <string.h>
#include <stdbool.h>

bool palindromo (char* palavra, int esq, int dir) {
    if(esq > dir){
        return true;
    }
    if(palavra[esq] != palavra[dir]) {
		return false;
	}
    return palindromo(palavra, esq+1, dir-1);
}

bool testeP(char* palavra){
	int tamanho = (strlen(palavra) - 1);
    return palindromo(palavra, 0, tamanho);
}

int main(void) {
	char texto[500];
	char teste[] = "FIM";
	while(scanf(" %[^\n]", texto) == 1) {
		if(strcmp(texto, teste) != 0) {
			if(testeP(texto)) {
				printf("SIM\n");
			}
			else {
				printf("NAO\n");
			}
		}
	}
}





