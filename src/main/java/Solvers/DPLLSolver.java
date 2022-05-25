package Solvers;

import java.io.IOException;
import java.util.*;

import parser.DimacsParser;

import representation.Clause;



public class DPLLSolver implements Solver {

    Set<Integer> solution = new HashSet<>();
    Integer point_true = 0;
    Set<Integer> lastStep = new HashSet<>();

    // принимает на вход лист всех clause и выясняет решаемы ли они
    @Override
    public boolean isSatisfiable(List<Clause> clauses) {

        lastStep.clear();
        unitPropagation(clauses);
        solution.addAll(lastStep);

        if(clauses.isEmpty()) {
            point_true++;
            System.out.println("SAT");
            System.out.println("Solution is " + solution);
            return true;
        }

        if(containsEmptyClauses(clauses)) {
            return false;
        }

        Integer literal = selectLiteralForSplitting(clauses);
        Integer complementaryLiteral = literal * -1;


        if(isSatisfiable(cloneAndAddClauseWithLiteral(clauses, literal))) {
            if (point_true == 0) {
                System.out.println("SAT");
                System.out.println("Solution is " + solution);
            }
            return true;
        }
        else {
            solution.removeIf(lit -> lastStep.contains(lit));
            return isSatisfiable(cloneAndAddClauseWithLiteral(clauses, complementaryLiteral));
        }

    }


    // Делаем список-клон со всеми clause, добавляем туда clause, состоящий из литерала
    private List<Clause> cloneAndAddClauseWithLiteral(List<Clause> clauses, Integer literal) {

        Clause clause = createClauseFromLiteral(literal);

        List<Clause> clone = clone(clauses);
        clone.add(clause);

        return clone;
    }

    // Делаем clause из литерала
    private Clause createClauseFromLiteral(Integer literal) {

        ArrayList<Integer> literals = new ArrayList<>();
        literals.add(literal);
        Clause clause = new Clause(literals);
        return clause;

    }

    // Создаём список-клон для clause
    private List<Clause> clone(List<Clause> clauses) {

        List<Clause> clone = new ArrayList<>(clauses.size()+1);
        for(Clause clause: clauses){
            clone.add(new Clause(clause));
        }
        return clone;

    }


    // Считаем какой литерал встречается чаще из самых коротких clause
    private Integer selectLiteralForSplitting(List<Clause> clauses) {

        int shortestClausesLength = calculateShortestClausesLength(clauses);

        Map<Integer, Integer> countOcurrencesByLiteral = new HashMap<>();

        for(Clause clause: clauses){
            if(clause.getLiterals().size() == shortestClausesLength){
                List<Integer> literals = clause.getLiterals();
                for(Integer literal:literals){

                    if(!countOcurrencesByLiteral.containsKey(literal)){
                        countOcurrencesByLiteral.put(literal, 0);
                    }

                    Integer oldValue = countOcurrencesByLiteral.get(literal);
                    countOcurrencesByLiteral.put(literal, oldValue + 1);

                }
            }
        }

        Integer maxOcсurences = null;
        Integer maxFrequencyLiteral = null;

        for(Map.Entry<Integer, Integer> entry: countOcurrencesByLiteral.entrySet()){

            Integer literal = entry.getKey();
            Integer literalOcurrences = entry.getValue();

            Integer complementaryLiteral = literal * -1;
            Integer complementaryLiteralOcurrences = (countOcurrencesByLiteral.containsKey(complementaryLiteral))
                    ? countOcurrencesByLiteral.get(complementaryLiteral) : 0;

            Integer totalOcсurences = literalOcurrences + complementaryLiteralOcurrences;

            if(maxOcсurences == null){
                maxOcсurences = totalOcсurences;
                if (literalOcurrences >= complementaryLiteralOcurrences) {
                    maxFrequencyLiteral = literal;
                } else maxFrequencyLiteral = complementaryLiteral;

            }

            if(totalOcсurences > maxOcсurences){
                maxOcсurences = totalOcсurences;
                if (literalOcurrences >= complementaryLiteralOcurrences) {
                    maxFrequencyLiteral = literal;
                } else maxFrequencyLiteral = complementaryLiteral;
            }

        }

        return maxFrequencyLiteral;

    }

    // Поиск самого маленького clause
    private int calculateShortestClausesLength(List<Clause> clauses) {

        Integer shortestClausesLength = null;

        for(Clause clause: clauses){

            if(shortestClausesLength == null && !clause.isEmpty() ){
                shortestClausesLength = clause.getLiterals().size();
            }

            if (!clause.isEmpty() && clause.getLiterals().size() < shortestClausesLength) {
                shortestClausesLength = clause.getLiterals().size();
            }
        }

        return shortestClausesLength;

    }

    // Смотрим, есть ли пустые clause
    private boolean containsEmptyClauses(List<Clause> clauses) {

        for (Clause clause : clauses) {
            if (clause.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    // Удаляем все лишнее, если есть одинокие литералы
    private void unitPropagation(List<Clause> clauses) {

        List<Integer> unitClausesLiterals = getUnitClausesLiterals(clauses);

        while(!unitClausesLiterals.isEmpty()){
            propagate(unitClausesLiterals, clauses);
            unitClausesLiterals = getUnitClausesLiterals(clauses);
        }



    }

    // Достаём единственный литерал из единичных clause, пихаем их в список таких одиночек :(
    private List<Integer> getUnitClausesLiterals(List<Clause> clauses) {

        List<Integer> unitClausesLiterals = new ArrayList<>();

        for(Clause clause: clauses){
            if(clause.isUnit()){
                unitClausesLiterals.add(clause.getLiterals().get(0));
                if (!solution.contains(clause.getLiterals().get(0))) {
                    if (solution.contains(clause.getLiterals().get(0) * -1)) {
                        solution.remove(clause.getLiterals().get(0) * -1);
                    }
                    lastStep.add(clause.getLiterals().get(0));

                }
            }
        }

        return unitClausesLiterals;
    }

    // Делаем propagateForUnitClause для каждого одинокого литерала
    private void propagate(List<Integer> unitClausesLiterals, List<Clause> clauses) {

        for(Integer literal: unitClausesLiterals){
            propagateForUnitClause(literal, clauses);
        }

    }

    // Смотрим содержится ли литерал в каких-то clause.
    // Если да - удаляем этот clause, тк он тоже будет сразу истинным
    // Из clause с отрицательным литералом удаляем его ???
    private void propagateForUnitClause(Integer literal, List<Clause> clauses) {

        Integer complementaryLiteral = literal * -1;

        for(int i=0; i<clauses.size(); i++){

            Clause clause = clauses.get(i);

            if(clause.containsLiteral(literal)){
                clauses.remove(i);
                i--;
            }

            if(clause.containsLiteral(complementaryLiteral)){
                clause.removeLiteral(complementaryLiteral);
            }

        }

    }

    public static void main(String[] args) throws IOException {

        DimacsParser parser = new DimacsParser();
        List<Clause> clauses = parser.parse("src/examples/minimum_satisfiable.cnf");

        Solver solver =  new DPLLSolver();

        System.out.println(solver.isSatisfiable(clauses));

    }

}
