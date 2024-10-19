package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

public class Multiply<T> extends BinaryOperator<T> {
    public Multiply(AnyExpression<T> firstOperand, AnyExpression<T> secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public T eval(T a, T b, final Evaluator<T> evaluator) {
        return evaluator.mul(a, b);
    }

    @Override
    public String getOperator() {
        return "*";
    }

    @Override
    public int getPriority() {
        return 10;
    }
}
