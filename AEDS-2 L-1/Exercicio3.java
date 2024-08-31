class Exercicio3 {
	public static String cifra (String x) {
		int tamanho = x.length();
		String resp= "";
		for(int i=0; i<tamanho; i++){
			char c = x.charAt(i);
			char novoC = (char) (c+3);
			resp += novoC;
		}
		return resp;
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
			cifra = cifra(texto);
			MyIO.println(cifra);
			texto = MyIO.readLine();
		}
	}
}
