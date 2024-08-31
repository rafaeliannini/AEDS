class Exercicio12 {
	public static String cifra (String x, int i) {
		if(x.length() == i){
            return "";
        }
        char c = x.charAt(i);
        char novoC = (char) (c + 3);

        return novoC + cifra(x, i+1);
    }

    public static String teste(String x){
        return cifra(x, 0);
    }
	
	public static boolean testeFim (String x) {
		if(x.length() != 3) {
			return false;
		}
		if(x.charAt(0) == 'F' && x.charAt(1) == 'I' && x.charAt(2) == 'M') {							      	
		       	return true;
		}
		else {
			return false;
		}
	}

	public static void main(String args[]) {
		String texto = MyIO.readLine();
		String cifra = "";
		while(!testeFim(texto)) {
			cifra = teste(texto);
			MyIO.println(cifra);
			texto = MyIO.readLine();
		}
	}
}
