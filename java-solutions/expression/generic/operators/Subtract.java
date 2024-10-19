package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

public class Subtract<T> extends BinaryOperator<T> {
    public Subtract(AnyExpression<T> firstOperand, AnyExpression<T> secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public T eval(T a, T b, final Evaluator<T> evaluator) {
        return evaluator.sub(a, b);
    }

    @Override
    public String getOperator() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public boolean getIsNotAssociative() {
        return true;
    }
}
