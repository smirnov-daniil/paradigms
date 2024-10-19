package expression.parser.exceptions;

public class ParserEndOfFileException extends ParserException {
    public ParserEndOfFileException(final String expression, Throwable cause) {
        super("Unexpected end-of-file", expression, cause);
    }
    public ParserEndOfFileException(final String expression) {
        super("Unexpected end-of-file", expression);
    }
}
