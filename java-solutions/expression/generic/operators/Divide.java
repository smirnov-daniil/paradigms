package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

public class Divide<T> extends BinaryOperator<T> {
    public Divide(AnyExpression<T> firstOperand, AnyExpression<T> secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public T eval(T a, T b, final Evaluator<T> evaluator) {
        return evaluator.div(a, b);
    }

    @Override
    public String getOperator() {
        return "/";
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public boolean getIsNotAssociative() {
        return true;
    }

    @Override
    public boolean getIsIntegerDependent() {
        return true;
    }
}
