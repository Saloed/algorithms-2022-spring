import java.io.*;
import java.util.*;

public class ParserForDimacsFiles {
    private final String fileName;
    private int countOfLiterals = 0;
    private int countOfClauses = 0;

    public ParserForDimacsFiles(String fileName) {
        this.fileName = fileName;
    }

    public Set<Clause> parse() {
        Set<Clause> clauses = new LinkedHashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("p")) {
                    String[] paramLineArr = line.split("\\s+");
                    if (!paramLineArr[1].equals("cnf") || paramLineArr[2].equals("0") || paramLineArr[3].equals("0"))
                        throw new IllegalArgumentException("The file has an incorrect format");
                    countOfLiterals = Integer.parseInt(paramLineArr[2]);
                    countOfClauses = Integer.parseInt(paramLineArr[3]);
                    break;
                }
            }
            while ((line = reader.readLine()) != null) {
                Clause currentClause = new Clause();
                if (line.endsWith(" 0")) {
                    String[] clauseLineArr = line.substring(0, line.length() - 2).split("\\s+");
                    Arrays.stream(clauseLineArr)
                            .filter(literal -> !literal.equals(""))
                            .forEach(literal -> currentClause.addLiteralToClause(new Literal(Integer.parseInt(literal))));
                }
                clauses.add(currentClause);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("File not found! Check path!");
            System.exit(0);
        }
        return clauses;
    }

    public int getLiteralsCount() {
        return countOfLiterals;
    }

    public int getClausesCount() {
        return countOfClauses;
    }
}
