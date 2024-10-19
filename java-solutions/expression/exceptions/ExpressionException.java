package expression.exceptions;

public class ExpressionException extends ArithmeticException {
    public ExpressionException(final String message, final String expression) {
        super(message + " got while evaluating f = " + expression + ".");
    }
}
