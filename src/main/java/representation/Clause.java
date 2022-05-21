package representation;

import java.util.ArrayList;
import java.util.List;


public class Clause {

    private final List<Integer> literals;

    public Clause(List<Integer> literals) {
        this.literals = literals;
    }

    public Clause(Clause clause) {

        List<Integer> clonedLiterals = new ArrayList<>(clause.getLiterals().size());
        clonedLiterals.addAll(clause.getLiterals());

        this.literals = clonedLiterals;

    }

    public List<Integer> getLiterals() {
        return literals;
    }

    public boolean containsLiteral(Integer literal) {
        return this.literals.contains(literal);
    }

    public void removeLiteral(Integer literal) {
        this.literals.remove(literal);
    }

    //состоит ли clause из всего 1 литерала
    public boolean isUnit() {
        return this.literals.size() == 1;
    }

    public boolean isEmpty() {
        return this.literals.isEmpty();
    }

    @Override
    public String toString() {
        return "Clause [literals=" + literals + "] }";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((literals == null) ? 0 : literals.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Clause other = (Clause) obj;
        if (literals == null) {
            return other.literals == null;
        } else return literals.equals(other.literals);
    }

}
