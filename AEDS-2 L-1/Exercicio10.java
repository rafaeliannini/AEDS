class Exercicio10 {
        public static boolean palindromo (String x, int esq, int dir) {
                if(esq > dir){
                    return true;
                }
                if(x.charAt(esq) != x.charAt(dir)){
                    return false;
                }
                return palindromo(x, esq+1, dir-1);
        }

        public static boolean teste (String x){
            return palindromo(x, 0, x.length()-1);
        }

        public static boolean testeFim (String x) {
            if(x.length() != 3) {
                return false;
            }
            if(x.charAt(0) == 'F' && x.charAt(1) == 'I' && x.charAt(2) == 'M'){
                return true;
            }
            else {
                return false;
            }
        }
    
    
        public static void main(String args[]) {
            String texto = MyIO.readLine();
            while(!testeFim(texto)) {
                if(teste(texto)){
                    MyIO.println("SIM");
                }
                else {
                    MyIO.println("NAO");
                }
                texto = MyIO.readLine();
            }
           }
    }       

