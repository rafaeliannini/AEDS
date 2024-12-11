import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class TabelaHashDiretaComReserva {
    private Pokemon[] tabela;
    private int tamTab;
    private int tamReserva;
    private int tamTotal;
    public int comparacoes;

    public TabelaHashDiretaComReserva() {
        tamTab = 21;
        tamReserva = 9;
        tamTotal = tamTab + tamReserva;
        tabela = new Pokemon[tamTotal];
    }

    private int funcaoHash(String nome) {
        int somaASCII = 0;
        for (char c : nome.toCharArray()) {
            somaASCII += c;
        }
        return somaASCII % tamTab;
    }

    public void inserir(Pokemon pokemon) {
        int pos = funcaoHash(pokemon.getName());

        if (tabela[pos] == null) {
            tabela[pos] = pokemon;
        } else {
            boolean inserido = false;
            for (int i = tamTab; i < tamTotal; i++) {
                if (tabela[i] == null) {
                    tabela[i] = pokemon;
                    inserido = true;
                    break;
                }
            }

            if (!inserido) {
            }
        }
    }

    public boolean pesquisar(String nome) {
        int pos = funcaoHash(nome);
        comparacoes++;
        if (tabela[pos] != null && tabela[pos].getName().equalsIgnoreCase(nome)) {
            System.out.println("(Posicao: "+pos+") SIM");
            return true;
        }

        for (int i = tamTab; i < tamTotal; i++) {
            comparacoes++;
            if (tabela[i] != null && tabela[i].getName().equalsIgnoreCase(nome)) {
                System.out.println("(Posicao: "+i+") SIM");
                return true;
            }
        }

        System.out.println("NAO");
        return false;
    }

    public void mostrar() {
        for (int i = 0; i < tamTotal; i++) {
            System.out.print(i + ": ");
            if (tabela[i] != null) {
                System.out.println(tabela[i].getName());
            } else {
                System.out.println("null");
            }
        }
    }
}

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
        if (!data[5].trim().isEmpty()) {
            typeList.add(data[5].trim());
        }
        setTypes(typeList.toArray(new String[0]));

        String abilityString = data[6].replaceAll("[\\[\\]\"']", "").trim();
        setAbilities(abilityString.split(",\\s*"));

        setWeight(data[7].trim().isEmpty() ? 0 : Double.parseDouble(data[7].trim()));
        setHeight(data[8].trim().isEmpty() ? 0 : Double.parseDouble(data[8].trim()));
        
        setCaptureRate(data[9].trim().isEmpty() ? 0 : Integer.parseInt(data[9].trim()));
        
        setIsLegendary(data[10].trim().equals("1") || data[10].trim().equalsIgnoreCase("true"));
        
        LocalDate date = parseDate(data[11].trim());
        setCaptureDate(date);
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        return LocalDate.parse(dateStr, dateFormatter);
    }

    public void imprimir(int index) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.printf("[%d] [#%s -> %s: %s - [", index, getId(), getName(), getDescription());

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

public class Exercicio5 {
    public static void main(String[] args) {
        TabelaHashDiretaComReserva pokedex = new TabelaHashDiretaComReserva();
        Scanner inputScanner = new Scanner(System.in);

        while (true) {
            String idInput = inputScanner.nextLine().trim();
            if (idInput.equalsIgnoreCase("FIM")) {
                break;
            }

            Pokemon pokemon = buscarPokemonPorID(idInput);
            if (pokemon != null) {
                try {
                    pokedex.inserir(pokemon);
                } catch (Exception e) {
                    System.err.println("Erro ao inserir Pokémon: " + e.getMessage());
                }
            } else {
                System.out.println("Pokémon com ID " + idInput + " não encontrado.");
            }
        }

        Instant start = Instant.now();
        int comparisons = 0;
        while (true) {
            String idInput = inputScanner.nextLine().trim();
            if (idInput.equalsIgnoreCase("FIM")) {
                break;
            }
            System.out.print("=> " + idInput + ": ");
            pokedex.pesquisar(idInput);
            comparisons += pokedex.comparacoes;
        }

        Instant end = Instant.now();
        long tempoExecucao = Duration.between(start, end).toMillis();

        String matricula = "1448840";
        String nomeArquivo = matricula + "_hashReserva.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write(matricula + "\t" + tempoExecucao + "ms" + "Comparações" + comparisons);
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
}