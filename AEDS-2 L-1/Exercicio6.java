class Exercicio6 {
	public static boolean vogal (String x){
		for (int i=0; i < x.length(); i++) {
			char c = x.charAt(i);
			if(c != 'A' && c != 'a' && c != 'E' && c != 'e' && c != 'I' && c != 'i' && c != 'O' && c != 'o' && c != 'U' && c != 'u') {
				return false;
			}
		}
		return true;
	}

	public static boolean consoante (String x) {
		for (int i=0; i < x.length(); i++) {
			char c = x.charAt(i);
			if(c == 'A' || c == 'a' || c == 'E' || c == 'e' || c == 'I' || c == 'i' || c == 'O' || c == 'o' || c == 'U' || c == 'u' || c == '1' || c == '2'|| c == '3' || c== '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == '0') {
				return false;
			}
		}
		return true;
	}

	public static boolean inteiro (String x) {
		try {
			Integer.parseInt(x);
			return true;
		}
		catch (NumberFormatException e){
			return false;
		}

	}
	
	public static boolean real (String x) {
		String novo = "";
		for (int i=0; i < x.length(); i++) {
			char c = x.charAt(i);
			if(c == ','){
				c = '.';
			}
			novo += c;
		}
		try {
			Double.parseDouble(novo);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean testeFim(String x) {
		if (x.length() != 3) {
			return false;
		}
		if (x.charAt(0) == 'F' && x.charAt(1) == 'I' && x.charAt(2) == 'M') {
			return true;
		}
		else {
			return false;
		}
	}
	public static void main (String args[]) {
		String texto = MyIO.readLine();
		while (!testeFim(texto)) {
			if(vogal(texto)){
				MyIO.print("SIM ");
			}
			else{
				MyIO.print("NAO ");
			}
			if(consoante(texto)){
				MyIO.print("SIM ");
			}
			else{
				MyIO.print("NAO ");
			}
			if(inteiro(texto)){
				MyIO.print("SIM ");
			}
			else{
				MyIO.print("NAO ");
			}
			if(real(texto)){
				MyIO.println("SIM");
			}
			else {
				MyIO.println("NAO");
			}
			texto = MyIO.readLine();
		}
	}
}
