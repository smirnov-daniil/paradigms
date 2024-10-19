package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

public class Xor<T> extends BinaryOperator<T> {
    public Xor(AnyExpression<T> firstOperand, AnyExpression<T> secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public T eval(T a, T b, final Evaluator<T> evaluator) {
        return evaluator.xor(a, b);
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public String getOperator() {
        return "^";
    }
}
