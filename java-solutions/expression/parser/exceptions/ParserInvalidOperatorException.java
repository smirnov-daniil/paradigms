package expression.parser.exceptions;

public class ParserInvalidOperatorException extends ParserException {
    public ParserInvalidOperatorException(String operator, String expression) {
        super("Invalid operator \"" + operator + "\"", expression);
    }
}
