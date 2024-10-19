package expression;

import java.math.BigInteger;

public class Xor extends BinaryOperator {
    public Xor(AnyExpression firstOperand, AnyExpression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    protected int eval(int a, int b) {
        return a ^ b;
    }

    /*@Override
    protected BigInteger eval(BigInteger a, BigInteger b) {
        return a.xor(b);
    }*/

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public String getOperator() {
        return "^";
    }
}
