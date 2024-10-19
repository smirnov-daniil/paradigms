package expression;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public abstract class BinaryOperator implements AnyExpression {
    private final BinaryOperand operands;

    private record BinaryOperand(AnyExpression first, AnyExpression second) {}

    public BinaryOperator(AnyExpression firstOperand, AnyExpression secondOperand) {
        this.operands = new BinaryOperand(Objects.requireNonNull(firstOperand), Objects.requireNonNull(secondOperand));
    }

    @Override
    public String toString() {
        return "(" + operands.first() + " " + getOperator() + " "  + operands.second() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(operands, getClass());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            BinaryOperator biOp = (BinaryOperator) obj;
            return Objects.equals(operands, biOp.operands);
        }
        return false;
    }

    protected abstract int eval(int a, int b);

    /*protected abstract BigInteger eval(BigInteger a, BigInteger b) throws UnsupportedOperationException;*/

    @Override
    public final int evaluate(int x) {
        return eval(operands.first().evaluate(x), operands.second().evaluate(x));
    }

    @Override
    public final int evaluate(int x, int y, int z) {
        return eval(operands.first().evaluate(x, y, z), operands.second().evaluate(x, y, z));
    }

    @Override
    public final int evaluate(List<Integer> variables) {
        return eval(operands.first().evaluate(variables), operands.second().evaluate(variables));
    }

    /*@Override
    public final BigInteger evaluate(BigInteger x) {
        return eval(operands.first().evaluate(x), operands.second().evaluate(x));
    }*/

    @Override
    public String toMiniString() {
        StringBuilder miniString = new StringBuilder();

        if (operands.first().getPriority() < getPriority()) {
            miniString.append("(").append(operands.first().toMiniString()).append(")");
        } else {
            miniString.append(operands.first().toMiniString());
        }
        miniString.append(" ").append(getOperator()).append(" ");
        if (operands.second().getPriority() < getPriority() ||
                operands.second().getPriority() == getPriority() &&
                        (getIsNotAssociative() ||
                                operands.second().getIsIntegerDependent()) ||
                                    getSpecial() != operands.second().getSpecial() &&
                                            operands.second().getSpecial() != 0) {
            miniString.append("(").append(operands.second.toMiniString()).append(")");
        } else {
            miniString.append(operands.second.toMiniString());
        }
        return miniString.toString();
    }
}
