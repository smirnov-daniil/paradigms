package expression.parser.exceptions;

public class ParserInvalidConstantException extends ParserException {
    public ParserInvalidConstantException(String constant, String expression, Throwable cause) {
        super("Invalid constant (overflow) " + constant, expression, cause);
    }
    public ParserInvalidConstantException(String constant, String expression) {
        super("Invalid constant (overflow) " + constant, expression);
    }
}
