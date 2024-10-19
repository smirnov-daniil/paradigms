package expression.exceptions;

public class CheckedAdd extends expression.Add {
    public CheckedAdd(expression.AnyExpression firstOperand, expression.AnyExpression secondOperand) {
        super(firstOperand, secondOperand);
    }

    public static boolean checkOverflow(int a, int b) {
        return b > 0 && a > Integer.MAX_VALUE - b || b < 0 && a < Integer.MIN_VALUE - b;
    }

    @Override
    public int eval(int a, int b) {
        if (checkOverflow(a, b)) {
            throw new expression.exceptions.ExpressionOverflowException(this.toString());
        }

        return super.eval(a, b);
    }
}
