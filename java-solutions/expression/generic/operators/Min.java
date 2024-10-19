package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

public class Min<T> extends BinaryOperator<T> {
    public Min(AnyExpression<T> firstOperand, AnyExpression<T> secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public T eval(T a, T b, final Evaluator<T> evaluator) {
        return evaluator.min(a, b);
    }

    @Override
    public String getOperator() {
        return "min";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public int getSpecial() {
        return 2;
    }

}
