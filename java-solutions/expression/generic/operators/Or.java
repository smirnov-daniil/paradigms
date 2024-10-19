package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

public class Or<T> extends BinaryOperator<T> {
    public Or(AnyExpression<T> firstOperand, AnyExpression<T> secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public T eval(T a, T b, final Evaluator<T> evaluator) {
        return evaluator.or(a, b);
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public String getOperator() {
        return "|";
    }
}
