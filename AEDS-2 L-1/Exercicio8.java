import java.io.IOException;
import java.io.RandomAccessFile;

public class Exercicio8 {
    public static void main(String[] args){
      try{  
      int n = MyIO.readInt();
        RandomAccessFile raf = new RandomAccessFile("texto.txt", "rw");

        for (int i =0; i < n; i++){
            double real = MyIO.readDouble();
            raf.writeDouble(real);
        }
        raf.close();
        
         raf = new RandomAccessFile("texto.txt", "r");
         for(int i = n-1; i >= 0; i--){
            raf.seek(i * 8);
            double leitura = raf.readDouble();
            if(leitura % 1 == 0) System.out.println((int)leitura);
            else System.out.println(leitura);
        }
         raf.close();
      }
      catch(IOException e) {
         MyIO.println("Erro");
      }
}

}
