import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.stream.Collectors;

public class Solver {
    private final int numberOfLiterals;
    private final Set<Clause> setOfClauses;
    private Set<Literal> lastTemp = new LinkedHashSet<>();
    private SortedSet<Integer> setOfUnassignLiterals = new TreeSet<>();
    private List<Literal> model = new LinkedList<>();
    private int[] assignCount;

    public Solver(int numberOfLiterals, Set<Clause> setOfClauses) {
        this.numberOfLiterals = numberOfLiterals;
        this.setOfClauses = setOfClauses;
        assignCount = new int[numberOfLiterals];
        for (int i = 1 ; i <= numberOfLiterals ; i++) setOfUnassignLiterals.add(i);
    }

    public void findModel() {
        while (model.size() != numberOfLiterals) {

            int deduction = deduce();
            switch (deduction) {
                case 0 -> newBranch();
                case 1 -> {
                    if (checkConflict()) {
                        if (lastTemp.size() == 0) return;
                        back();
                        if (model.size() == 0) return;
                    }
                }
                case -1 -> {
                    if (lastTemp.size() == 0) return;
                    back();
                }
            }
        }
    }

    private int deduce() {
        for (Clause clause : setOfClauses) {
            List<Literal> listOfLiterals = clause.getListOfLiteras();
            if (listOfLiterals.size() == 1) {
                Literal literal = listOfLiterals.get(0);
                if (assignCount[literal.getValue() - 1] == 0) {
                    model.add(new Literal(literal.getValue(), literal.getSign()));
                    setOfUnassignLiterals.remove(literal.getValue());
                    assignCount[literal.getValue() - 1]++;
                    return 1;
                } else if (literal.getSign() != checkSignInModel(literal.getValue())) return -1;
            } else {
                Literal temp = null;
                int unassignedCount = 0;
                for (Literal lit : listOfLiterals) {
                    if (assignCount[lit.getValue() - 1] == 0) {
                        unassignedCount++;
                        temp = lit;
                    }
                }
                if (unassignedCount == 1) {
                    if (checkClause(listOfLiterals)) {
                        model.add(new Literal(temp.getValue(), temp.getSign()));
                        setOfUnassignLiterals.remove(temp.getValue());
                        assignCount[temp.getValue() - 1]++;
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    private boolean checkClause(@NotNull List<Literal> listOfLiteras) {
        boolean signOfClause = true;
        for (Literal literal : listOfLiteras) {
            if (assignCount[literal.getValue() - 1] != 0 && checkSignInModel(literal.getValue()) == literal.getSign())
                signOfClause = false;
        }
        return signOfClause;
    }

    private boolean checkSignInModel(int lit) {
        for (Literal l : model) if (l.getValue() == lit) return l.getSign();
        return false;
    }

    private boolean checkConflict() {
        for (Clause c : setOfClauses) {
            int unassignedCount = 0;
            for (Literal literal : c.getListOfLiteras()) if (assignCount[literal.getValue() - 1] == 0) unassignedCount++;
            if (unassignedCount == 0 && checkClause(c.getListOfLiteras())) return true;
        }
        return false;
    }

    private void newBranch() {
        if (setOfUnassignLiterals.size() == 0) {
            back();
            return;
        }
        int first = setOfUnassignLiterals.first();
        setOfUnassignLiterals.remove(first);
        Literal literal = new Literal(first, true);
        assignCount[literal.getValue() - 1]++;
        lastTemp.add(literal);
        model.add(new Literal(literal.getValue(), literal.getSign()));
    }

    private void back() {
        Literal temp = null;
        while (temp == null && model.size() != 0) {
            temp = model.remove(model.size() - 1);
            Literal last = null;
            for (Literal lit : lastTemp) last = lit;
            assert last != null;
            if (temp.getValue() == last.getValue()) {
                lastTemp.remove(last);
                assignCount[temp.getValue() - 1] = 0;
                if (temp.getSign()) {
                    model.add(new Literal(temp.getValue(), false));
                    assignCount[temp.getValue() - 1]++;
                    lastTemp.add(new Literal(temp.getValue(), false));
                } else {
                    setOfUnassignLiterals.add(temp.getValue());
                    assignCount[temp.getValue() - 1] = 0;
                    temp = null;
                }
            } else {
                setOfUnassignLiterals.add(temp.getValue());
                assignCount[temp.getValue() - 1] = 0;
                temp = null;
            }
        }
    }

    public void printModel(String fileName, boolean printSolution) {
        System.out.println("\nFile to SAT using DPLL: " + fileName);
        System.out.println("SAT");
        if (printSolution) {
            System.out.print("Solution: ");
            model.stream()
                    .sorted(Comparator.comparing(Literal::getValue))
                    .collect(Collectors.toList())
                    .forEach(literal -> System.out.print(literal.toInteger() + " "));
        }
    }

    public int[] getModel() {
        return model.stream().mapToInt(Literal::toInteger).toArray();
    }
}
