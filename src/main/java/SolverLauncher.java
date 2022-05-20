import org.kohsuke.args4j.*;
import java.util.*;

public class SolverLauncher {
    @Option(name = "-p", metaVar = "Solution", usage = "Print solution")
    private boolean printSolution;

    @Argument(metaVar = "InputFiles", usage = "Input files names")
    private List<String> inputFilesNames = new LinkedList<>();

    public static void main(String[] args) {
        new SolverLauncher().launch(args);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar SAT_solver.jar [-s] fileName1 fileName2 fileName3");
            parser.printUsage(System.err);
            System.exit(1);
        }
        if (inputFilesNames.isEmpty()) System.out.println("no input files");
        for (String fileName : inputFilesNames) {
            ParserForDimacsFiles prsr = new ParserForDimacsFiles(fileName);
            Set<Clause> listOfClauses = prsr.parse();
            Solver solver = new Solver(prsr.getLiteralsCount(), listOfClauses);
            solver.findModel();
            solver.printModel(fileName, printSolution);
        }
    }
}