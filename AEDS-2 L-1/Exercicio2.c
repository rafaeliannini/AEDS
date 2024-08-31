#include <stdio.h>
#include <string.h>
#include <stdbool.h>

bool palindromo (char* palavra) {
	int tamanho = strlen(palavra);
	int esq = 0;
	int dir = tamanho -1;
	while(esq <= dir) {
		if(palavra[esq] != palavra[dir]) {
				return false;
		}
		esq ++;
		dir --;
	}
	return true;
}

int main(void) {
	char texto[500];
	char teste[] = "FIM";
	while(scanf(" %[^\n]", texto) == 1) {
		if(strcmp(texto, teste) != 0) {
			if(palindromo(texto)) {
				printf("SIM\n");
			}
			else {
				printf("NAO\n");
			}
		}
	}
}





