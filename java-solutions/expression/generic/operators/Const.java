package expression.generic.operators;

import expression.generic.evaluators.Evaluator;
import java.util.Objects;

public class Const<T> implements AnyExpression<T> {
    private final String stringNumber;

    public Const(String stringNumber) {
        this.stringNumber = Objects.requireNonNull(stringNumber);
    }

    @Override
    public T evaluate(T x, T y, T z, final Evaluator<T> evaluator) {
        return evaluator.parse(stringNumber);
    }

    @Override
    public String toString() {
        return stringNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stringNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            Const<?> con = (Const<?>) obj;
            return Objects.equals(stringNumber, con.stringNumber);
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
