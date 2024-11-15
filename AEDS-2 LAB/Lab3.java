public class Lab3 {
    public static boolean testeParentese (String x){
        int ap=0, fp=0;
        for(int i=0; i < x.length(); i++){
            if(x.charAt(i) == '('){
                ap++;
            }
            else if(x.charAt(i) == ')'){
                fp++;
            }
        }
        if(fp == ap){
            return true;
        }
        else{
        return false;
        }
    }
    
    public static boolean testeFim(String x) {
		if (x.charAt(0) == 'F' && x.charAt(1) == 'I' && x.charAt(2) == 'M') {
			return true;
		}
		else {
			return false;
		}
	}
    public static void main(String[] args) {
        String texto = MyIO.readLine();
        while(!testeFim(texto)){
            if(testeParentese(texto)){
                MyIO.println("correto");
            }
            else{
                MyIO.println("incorreto");
            }
        }
    }
}
