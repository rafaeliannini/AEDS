import java.util.Scanner;

class Celula {
    public int elemento;
    public Celula inf, sup, esq, dir;

    public Celula() {
        this(-1);
    }

    public Celula(int elemento) {
        this.elemento = elemento;
        this.inf = this.sup = this.esq = this.dir = null;
    }
}

class Matriz {
    private Celula inicio;
    private int linha, coluna;

    public Matriz(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;

        Celula ultimaLinha = null;
        for (int i = 0; i < linha; i++) {
            Celula atualLinha = null, anterior = null;

            for (int j = 0; j < coluna; j++) {
                Celula nova = new Celula();
                if (atualLinha == null) {
                    atualLinha = nova;
                }

                if (anterior != null) {
                    anterior.dir = nova;
                    nova.esq = anterior;
                }

                if (ultimaLinha != null) {
                    Celula superior = ultimaLinha;
                    for (int k = 0; k < j; k++) {
                        superior = superior.dir;
                    }
                    nova.sup = superior;
                    superior.inf = nova;
                }

                anterior = nova;
                if (inicio == null) {
                    inicio = nova;
                }
            }

            ultimaLinha = atualLinha;
        }
    }

    public void preencher(int[][] elementos) {
        Celula atual = inicio;
        for (int i = 0; i < linha; i++) {
            Celula temp = atual;
            for (int j = 0; j < coluna; j++) {
                temp.elemento = elementos[i][j];
                temp = temp.dir;
            }
            atual = atual.inf;
        }
    }

    public Matriz soma(Matriz m) {
        if (this.linha != m.linha || this.coluna != m.coluna) {
            throw new IllegalArgumentException("Matrizes de tamanhos diferentes não podem ser somadas.");
        }

        Matriz resp = new Matriz(linha, coluna);
        Celula a = this.inicio, b = m.inicio, c = resp.inicio;

        while (a != null) {
            Celula tempA = a, tempB = b, tempC = c;
            while (tempA != null) {
                tempC.elemento = tempA.elemento + tempB.elemento;
                tempA = tempA.dir;
                tempB = tempB.dir;
                tempC = tempC.dir;
            }
            a = a.inf;
            b = b.inf;
            c = c.inf;
        }
        return resp;
    }

    public Matriz multiplicacao(Matriz m) {
        if (this.coluna != m.linha) {
            throw new IllegalArgumentException("Matrizes incompatíveis para multiplicação.");
        }

        Matriz resp = new Matriz(this.linha, m.coluna);
        Celula a = this.inicio, c = resp.inicio;

        for (int i = 0; i < this.linha; i++) {
            Celula bCol = m.inicio;
            for (int j = 0; j < m.coluna; j++) {
                int soma = 0;
                Celula tempA = a, tempB = bCol;
                for (int k = 0; k < this.coluna; k++) {
                    soma += tempA.elemento * tempB.elemento;
                    tempA = tempA.dir;
                    tempB = tempB.inf;
                }
                c.elemento = soma;
                c = c.dir;
                bCol = bCol.dir;
            }
            a = a.inf;
            c = resp.inicio;
            for (int x = 0; x <= i; x++) c = c.inf;
        }
        return resp;
    }

    public void mostrarDiagonalPrincipal() {
        if (!isQuadrada()) return;
        Celula atual = inicio;
        while (atual != null) {
            System.out.print(atual.elemento + " ");
            atual = (atual.dir != null) ? atual.dir.inf : null;
        }
        System.out.println();
    }

    public void mostrarDiagonalSecundaria() {
        if (!isQuadrada()) return;
        Celula atual = inicio;
        while (atual.dir != null) {
            atual = atual.dir;
        }
        while (atual != null) {
            System.out.print(atual.elemento + " ");
            atual = (atual.esq != null) ? atual.esq.inf : null;
        }
        System.out.println();
    }

    public boolean isQuadrada() {
        return linha == coluna;
    }

    public void imprimir() {
        Celula atual = inicio;
        while (atual != null) {
            Celula temp = atual;
            while (temp != null) {
                System.out.print(temp.elemento + " ");
                temp = temp.dir;
            }
            System.out.println();
            atual = atual.inf;
        }
    }
}

public class TP3Exercicio11 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int casos = Integer.parseInt(input.nextLine());

        for (int i = 0; i < casos; i++) {
            int l1 = Integer.parseInt(input.nextLine());
            int c1 = Integer.parseInt(input.nextLine());
            int[][] matriz1 = new int[l1][c1];

            for (int j = 0; j < l1; j++) {
                String[] linha = input.nextLine().split(" ");
                for (int k = 0; k < c1; k++) {
                    matriz1[j][k] = Integer.parseInt(linha[k]);
                }
            }

            int l2 = Integer.parseInt(input.nextLine());
            int c2 = Integer.parseInt(input.nextLine());
            int[][] matriz2 = new int[l2][c2];

            for (int j = 0; j < l2; j++) {
                String[] linha = input.nextLine().split(" ");
                for (int k = 0; k < c2; k++) {
                    matriz2[j][k] = Integer.parseInt(linha[k]);
                }
            }

            Matriz m1 = new Matriz(l1, c1);
            m1.preencher(matriz1);
            Matriz m2 = new Matriz(l2, c2);
            m2.preencher(matriz2);

            m1.mostrarDiagonalPrincipal();
            m1.mostrarDiagonalSecundaria();

            Matriz soma = m1.soma(m2);
            soma.imprimir();

            Matriz multiplicacao = m1.multiplicacao(m2);
            multiplicacao.imprimir();
        }

        input.close();
    }
}
