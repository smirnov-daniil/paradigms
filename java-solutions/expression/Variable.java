package expression;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class Variable implements AnyExpression {
    private final String name;
    private final int index;

    public Variable(String name, int index) {
        this.name = Objects.requireNonNull(name);
        this.index = index;
    }

    public Variable(String name) {
        this.name = Objects.requireNonNull(name);
        this.index = -1;
    }

    public Variable(int index) {
        this.name = "";
        this.index = index;
    }

    @Override
    public int evaluate(int variable) {
        return variable;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalArgumentException("Unknown variable " + name);
        };
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return variables.get(index);
    }


    /*@Override
    public BigInteger evaluate(BigInteger variable) {
        return variable;
    }*/

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            Variable var = (Variable) obj;
            return Objects.equals(name, var.name);
        }
        return false;
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public int getPriority() {
        return 100;
    }
}
