import java.io.*;
import java.net.*;

class Exercicio7 {
   public static String getHtml(String endereco){
      URL url;
      InputStream is = null;
      BufferedReader br;
      String resp = "", line;

      try {
         url = new URL(endereco);
         is = url.openStream();  // throws an IOException
         br = new BufferedReader(new InputStreamReader(is));

         while ((line = br.readLine()) != null) {
            resp += line + "\n";
         }
      } catch (MalformedURLException mue) {
         mue.printStackTrace();
      } catch (IOException ioe) {
         ioe.printStackTrace();
      } 

      try {
         is.close();
      } catch (IOException ioe) {
         // nothing to see here

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

   public static void main(String[] args) {
      String endereco, nome, html;
      nome = MyIO.readLine();
      endereco = MyIO.readLine();

      while(!testeFim(nome)){
         int a = 0, e = 0, i = 0, o = 0, u = 0;
         int aAgudo = 0, eAgudo = 0, iAgudo = 0, oAgudo = 0, uAgudo = 0;
         int aCrase = 0, eCrase = 0, iCrase = 0, oCrase = 0, uCrase = 0;
         int aTIl = 0, oTil = 0;
         int aCirc = 0, eCirc = 0, iCirc = 0, oCirc = 0, uCirc = 0;
         int consoante = 0, br = 0, table = 0;
         html = getHtml(endereco);
         for(int k = 0; k < html.length(); k++){
            char c = html.charAt(k);
            switch(c){
                case 'a': a++;
                break;

                case 'e': e++;
                break;

                case 'i': i++;
                break;

                case 'o': o++;
                break;

                case 'u': u++;
                break;

                case '\u00E1': aAgudo++;
                break;

                case '\u00E9': eAgudo++;
                break;

                case '\u00ED': iAgudo++;
                break;

                case '\u00F3': oAgudo++;
                break;

                case '\u00FA': uAgudo++;
                break;

                case '\u00E0': aCrase++;
                break;

                case '\u00E8': eCrase++;
                break;

                case '\u00EC': iCrase++;
                break;

                case '\u00F2': oCrase++;
                break;

                case '\u00F9': uCrase++;
                break;

                case '\u00E3': aTIl++;
                break;

                case '\u00F5': oTil++;
                break;

                case '\u00E2': aCirc++;
                break;

                case '\u00EA': eCirc++;
                break;

                case '\u00EE': iCirc++;
                break;

                case '\u00F4': oCirc++;
                break;

                case '\u00FB': uCirc++;
                break;

                case '<':
                    int j = k + 1; 
                    if(html.charAt(j) == 'b' && html.charAt(j + 1) == 'r' && html.charAt(j + 2) == '>'){
                        br++;
                    }
                    else if(html.charAt(j) == 't' && html.charAt(j + 1) == 'a' && html.charAt(j + 2) == 'b' && html.charAt(j + 3) =='l' && html.charAt(j + 4) == 'e' && html.charAt(j + 5) == '>'){
                        table++;
                    }
                
                break;
                
                case 'b':
                case 'c':
                case 'd':
                case 'f':
                case 'g':
                case 'h':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    consoante++;
                    break;
                default:
                    // Tratamento padrão para outros caracteres
                    break;
            }
            MyIO.print("a("+ a +") e("+ e +") i("+ i +") o("+ o +") u(" + u + ") ");
            MyIO.print("á(" + aAgudo + ") é(" + eAgudo + ") í(" + iAgudo + ") ó(" + oAgudo + ") ú(" + uAgudo + ") ");
            MyIO.print("à(" + aCrase + ") è(" + eCrase + ") ì(" + iCrase + ") ò(" + oCrase + ") ù(" + uCrase + ") ");
            MyIO.print("ã(" + aTIl + ") õ(" + oTil + ") ");
            MyIO.print("â(" + aCirc + ") ê(" + eCirc + ") î(" + iCirc + ") ô(" + oCirc + ") û(" + uCirc + ") ");
            MyIO.print("consoante(" + consoante + ") <br>(" + br + ") <table>(" + table + ") " + nome    );
            MyIO.println("");
         }
			nome = MyIO.readLine();
         endereco = MyIO.readLine();
		}
   }
}