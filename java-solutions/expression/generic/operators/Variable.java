package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

import java.util.Objects;

public class Variable<T> implements AnyExpression<T> {
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
    public T evaluate(T x, T y, T z, final Evaluator<T> evaluator) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalArgumentException("Unknown variable " + name);
        };
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            Variable<?> var = (Variable<?>) obj;
            return Objects.equals(name, var.name) && index == var.index;
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
