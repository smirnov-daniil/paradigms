package expression.exceptions;

import expression.AnyExpression;

public class ExpressionDivisionByZeroException extends ExpressionException {
    public ExpressionDivisionByZeroException(String expression) {
        super("Division by zero", expression);
    }
}
