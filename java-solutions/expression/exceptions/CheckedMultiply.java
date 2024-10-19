package expression.exceptions;

public class CheckedMultiply extends expression.Multiply {
    public CheckedMultiply(expression.AnyExpression firstOperand, expression.AnyExpression secondOperand) {
        super(firstOperand, secondOperand);
    }

    public static boolean checkOverflow(int a, int b) {
        return a > 0 && b > 0 && Integer.MAX_VALUE / a < b ||
               a > 0 && b < 0 && Integer.MIN_VALUE / a > b ||
               a < 0 && b > 0 && Integer.MIN_VALUE / b > a ||
               a < 0 && b < 0 && Integer.MAX_VALUE / a > b;
    }

    @Override
    public int eval(int a, int b) {
        if (checkOverflow(a, b)) {
            throw new expression.exceptions.ExpressionOverflowException(this.toString());
        }
        return super.eval(a, b);
    }
}
