package expression;

import java.math.BigInteger;

public class Divide extends BinaryOperator {
    public Divide(AnyExpression firstOperand, AnyExpression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public int eval(int a, int b) {
        return a / b;
    }

    /*@Override
    public BigInteger eval(BigInteger a, BigInteger b) {
        return a.divide(b);
    }*/

    @Override
    public String getOperator() {
        return "/";
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public boolean getIsNotAssociative() {
        return true;
    }

    @Override
    public boolean getIsIntegerDependent() {
        return true;
    }
}
