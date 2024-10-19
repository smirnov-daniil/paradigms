package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

import java.util.Objects;

public abstract class UnaryOperator<T> implements AnyExpression<T> {
    private final AnyExpression<T> operand;
    public UnaryOperator(AnyExpression<T> operand) {
        this.operand = operand;
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
            UnaryOperator<?> uOp = (UnaryOperator<?>) obj;
            return Objects.equals(operand, uOp.operand) &&
                    getType() == uOp.getType();
        }
        return false;
    }

    protected abstract T eval(T a, final Evaluator<T> evaluator);

    @Override
    public final T evaluate(T x, T y, T z, final Evaluator<T> evaluator) {
        return eval(operand.evaluate(x, y, z, evaluator), evaluator);
    }

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
