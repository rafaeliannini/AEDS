import java.util.Random;

class Exercicio4 {
	public static String cifraAleatoria(String x, char letra1, char letra2) {
		int tamanho = x.length();
		String resp = "";		
		for(int i=0; i < tamanho; i++) {
			char c = x.charAt(i);
			if(c == letra1) {
				resp += letra2;
			}
			else {
				resp += c;
			}
		}
		return resp;
	}
	public static boolean testeFim (String x){
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
		Random gerador = new Random();
		gerador.setSeed(4);
		String texto = MyIO.readLine();
		String cifra = "";
		while(!testeFim(texto)){
			char letra1 = (char)('a' + (Math.abs(gerador.nextInt()) % 26));
			char letra2 = (char)('a' + (Math.abs(gerador.nextInt()) % 26));	
			cifra = cifraAleatoria(texto, letra1, letra2);
			MyIO.println(cifra);
			texto = MyIO.readLine();
		}
	}	
}