package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

public class Max<T> extends BinaryOperator<T> {
    public Max(AnyExpression<T> firstOperand, AnyExpression<T> secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public T eval(T a, T b, final Evaluator<T> evaluator) {
        return evaluator.max(a, b);
    }

    @Override
    public String getOperator() {
        return "max";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public int getSpecial() {
        return 1;
    }
}
