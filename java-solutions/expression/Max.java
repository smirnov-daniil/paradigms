package expression;

import java.math.BigInteger;

public class Max extends BinaryOperator {
    public Max(AnyExpression firstOperand, AnyExpression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public int eval(int a, int b) {
        return a > b ? a : b;
    }

    /*@Override
    public BigInteger eval(BigInteger a, BigInteger b) {
        return a.max(b);
    }*/

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
