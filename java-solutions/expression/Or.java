package expression;

import java.math.BigInteger;

public class Or extends BinaryOperator {
    public Or(AnyExpression firstOperand, AnyExpression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    protected int eval(int a, int b) {
        return a | b;
    }

    /*@Override
    protected BigInteger eval(BigInteger a, BigInteger b) {
        return a.or(b);
    }*/

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public String getOperator() {
        return "|";
    }
}
