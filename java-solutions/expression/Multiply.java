package expression;

import java.math.BigInteger;

public class Multiply extends BinaryOperator {
    public Multiply(AnyExpression firstOperand, AnyExpression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public int eval(int a, int b) {
        return a * b;
    }

    /*@Override
    public BigInteger eval(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }*/

    @Override
    public String getOperator() {
        return "*";
    }

    @Override
    public int getPriority() {
        return 10;
    }
}
