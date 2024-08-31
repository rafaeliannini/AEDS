#include <stdio.h>
#include <stdlib.h>

int main (void){
    FILE* arquivo = fopen("texto.txt", "w");
    int n;
    scanf("%d", &n);
    double real;

    for(int i=0; i < n; i++){
        scanf("%lf", &real);
        fwrite(&real, 8, 1, arquivo);   
    }
    fclose(arquivo);

    arquivo = fopen("texto.txt", "r");
    fseek(arquivo, 0, SEEK_END);
    long posicao = ftell(arquivo);
    double leitura;

    while(posicao > 0){
        posicao -= sizeof(double); 
        fseek(arquivo, posicao, SEEK_SET);
        fread(&leitura, 8, 1, arquivo);
        printf("%g\n", leitura);
    }

    fclose(arquivo);
    return 0;
}