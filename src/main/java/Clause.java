import java.util.*;

public class Clause {
    protected Set<Literal> literals = new HashSet<>();

    public void addLiteralToClause(Literal lit) {
        literals.add(lit);
    }

    public List<Literal> getListOfLiteras() {
        return literals.stream().toList();
    }
}
