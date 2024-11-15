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

class Celula {
    public Pokemon elemento;
    public Celula prox;
    public Celula ant;

    public Celula() {
        this(null);
    }

    public Celula(Pokemon elemento) {
        this.elemento = elemento;
        this.prox = null;
        this.ant = null;
    }
}

class Lista {
    private Celula primeiro;
    private Celula ultimo;

    public Lista() {
        primeiro = new Celula();
        ultimo = primeiro;
    }

    public void inserirInicio(Pokemon pokemon) {
        Celula tmp = new Celula(pokemon);
        tmp.prox = primeiro.prox;
        tmp.ant = primeiro;

        if (primeiro.prox != null) {
            primeiro.prox.ant = tmp;
        } else {
            ultimo = tmp;
        }

        primeiro.prox = tmp;
    }

    public void inserirFim(Pokemon pokemon) {
        Celula tmp = new Celula(pokemon);
        tmp.ant = ultimo;
        ultimo.prox = tmp;
        ultimo = tmp;
    }

    public String removerInicio() throws Exception {
        if (primeiro == ultimo) {
            throw new Exception("Erro ao remover (vazia)!");
        }

        Celula tmp = primeiro.prox;
        String resp = tmp.elemento.getName();
        primeiro.prox = tmp.prox;

        if (tmp.prox != null) {
            tmp.prox.ant = primeiro;
        } else {
            ultimo = primeiro;
        }

        tmp.prox = tmp.ant = null;
        return resp;
    }

    public String removerFim() throws Exception {
        if (primeiro == ultimo) {
            throw new Exception("Erro ao remover (vazia)!");
        }

        String resp = ultimo.elemento.getName();
        ultimo = ultimo.ant;
        ultimo.prox.ant = null;
        ultimo.prox = null;

        return resp;
    }

    public void inserir(Pokemon pokemon, int pos) throws Exception {
        int tamanho = tamanho();

        if (pos < 0 || pos > tamanho) {
            throw new Exception("Erro ao inserir (posição inválida)!");
        } else if (pos == 0) {
            inserirInicio(pokemon);
        } else if (pos == tamanho) {
            inserirFim(pokemon);
        } else {
            Celula i = primeiro.prox;
            for (int j = 0; j < pos - 1; j++, i = i.prox);

            Celula tmp = new Celula(pokemon);
            tmp.prox = i.prox;
            tmp.ant = i;

            if (i.prox != null) {
                i.prox.ant = tmp;
            }
            i.prox = tmp;
        }
        
    }

    public String remover(int pos) throws Exception {
        int tamanho = tamanho();

        if (primeiro == ultimo) {
            throw new Exception("Erro ao remover (vazia)!");
        } else if (pos < 0 || pos >= tamanho) {
            throw new Exception("Erro ao remover (posição inválida)!");
        } else if (pos == 0) {
            return removerInicio();
        } else if (pos == tamanho - 1) {
            return removerFim();
        } else {
            Celula i = primeiro.prox;
            for (int j = 0; j < pos; j++, i = i.prox);

            String resp = i.elemento.getName();
            i.ant.prox = i.prox;

            if (i.prox != null) {
                i.prox.ant = i.ant;
            }

            i.prox = i.ant = null;
            return resp;
        }
    }

    public void mostrar() {
        int index = 0;
        for (Celula i = primeiro.prox; i != null; i = i.prox) {
            i.elemento.imprimir(index);
            index++;
        }
    }

    public int tamanho() {
        int tamanho = 0;
        for (Celula i = primeiro.prox; i != null; i = i.prox, tamanho++);
        return tamanho;
    }

       
    private Celula partition(Celula esq, Celula dir, int[] comparisons, int[] movements) {
        Pokemon pivot = dir.elemento;
        Celula i = esq.ant;
    
        for (Celula j = esq; j != dir; j = j.prox) {
            comparisons[0]++;
            if (j.elemento.getGeneration() < pivot.getGeneration() || 
                (j.elemento.getGeneration() == pivot.getGeneration() && 
                 j.elemento.getName().compareToIgnoreCase(pivot.getName()) < 0)) {
                i = (i == null) ? esq : i.prox;
                swap(i, j);
                movements[0]++;
            }
        }
    
        i = (i == null) ? esq : i.prox;
        swap(i, dir);
        movements[0]++;
        return i;
    }
    
    private void quickSort(Celula esq, Celula dir, int[] comparisons, int[] movements) {
        if (esq != null && dir != null && esq != dir && esq != dir.prox) {
            Celula pi = partition(esq, dir, comparisons, movements);
    
            quickSort(esq, pi.ant, comparisons, movements);
            quickSort(pi.prox, dir, comparisons, movements);
        }
    }
    
    public void quickSort(int[] comparisons, int[] movements) {
        quickSort(primeiro.prox, ultimo, comparisons, movements);
    }
    
    private void swap(Celula a, Celula b) {
        Pokemon temp = a.elemento;
        a.elemento = b.elemento;
        b.elemento = temp;
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

public class TP3Exercicio10 {
    public static void main(String[] args) {
        Lista pokedexList = new Lista();
        Scanner inputScanner = new Scanner(System.in);

        while (true) {
            String idInput = inputScanner.nextLine().trim();
            if (idInput.equalsIgnoreCase("FIM")) {
                break;
            }

            Pokemon pokemon = buscarPokemonPorID(idInput);
            if (pokemon != null) {
                pokedexList.inserirFim(pokemon);
            } else {
                System.out.println("Pokémon com ID " + idInput + " não encontrado.");
            }
        }

        int[] comparisons = {0};
        int[] movements = {0};

        Instant start = Instant.now();

        pokedexList.quickSort(comparisons, movements);

        Instant end = Instant.now();
        long tempoExecucao = Duration.between(start, end).toMillis();

        String matricula = "1448840";
        String nomeArquivo = matricula + "_quicksort3.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write(matricula + "\t" + tempoExecucao + "ms\t" + comparisons + "\t" + movements);
        } catch (IOException e) {
            System.err.println("Erro ao gravar o arquivo de log: " + e.getMessage());
        }

        pokedexList.mostrar();

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
