
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import representation.Clause;
import Solvers.DPLLSolver;
import parser.DimacsParser;

public class DPLLParserTest{

    @Test
    public void positiveSimpleTests_shouldReturnSatisfiable() throws IOException{
        runtTest("src/examples/satisfiable_by_up.cnf", true);
    }

    @Test
    public void negativeSimpleTests_shouldReturnSatisfiable() throws IOException{
        runtTest("src/examples/unsatisfiable_by_up.cnf", false);
    }

    @Test
    public void satLibNegativeTest_shouldReturnSatisfiable() throws IOException{
        runtTest("src/examples/uuf75-024.cnf", false);
    }

    @Test
    public void satLibPositiveTest_shouldReturnSatisfiable() throws IOException{
        runtTest("src/examples/uf75-040.cnf", true);
    }

    @Test
    public void minimunSatisfiableTest_shouldReturnSatisfiable() throws IOException{
        runtTest("src/examples/minimum_satisfiable.cnf", true);
    }

    @Test
    public void minTest_shouldReturnSatisfiable() throws IOException{
        runtTest("src/examples/min.cnf", true);
    }

    @Test
    public void minTest_shouldReturnSatisfiable1() throws IOException{
        runtTest("src/examples/ex1.cnf", true);
    }

    @Test
    public void minTest_shouldReturnSatisfiable2() throws IOException{
        runtTest("src/examples/ex2.cnf", true);
    }

    private void runtTest(String filePath, boolean expectedResult) throws IOException{

        DimacsParser parser = new DimacsParser();
        List<Clause> clauses = parser.parse(filePath);

        DPLLSolver solver =  new DPLLSolver();


        Assert.assertEquals(expectedResult, solver.isSatisfiable(clauses));

    }

}