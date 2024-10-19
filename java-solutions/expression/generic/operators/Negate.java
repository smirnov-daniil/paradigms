package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

public class Negate<T> extends UnaryOperator<T> {
    public Negate(AnyExpression<T> operand) {
        super(operand);
    }

    @Override
    public T eval(T a, final Evaluator<T> evaluator) {
        return evaluator.neg(a);
    }

    @Override
    public String getOperator() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 15;
    }

    @Override
    public Type getType() {
        return Type.PREFIX;
    }
}
