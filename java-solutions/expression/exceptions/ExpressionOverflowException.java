package expression.exceptions;

public class ExpressionOverflowException extends ExpressionException {
    public ExpressionOverflowException(String expression) {
        super("Overflow", expression);
    }
}
