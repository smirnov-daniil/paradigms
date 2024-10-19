package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

public class Add<T> extends BinaryOperator<T> {
    public Add(AnyExpression<T> firstOperand, AnyExpression<T> secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public T eval(T a, T b, final Evaluator<T> evaluator) {
        return evaluator.add(a, b);
    }

    @Override
    public String getOperator() {
        return "+";
    }

    @Override
    public int getPriority() {
        return 5;
    }
}
