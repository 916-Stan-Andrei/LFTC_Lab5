import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Grammar g1 = new Grammar();
        String filename;

        filename = "g1.txt";
        printCFG(filename, g1);

        Grammar g2 = new Grammar();

        filename = "g2.txt";
        printCFG(filename, g2);
    }

    private static void printCFG(String filename, Grammar g) {
        boolean isCFG;
        try {
            g.readFromFile(filename);
            System.out.println(g);

            String grammar = filename.split("\\.")[0];
            isCFG = g.checkCFG();
            if (isCFG) {
                System.out.println("The grammar " + grammar + " is a CFG\n");
            } else {
                System.out.println("The grammar " + grammar + " is not a CFG\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

