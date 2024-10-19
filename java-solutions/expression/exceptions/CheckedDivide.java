package expression.exceptions;

import java.math.BigInteger;

public class CheckedDivide extends expression.Divide {
    public CheckedDivide(expression.AnyExpression firstOperand, expression.AnyExpression secondOperand) {
        super(firstOperand, secondOperand);
    }

    public static boolean checkOverflow(int a, int b) {
        return a == Integer.MIN_VALUE && b == -1;
    }

    @Override
    public int eval(int a, int b) {
        if (b == 0) {
            throw new expression.exceptions.ExpressionDivisionByZeroException(this.toString());
        }

        if (checkOverflow(a, b)) {
            throw new expression.exceptions.ExpressionOverflowException(this.toString());
        }

        return super.eval(a, b);
    }

    /*@Override
    public BigInteger eval(BigInteger a, BigInteger b) {
        if (b.equals(java.math.BigInteger.ZERO)) {
            throw new expression.exceptions.ExpressionDivisionByZeroException(this);
        }

        return super.eval(a, b);
    }*/
}
