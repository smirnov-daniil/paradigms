package expression;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public abstract class UnaryOperator implements AnyExpression {
    private final AnyExpression operand;
    public UnaryOperator(AnyExpression operand) {
        this.operand = Objects.requireNonNull(operand);
    }

    @Override
    public String toString() {
        return (getType() == Type.PREFIX ? (getOperator() + "(") : "(") +
                operand +
                (getType() == Type.SUFFIX ? (")" + getOperator()) : ")");
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand, getClass(), getType());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            UnaryOperator uOp = (UnaryOperator) obj;
            return Objects.equals(operand, uOp.operand) &&
                    getType() == uOp.getType();
        }
        return false;
    }
    protected abstract int eval(int a);

    /*protected abstract BigInteger eval(BigInteger a) throws UnsupportedOperationException;*/

    @Override
    public final int evaluate(int x) {
        return eval(operand.evaluate(x));
    }

    @Override
    public final int evaluate(int x, int y, int z) {
        return eval(operand.evaluate(x, y, z));
    }

    @Override
    public final int evaluate(List<Integer> variables) {
        return eval(operand.evaluate(variables));
    }

    /*@Override
    public final BigInteger evaluate(BigInteger x) {
        return eval(operand.evaluate(x));
    }*/

    @Override
    public String toMiniString() {
        StringBuilder miniString = new StringBuilder();
        if (operand.getPriority() < getPriority()) {
            if (getType() == Type.PREFIX) {
                miniString.append(getOperator());
            }
            miniString.append("(").append(operand.toMiniString()).append(")");
            if (getType() == Type.SUFFIX) {
                miniString.append(getOperator());
            }
            return miniString.toString();
        }
        if (getType() == Type.PREFIX) {
            miniString.append(getOperator()).append(" ").append(operand.toMiniString());
        }
        if (getType() == Type.SUFFIX) {
            miniString.append(operand.toMiniString()).append(" ").append(getOperator());
        }
        return miniString.toString();
    }
}
