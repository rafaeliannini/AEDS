class Exercicio5 {

    public static boolean calcular(String str) {
        String resp = "";
        int inicio = 0, fim = 0;
        boolean resposta;

        while (str.length() > 1) {

            // Encontrando o inicio e o fim de cada expressão
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '(') {
                    inicio = i;
                } else if (str.charAt(i) == ')') {
                    fim = i;
                    i = str.length();
                }
            }

            if (str.charAt(inicio - 1) == 't') { // Resolvendo a expressão NOT
                resp = new String();

                for (int i = 0; i < str.length(); i++) {
                    if (i == (inicio - 3)) {
                        if (str.charAt(inicio + 1) == '0') {
                            resp += '1';
                        } else {
                            resp += '0';
                        }
                    } else if (i > (inicio - 3) && i <= fim) {
                        resp += "";
                    } else {
                        resp += str.charAt(i);
                    }
                }

            } else if (str.charAt(inicio - 1) == 'd') { // Resolvendo a expressão AND
                resp = new String();

                for (int i = 0; i < str.length(); i++) {

                    if (i == (inicio - 3)) {

                        if (str.charAt(inicio + 1) == '1'
                                && str.charAt(inicio + 3) == '1'
                                && str.charAt(fim - 1) == '1') {
                            resp += '1';
                        } else {
                            resp += '0';
                        }
                    } else if (i > (inicio - 3) && i <= fim) {
                        resp += "";
                    } else {
                        resp += str.charAt(i);
                    }

                }
            } else if (str.charAt(inicio - 1) == 'r') { // Resolvendo a expressão OR
                resp = new String();

                for (int i = 0; i < str.length(); i++) {

                    if (i == (inicio - 2)) {
                        if (str.charAt(inicio + 1) == '1'
                                || str.charAt(inicio + 3) == '1'
                                || str.charAt(fim - 1) == '1' || str.charAt(fim - 3) == '1') {
                            resp += '1';
                        } else {
                            resp += '0';
                        }
                    } else if (i > (inicio - 2) && i <= fim) {
                        resp += "";
                    } else {
                        resp += str.charAt(i);
                    }
                }
            }

            str = resp.replaceAll(" ", "");
        }

        if (str.charAt(0) == '1') {
            resposta = true;
        } else {
            resposta = false;
        }

        return resposta;
    }

    public static void main(String[] args) {
        String str = MyIO.readLine().replaceAll(" ", "");

        while (str.charAt(0) != '0') {
            String bool = " ";

            char A = str.charAt(1);
            char B = str.charAt(2);
            char C = (str.charAt(0) == '3') ? str.charAt(3) : '*';

            // Substituindo cada variável pelo seu valor na expressão booleana
            if (str.charAt(0) == '3') {
                for (int i = 4; i < str.length(); i++) {
                    if (str.charAt(i) == 'A') {
                        bool += A;
                    } else if (str.charAt(i) == 'B') {
                        bool += B;
                    } else if (str.charAt(i) == 'C') {
                        bool += C;
                    } else {
                        bool += str.charAt(i);
                    }
                }
            } else {
                for (int i = 3; i < str.length(); i++) {
                    if (str.charAt(i) == 'A') {
                        bool += A;
                    } else if (str.charAt(i) == 'B') {
                        bool += B;
                    } else {
                        bool += str.charAt(i);
                    }
                }
            }

            if (calcular(bool)) {
                System.out.println("1");
            } else {
                System.out.println("0");
            }

            str = MyIO.readLine().replaceAll(" ", "");
        }

    }
}