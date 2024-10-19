package expression;

import java.math.BigInteger;

public class And extends BinaryOperator {
    public And(AnyExpression firstOperand, AnyExpression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    protected int eval(int a, int b) {
        return a & b;
    }

    /*@Override
    protected BigInteger eval(BigInteger a, BigInteger b) {
        return a.and(b);
    }*/

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public String getOperator() {
        return "&";
    }
}
