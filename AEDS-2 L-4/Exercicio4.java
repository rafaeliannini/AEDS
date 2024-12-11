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

class No {
    public Pokemon elemento;
    public No esq, dir;
    public boolean cor; 

    public No(Pokemon elemento) {
        this(elemento, null, null, true);
    }

    public No(Pokemon elemento, No esq, No dir, boolean cor) {
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
        this.cor = cor;
    }
}

class ArvoreAlvinegra {
    private No raiz;
    public int comparacoes;

    public ArvoreAlvinegra() {
        raiz = null;
    }

    public void inserir(Pokemon pokemon) throws Exception {
        raiz = inserir(pokemon, null, null, raiz);
        if (raiz != null) {
            raiz.cor = false; 
        }
    }

    private No inserir(Pokemon pokemon, No bisavo, No avo, No pai) {
        if (pai == null) {
            return new No(pokemon);
        }

        if (pokemon.getName().compareToIgnoreCase(pai.elemento.getName()) < 0) {
            pai.esq = inserir(pokemon, avo, pai, pai.esq);
        } else if (pokemon.getName().compareToIgnoreCase(pai.elemento.getName()) > 0) {
            pai.dir = inserir(pokemon, avo, pai, pai.dir);
        } else {
            System.out.println("Pokémon já inserido: " + pokemon.getName());
        }

        pai = balancear(bisavo, avo, pai);

        return pai;
    }

    private No balancear(No bisavo, No avo, No pai) {
        if (isRed(pai.dir) && !isRed(pai.esq)) {
            pai = rotacaoEsquerda(pai);
        }
        if (isRed(pai.esq) && isRed(pai.esq.esq)) {
            pai = rotacaoDireita(pai);
        }
        if (isRed(pai.esq) && isRed(pai.dir)) {
            trocaCor(pai);
        }

        if (avo != null && isRed(pai) && isRed(avo)) {
            if ((avo.esq == pai && avo.dir != null && isRed(avo.dir)) || 
                (avo.dir == pai && avo.esq != null && isRed(avo.esq))) {
                trocaCor(avo);
                if (bisavo != null) {
                    bisavo = balancear(null, null, bisavo);
                }
            } else if (avo.esq == pai && isRed(pai.dir)) {
                avo.esq = rotacaoEsquerda(pai);
                avo = rotacaoDireita(avo);
            } else if (avo.dir == pai && isRed(pai.esq)) {
                avo.dir = rotacaoDireita(pai);
                avo = rotacaoEsquerda(avo);
            }
        }

        return pai;
    }

    private boolean isRed(No no) {
        return no != null && no.cor == true;
    }

    private No rotacaoEsquerda(No no) {
        No aux = no.dir;
        no.dir = aux.esq;
        aux.esq = no;
        aux.cor = no.cor;
        no.cor = true;
        return aux;
    }

    private No rotacaoDireita(No no) {
        No aux = no.esq;
        no.esq = aux.dir;
        aux.dir = no;
        aux.cor = no.cor;
        no.cor = true;
        return aux;
    }

    private void trocaCor(No no) {
        no.cor = !no.cor;
        if (no.esq != null) no.esq.cor = !no.esq.cor;
        if (no.dir != null) no.dir.cor = !no.dir.cor;
    }

    public void mostrar() {
        int[] index = {0};
        mostrar(raiz, index);
    }

    private void mostrar(No no, int[] index) {
        if (no != null) {
            mostrar(no.esq, index);
            no.elemento.imprimir(index[0]++);
            mostrar(no.dir, index);
        }
    }

    public boolean pesquisar(String nome) {
        System.out.print("raiz ");
        return pesquisar(nome, raiz);
    }

    private boolean pesquisar(String nome, No no) {
        comparacoes++;
        if (no == null) {
            System.out.println("NAO");
            return false;
        }
        comparacoes++;
        if (nome.equalsIgnoreCase(no.elemento.getName())) {
            System.out.println("SIM");
            return true;
        }
        comparacoes++;
        if (nome.compareToIgnoreCase(no.elemento.getName()) < 0) {
            System.out.print("esq ");
            return pesquisar(nome, no.esq);
        } else {
            System.out.print("dir ");
            return pesquisar(nome, no.dir);
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

public class Exercicio4 {
    public static void main(String[] args) {
        ArvoreAlvinegra pokedexArvore = new ArvoreAlvinegra();
        Scanner inputScanner = new Scanner(System.in);

        while (true) {
            String idInput = inputScanner.nextLine().trim();
            if (idInput.equalsIgnoreCase("FIM")) {
                break;
            }

            Pokemon pokemon = buscarPokemonPorID(idInput);
            if (pokemon != null) {
                try {
                    pokedexArvore.inserir(pokemon);
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
            System.out.println(idInput);
            pokedexArvore.pesquisar(idInput);
            comparisons += pokedexArvore.comparacoes;
        }

        Instant end = Instant.now();
        long tempoExecucao = Duration.between(start, end).toMillis();

        String matricula = "1448840";
        String nomeArquivo = matricula + "_avinegra.txt";

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
