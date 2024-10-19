package expression.exceptions;

import expression.AnyExpression;

public class CheckedNegate extends expression.Negate {
    public CheckedNegate(AnyExpression operand) {
        super(operand);
    }

    public static boolean checkOverflow(int a) {
        return a == Integer.MIN_VALUE;
    }

    @Override
    public int eval(int a) {
        if (checkOverflow(a)) {
            throw new expression.exceptions.ExpressionOverflowException(this.toString());
        }
        return super.eval(a);
    }
}
