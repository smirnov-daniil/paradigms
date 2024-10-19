package expression;

import java.math.BigInteger;

public class Subtract extends BinaryOperator {
    public Subtract(AnyExpression firstOperand, AnyExpression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public int eval(int a, int b) {
        return a - b;
    }

    /*@Override
    public BigInteger eval(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }*/

    @Override
    public String getOperator() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public boolean getIsNotAssociative() {
        return true;
    }
}
