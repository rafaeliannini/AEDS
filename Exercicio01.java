import java.util.*;

class Pokemon{
    private int id;
    private int generation;
    private String name;
    private String description;
    private ArrayList<String> types = new ArrayList();
    private ArrayList<String> abilities = new ArrayList();
    private double weight;
    private double height;
    private int captureRate;
    private boolean isLegegendary;
    private Date captureDate = new Date();

    public Pokemon(){
        this.id = -1;
        this.generation = -1;
        this.name = "unknown";
        this.description = "unknown";
        this.weight = -1;
        this.height = -1;
        this.captureRate = -1;
        this.isLegegendary = -1;
    }

    public Pokemon(String linha){
        this.ler(linha);
    }

    public void setId(int id){
        this.id = id;
    }

    public void setGeneration(int generation){
        this.generation = generation;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setDescription(String description){
        this.description = description;
    }        

    public void setTypes(String type){
        types.add(type);
    }

    public void setAbilities(String abilitie){
        abilities.add(abilitie);
    }

    public void setWeight(double weight){
        this.weight = weight;
    }

    public void setHeight(double height){
        this.height = height;
    }

    public void setCaptureRate(int captureRate){
        this.captureRate = captureRate;
    }

    public void setIsLegegendary(boolean isLegegendary){
        this.isLegegendary = isLegegendary;
    }

    public void setCaptureDate(long captureDate){
        captureDate.setTime(captureDate);
    }

    public int getId(){
        return this.id;
    }

    public int getGeneration(){
        return this.generation;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public double getWeight(){
        return this.weight;
    }

    public double getHeight(){
        return this.height;
    }

    public int getCaptureRate(){
        return this.captureRate;
    }

    public boolean getIsLegendary(){
        return this.isLegegendary;
    }

    public long getCaptureDate(){
        return captureDate.getTime();
    }

    public void clone(){

    }

    public void ler(String linha){

    }

    public String imprimir(){

    }
}

public class Ex01{
    public static void main(String args[]){
        string id = MyIO.readline();
        Pokemon[] pokedex = new Pokemon[100];
        int pos = 0;
        while(!(equal(id, "FIM"))){
            //buscar arquivos linha id
            Pokemon[pos] = new Pokemon();
            Pokemon[pos].ler(linha);
            pos++;
            id = MyIO.readline();
        }
    }
}