package expression;

import java.math.BigInteger;

public class Add extends BinaryOperator {
    public Add(AnyExpression firstOperand, AnyExpression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public int eval(int a, int b) {
        return a + b;
    }

    /*@Override
    public BigInteger eval(BigInteger a, BigInteger b) {
        return a.add(b);
    }*/

    @Override
    public String getOperator() {
        return "+";
    }

    @Override
    public int getPriority() {
        return 5;
    }
}
