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

class NoPrimario {
    public int chave; 
    public NoPrimario esq, dir;
    public ArvoreSecundaria arvoreSecundaria; 

    public NoPrimario(int chave) {
        this.chave = chave;
        this.esq = this.dir = null;
        this.arvoreSecundaria = new ArvoreSecundaria();
    }
}

class NoSecundario {
    public String chave;
    public NoSecundario esq, dir;

    public NoSecundario(String chave) {
        this.chave = chave;
        this.esq = this.dir = null;
    }
}

class ArvoreSecundaria {
    private NoSecundario raiz;

    public ArvoreSecundaria() {
        raiz = null;
    }

    public void inserir(String chave) {
        raiz = inserir(chave, raiz);
    }

    private NoSecundario inserir(String chave, NoSecundario no) {
        if (no == null) {
            no = new NoSecundario(chave);
        } else if (chave.compareToIgnoreCase(no.chave) < 0) {
            no.esq = inserir(chave, no.esq);
        } else if (chave.compareToIgnoreCase(no.chave) > 0) {
            no.dir = inserir(chave, no.dir);
        }
        return no;
    }

    public boolean pesquisar(String chave) {
        boolean encontrado = pesquisar(chave, raiz);
        return encontrado;
    }

    private boolean pesquisar(String chave, NoSecundario no) {
        if (no == null) {
            return false;
        }
        if (chave.compareToIgnoreCase(no.chave) == 0) {
            return true;
        }
        if (chave.compareToIgnoreCase(no.chave) < 0) {
            System.out.print("esq ");
            return pesquisar(chave, no.esq);
        } else {
            System.out.print("dir ");
            return pesquisar(chave, no.dir);
        }
    }

    public void mostrar() {
        mostrar(raiz);
    }

    private void mostrar(NoSecundario no) {
        if (no != null) {
            mostrar(no.esq);
            System.out.println(no.chave);
            mostrar(no.dir);
        }
    }
}

class ArvorePrimaria {
    private NoPrimario raiz;

    public ArvorePrimaria() {
        raiz = null;
    }

    public void inserir(int chave) {
        raiz = inserir(chave, raiz);
    }

    private NoPrimario inserir(int chave, NoPrimario no) {
        if (no == null) {
            no = new NoPrimario(chave);
        } else if (chave < no.chave) {
            no.esq = inserir(chave, no.esq);
        } else if (chave > no.chave) {
            no.dir = inserir(chave, no.dir);
        }
        return no;
    }

    public NoPrimario pesquisar(int chave) {
        NoPrimario resultado = pesquisar(chave, raiz);
        return resultado;
    }

    private NoPrimario pesquisar(int chave, NoPrimario no) {
        if (no == null) {
            return null;
        }
        if (chave == no.chave) {
            return no;
        }
        if (chave < no.chave) {
            return pesquisar(chave, no.esq);
        } else {
            return pesquisar(chave, no.dir);
        }
    }

    public boolean pesquisarGeral(String nome) {
        System.out.print("raiz ");
        return pesquisarGeral(nome, raiz);
    }
    
    private boolean pesquisarGeral(String nome, NoPrimario no) {
        if (no == null) {
            return false; 
        }
    
        if (no.arvoreSecundaria.pesquisar(nome)) {
            return true;
        }
    
        System.out.print(" ESQ ");
        if (pesquisarGeral(nome, no.esq)) {
            return true;
        }
    
        System.out.print(" DIR ");
        return pesquisarGeral(nome, no.dir);
    }
    

    public void mostrar() {
        mostrar(raiz);
    }

    private void mostrar(NoPrimario no) {
        if (no != null) {
            mostrar(no.esq);
            System.out.println("Chave: " + no.chave);
            if (no.arvoreSecundaria != null) {
                no.arvoreSecundaria.mostrar();
            }
            mostrar(no.dir);
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

public class Exercicio2 {
    public static void main(String[] args) {
        ArvorePrimaria arvorePrimaria = new ArvorePrimaria();
        int[] chaves = {7, 3, 11, 1, 5, 9, 13, 0, 2, 4, 6, 8, 10, 12, 14};

        for (int chave : chaves) {
            arvorePrimaria.inserir(chave);
        }

        Scanner inputScanner = new Scanner(System.in);
        while (true) {
            String idInput = inputScanner.nextLine().trim();
            if (idInput.equalsIgnoreCase("FIM")) {
                break;
            }

            Pokemon pokemon = buscarPokemonPorID(idInput);
            if (pokemon != null) {
                int chavePrimaria = pokemon.getCaptureRate() % 15;
                NoPrimario noPrimario = arvorePrimaria.pesquisar(chavePrimaria);
                if (noPrimario != null) {
                    noPrimario.arvoreSecundaria.inserir(pokemon.getName());
                }
            }
        }

        Instant start = Instant.now();
        int comparisons = 0;

        while (true) {
            String idInput = inputScanner.nextLine().trim();
            if (idInput.equalsIgnoreCase("FIM")) {
                break;
            }
            System.out.println("=> " + idInput);
            if(arvorePrimaria.pesquisarGeral(idInput)){
                System.out.println(" SIM");
            }else{
                System.out.println(" NAO");
            }
            comparisons ++;
        }

        Instant end = Instant.now();
        long tempoExecucao = Duration.between(start, end).toMillis();

        String matricula = "1448840";
        String nomeArquivo = matricula + "_arvoreArvore.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write(matricula + "\t" + tempoExecucao + "ms\tComparações: " + comparisons);
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
