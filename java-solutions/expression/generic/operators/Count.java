package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

public class Count<T> extends UnaryOperator<T> {
    public Count(AnyExpression<T> operand) {
        super(operand);
    }

    @Override
    public T eval(T a, final Evaluator<T> evaluator) {
        return evaluator.count(a);
    }

    @Override
    public String getOperator() {
        return "count";
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
