package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

public class And<T> extends BinaryOperator<T> {
    public And(AnyExpression<T> firstOperand, AnyExpression<T> secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public T eval(T a, T b, final Evaluator<T> evaluator) {
        return evaluator.and(a, b);
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public String getOperator() {
        return "&";
    }
}
