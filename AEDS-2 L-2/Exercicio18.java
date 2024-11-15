import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Pokemon {
    private String id;
    private int generation;
    private String name;
    private String description;
    private String[] types;
    private String[] abilities;
    private double weight;
    private double height;
    private int captureRate;
    private boolean isLegendary;
    private LocalDate captureDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public int getGeneration() { return generation; }
    public void setGeneration(int generation) { this.generation = generation; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String[] getTypes() { return types; }
    public void setTypes(String[] types) { this.types = types; }
    
    public String[] getAbilities() { return abilities; }
    public void setAbilities(String[] abilities) { this.abilities = abilities; }
    
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    
    public int getCaptureRate() { return captureRate; }
    public void setCaptureRate(int captureRate) { this.captureRate = captureRate; }
    
    public boolean isLegendary() { return isLegendary; }
    public void setIsLegendary(boolean isLegendary) { this.isLegendary = isLegendary; }
    
    public LocalDate getCaptureDate() { return captureDate; }
    public void setCaptureDate(LocalDate captureDate) { this.captureDate = captureDate; }

    public void ler(String csvLine) {
        String[] data = csvLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        
        setId(data[0].trim());
        setGeneration(Integer.parseInt(data[1].trim()));
        setName(data[2].trim());
        setDescription(data[3].trim());

        List<String> typeList = new ArrayList<>();
        typeList.add(data[4].trim());
        if (data.length > 5 && !data[5].trim().isEmpty()) { 
            typeList.add(data[5].trim());
        }
        setTypes(typeList.toArray(new String[0]));

        String abilityString = data[6].replaceAll("[\\[\\]\"']", "").trim();
        setAbilities(abilityString.split(",\\s*"));

        setWeight(data.length > 7 && !data[7].trim().isEmpty() ? Double.parseDouble(data[7].trim()) : 0);
        setHeight(data.length > 8 && !data[8].trim().isEmpty() ? Double.parseDouble(data[8].trim()) : 0);
        
        setCaptureRate(data.length > 9 && !data[9].trim().isEmpty() ? Integer.parseInt(data[9].trim()) : 0);
        
        setIsLegendary(data.length > 10 && (data[10].trim().equals("1") || data[10].trim().equalsIgnoreCase("true")));
        
        if (data.length > 11 && !data[11].trim().isEmpty()) {
            LocalDate date = parseDate(data[11].trim());
            setCaptureDate(date);
        }
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        return LocalDate.parse(dateStr, dateFormatter);
    }

    public void imprimir() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.printf("[#%s -> %s: %s - [", getId(), getName(), getDescription());

        for (int i = 0; i < getTypes().length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.printf("'%s'", getTypes()[i]);
        }
        System.out.print("] - [");

        for (int i = 0; i < getAbilities().length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.printf("'%s'", getAbilities()[i]);
        }
        System.out.print("] - ");

        System.out.printf("%.1fkg - %.1fm - %d%% - %s - %d gen] - %s\n", 
            getWeight(), getHeight(), getCaptureRate(), 
            isLegendary() ? "true" : "false", getGeneration(),
            getCaptureDate().format(outputFormatter));
    }
}

public class Exercicio18 {
    private static int comparacoes = 0;
    private static int movimentacoes = 0;

    public static void main(String[] args) {
        List<Pokemon> pokedexList = new ArrayList<>();
        Scanner inputScanner = new Scanner(System.in);
        Instant start = Instant.now();

        while (true) {
            String idInput = inputScanner.nextLine().trim();
            if (idInput.equalsIgnoreCase("FIM")) {
                break;
            }
            Pokemon pokemon = buscarPokemonPorID(idInput);
            if (pokemon != null) {
                pokedexList.add(pokemon);
            } else {
                System.out.println("Pokémon com ID " + idInput + " não encontrado.");
            }
        }

        quicksortParcial(pokedexList, 0, pokedexList.size() - 1, 10);

        for (int i = 0; i < Math.min(10, pokedexList.size()); i++) {
            pokedexList.get(i).imprimir();
        }

        Instant end = Instant.now();
        long tempoExecucao = Duration.between(start, end).toMillis();

        String matricula = "1448840";
        String nomeArquivo = matricula + "_selecao.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write(matricula + "\t" + tempoExecucao + "ms\t" + comparacoes + "\t" + movimentacoes);
        } catch (IOException e) {
            System.err.println("Erro ao gravar o arquivo de log: " + e.getMessage());
        }

        inputScanner.close();
    }

    private static Pokemon buscarPokemonPorID(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("/tmp/pokemon.csv"))) {
            String csvLine;
            br.readLine();
            while ((csvLine = br.readLine()) != null) {
                String[] data = csvLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                String csvId = data[0].trim();
                if (csvId.equalsIgnoreCase(id)) {
                    Pokemon pokemon = new Pokemon();
                    pokemon.ler(csvLine);
                    return pokemon;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao abrir o arquivo: " + e.getMessage());
        }
        return null;
    }

    private static void quicksortParcial(List<Pokemon> pokedexList, int esq, int dir, int k) {
        if (esq < dir && esq < k) {
            int pivotIndex = partition(pokedexList, esq, dir);

            quicksortParcial(pokedexList, esq, pivotIndex - 1, k);
            quicksortParcial(pokedexList, pivotIndex + 1, dir, k);
        }
    }

    private static int partition(List<Pokemon> pokedexList, int low, int high) {
        Pokemon pivot = pokedexList.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            comparacoes++;
            int compareGen = Integer.compare(pokedexList.get(j).getGeneration(), pivot.getGeneration());

            if (compareGen < 0 || (compareGen == 0 && pokedexList.get(j).getName().compareToIgnoreCase(pivot.getName()) < 0)) {
                i++;
                movimentacoes += 3;
                Pokemon temp = pokedexList.get(i);
                pokedexList.set(i, pokedexList.get(j));
                pokedexList.set(j, temp);
            }
        }

        movimentacoes += 3;
        Pokemon temp = pokedexList.get(i + 1);
        pokedexList.set(i + 1, pokedexList.get(high));
        pokedexList.set(high, temp);

        return i + 1;
    }
}