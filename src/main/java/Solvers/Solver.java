package Solvers;

import java.util.List;

import representation.Clause;


public interface Solver {

    boolean isSatisfiable(List<Clause> clauses);


}
