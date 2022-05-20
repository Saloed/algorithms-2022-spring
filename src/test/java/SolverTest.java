import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.specs.IProblem;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class SolverTest {
    String DIR = "src/main/resources/";
    String EXT = ".cnf";

    @org.junit.jupiter.api.Test
    void modelTest1() {
        ParserForDimacsFiles myParser = new ParserForDimacsFiles(DIR + 1 + EXT);
        Set<Clause> listOfClauses = myParser.parse();
        Solver mySolver = new Solver(myParser.getLiteralsCount(), listOfClauses);
        mySolver.findModel();
        int[] arr = mySolver.getModel();
        assertEquals(0, arr.length);
    }

    @org.junit.jupiter.api.Test
    void modelTest2() throws Exception {
        ParserForDimacsFiles myParser = new ParserForDimacsFiles(DIR + 2 + EXT);
        Set<Clause> listOfClauses = myParser.parse();
        Solver mySolver = new Solver(myParser.getLiteralsCount(), listOfClauses);
        mySolver.findModel();
        int[] arr = mySolver.getModel();
        IProblem problem = new DimacsReader(SolverFactory.newDefault()).parseInstance(DIR + 2 + EXT);
        assertTrue(problem.isSatisfiable(new VecInt(arr)));
    }

    @org.junit.jupiter.api.Test
    void modelTest3() throws Exception {
        ParserForDimacsFiles myParser = new ParserForDimacsFiles(DIR + 3 + EXT);
        Set<Clause> listOfClauses = myParser.parse();
        Solver mySolver = new Solver(myParser.getLiteralsCount(), listOfClauses);
        mySolver.findModel();
        int[] arr = mySolver.getModel();
        IProblem problem = new DimacsReader(SolverFactory.newDefault()).parseInstance(DIR + 3 + EXT);
        assertTrue(problem.isSatisfiable(new VecInt(arr)));
    }

    @org.junit.jupiter.api.Test
    void modelTest4() throws Exception {
        ParserForDimacsFiles myParser = new ParserForDimacsFiles(DIR + 4 + EXT);
        Set<Clause> listOfClauses = myParser.parse();
        Solver mySolver = new Solver(myParser.getLiteralsCount(), listOfClauses);
        mySolver.findModel();
        int[] arr = mySolver.getModel();
        IProblem problem = new DimacsReader(SolverFactory.newDefault()).parseInstance(DIR + 4 + EXT);
        assertTrue(problem.isSatisfiable(new VecInt(arr)));
    }

    @org.junit.jupiter.api.Test
    void modelTest5() {
        ParserForDimacsFiles myParser = new ParserForDimacsFiles(DIR + 5 + EXT);
        Set<Clause> listOfClauses = myParser.parse();
        Solver mySolver = new Solver(myParser.getLiteralsCount(), listOfClauses);
        mySolver.findModel();
        int[] arr = mySolver.getModel();
        assertEquals(0, arr.length);
    }

    @org.junit.jupiter.api.Test
    void modelTest6() {
        ParserForDimacsFiles myParser = new ParserForDimacsFiles(DIR + 6 + EXT);
        Set<Clause> listOfClauses = myParser.parse();
        Solver mySolver = new Solver(myParser.getLiteralsCount(), listOfClauses);
        mySolver.findModel();
        int[] arr = mySolver.getModel();
        assertEquals(0, arr.length);
    }

    @org.junit.jupiter.api.Test
    void modelTest7() throws Exception {
        ParserForDimacsFiles myParser = new ParserForDimacsFiles(DIR + 7 + EXT);
        Set<Clause> listOfClauses = myParser.parse();
        Solver mySolver = new Solver(myParser.getLiteralsCount(), listOfClauses);
        mySolver.findModel();
        int[] arr = mySolver.getModel();
        IProblem problem = new DimacsReader(SolverFactory.newDefault()).parseInstance(DIR + 7 + EXT);
        assertTrue(problem.isSatisfiable(new VecInt(arr)));
    }

    @org.junit.jupiter.api.Test
    void modelTest8() throws Exception {
        ParserForDimacsFiles myParser = new ParserForDimacsFiles(DIR + 8 + EXT);
        Set<Clause> listOfClauses = myParser.parse();
        Solver mySolver = new Solver(myParser.getLiteralsCount(), listOfClauses);
        mySolver.findModel();
        int[] arr = mySolver.getModel();
        IProblem problem = new DimacsReader(SolverFactory.newDefault()).parseInstance(DIR + 8 + EXT);
        assertTrue(problem.isSatisfiable(new VecInt(arr)));
    }

    @org.junit.jupiter.api.Test
    void modelTest9() {
        ParserForDimacsFiles myParser = new ParserForDimacsFiles(DIR + 9 + EXT);
        Set<Clause> listOfClauses = myParser.parse();
        Solver mySolver = new Solver(myParser.getLiteralsCount(), listOfClauses);
        mySolver.findModel();
        int[] arr = mySolver.getModel();
        assertEquals(0, arr.length);
    }

    @org.junit.jupiter.api.Test
    void modelTest10() throws Exception {
        ParserForDimacsFiles myParser = new ParserForDimacsFiles(DIR + 10 + EXT);
        Set<Clause> listOfClauses = myParser.parse();
        Solver mySolver = new Solver(myParser.getLiteralsCount(), listOfClauses);
        mySolver.findModel();
        int[] arr = mySolver.getModel();
        IProblem problem = new DimacsReader(SolverFactory.newDefault()).parseInstance(DIR + 10 + EXT);
        assertTrue(problem.isSatisfiable(new VecInt(arr)));
    }
}