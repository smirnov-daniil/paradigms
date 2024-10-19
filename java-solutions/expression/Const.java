package expression;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class Const implements AnyExpression {
    private final Number number;
    private Const(Number number) {
        this.number = Objects.requireNonNull(number);
    }

    public Const(int number) {
        this.number = number;
    }

    public Const(BigInteger number) {
        this((Number) number);
    }

    @Override
    public int evaluate(int variable) {
        return number.intValue();
    }

    @Override
    public final int evaluate(List<Integer> variables) {
        return number.intValue();
    }

    /*@Override
    public BigInteger evaluate(BigInteger variable) {
        return (BigInteger) number;
    }*/

    @Override
    public int evaluate(int x, int y, int z) {
        return number.intValue();
    }

    @Override
    public String toString() {
        return number.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            Const con = (Const) obj;
            return Objects.equals(number, con.number);
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
