package expression.generic.operators;

import expression.generic.evaluators.Evaluator;

public class LeadingOnes<T> extends UnaryOperator<T> {
    public LeadingOnes(AnyExpression<T> operand) {
        super(operand);
    }

    @Override
    public T eval(T a, final Evaluator<T> evaluator) {
        return evaluator.l1(a);
    }

    @Override
    public String getOperator() {
        return "l1";
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
