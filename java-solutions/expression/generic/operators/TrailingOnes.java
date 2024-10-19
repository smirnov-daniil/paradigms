package expression.generic.operators;


import expression.generic.evaluators.Evaluator;

public class TrailingOnes<T> extends UnaryOperator<T> {
    public TrailingOnes(AnyExpression<T> operand) {
        super(operand);
    }

    @Override
    public T eval(T a, final Evaluator<T> evaluator) {
        return evaluator.t1(a);
    }

    @Override
    public String getOperator() {
        return "t1";
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
