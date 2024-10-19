package expression.generic.operators;

import expression.generic.evaluators.Evaluator;
import java.util.Objects;

public abstract class BinaryOperator<T> implements AnyExpression<T> {
    private final BinaryOperand<T> operands;

    private record BinaryOperand<T>(AnyExpression<T> first, AnyExpression<T> second) {}

    public BinaryOperator(AnyExpression<T> firstOperand, AnyExpression<T> secondOperand) {
        this.operands = new BinaryOperand<>(Objects.requireNonNull(firstOperand), Objects.requireNonNull(secondOperand));
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
            BinaryOperator<?> biOp = (BinaryOperator<?>) obj;
            return Objects.equals(operands, biOp.operands);
        }
        return false;
    }

    protected abstract T eval(T a, T b, final Evaluator<T> evaluator);

    @Override
    public final T evaluate(T x, T y, T z, final Evaluator<T> evaluator) {
        return eval(operands.first().evaluate(x, y, z, evaluator), operands.second().evaluate(x, y, z, evaluator), evaluator);
    }

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
