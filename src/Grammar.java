import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grammar {
    private static final String EPSILON = "ε";

    private List<String> nonterminals;
    private List<String> terminals;
    private String startingSymbol;
    private final Map<String, List<List<String>>> productions;

    public Grammar() {
        nonterminals = new ArrayList<>();
        terminals = new ArrayList<>();
        startingSymbol = "";
        productions = new HashMap<>();
    }

    private List<String> processLine(String line, String delimiter) {
        List<String> elements = new ArrayList<>();
        line = line.trim().replaceAll("[{}]", "");
        String[] parts = line.split(delimiter);
        for (String element : parts) {
            elements.add(element.trim());
        }
        return elements;
    }

    public void readFromFile(String fileName) throws IOException {
        nonterminals.clear();
        terminals.clear();
        startingSymbol = "";
        productions.clear();

        BufferedReader file = new BufferedReader(new FileReader(fileName));
        String line = file.readLine();
        nonterminals = processLine(line.split("=")[1], ", ");

        line = file.readLine();
        String terminalLine = line.substring(line.indexOf('=') + 1).trim();
        terminals = processLine(terminalLine, " ");


        line = file.readLine();
        startingSymbol = processLine(line.split("=")[1], ", ").get(0);

        while ((line = file.readLine()) != null) {
            if (line.contains(" -> ")) {
                String[] parts = line.split(" -> ");
                String source = parts[0].trim();
                String[] productions = parts[1].split("\\|");
                for (String production : productions) {
                    production = production.trim().replace("ε", EPSILON);
                    List<String> rhs = processLine(production, " ");
                    this.productions.computeIfAbsent(source, k -> new ArrayList<>()).add(rhs);
                }
            }
        }
        file.close();
    }

    public boolean checkCFG() {
        boolean hasStartingSymbol = false;
        for (String key : productions.keySet()) {
            if (key.equals(startingSymbol)) {
                hasStartingSymbol = true;
            }
            if (!nonterminals.get(0).contains(key)) {
                return false;
            }
        }
        if (!hasStartingSymbol) {
            return false;
        }
        for (List<List<String>> production : productions.values()) {
            for (List<String> rhs : production) {
                for (String value : rhs) {
                    if (!nonterminals.get(0).contains(value) && !terminals.contains(value) && !value.equals(EPSILON)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "N = " + nonterminals + "\n" +
                "E = " + terminals + "\n" +
                "S = " + startingSymbol + "\n" +
                "P = " + productions + "\n";
    }
}
