package expression.parser.exceptions;

public class ParserTokenException extends ParserException {
    public ParserTokenException(final String expression, Throwable cause) {
        super("Unexpected token", expression, cause);
    }
    public ParserTokenException(final String expression) {
        super("Unexpected token", expression);
    }
}
