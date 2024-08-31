class Exercicio1 {
	public static boolean palindromo (String x) {
               int esq = 0;
	       int dir = x.length() -1;
	       while (esq < dir) {
		       if(x.charAt(esq) != x.charAt(dir)) {
			       return false;
		       }
		       esq++;
		       dir--;
	       }
	       return true;
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
		       if(palindromo(texto)){
			       MyIO.println("SIM");
		       }
		       else {
			       MyIO.println("NAO");
		       }
		       texto = MyIO.readLine();
	       }
       }
}       
