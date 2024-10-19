package expression.parser.exceptions;

public class ParserException extends Exception {
    private static String combineCause(final String message, final String expression, Throwable cause) {
        StringBuilder sb = new StringBuilder(message + " got while parsing \"" + expression + "\".");
        Throwable recCause = cause;
        while (recCause != null) {
            sb.append("Caused by: ").append(cause.getMessage()).append('\n');
            recCause = recCause.getCause();
        }
        return sb.toString();
    }
    public ParserException(final String message, final String expression, Throwable cause) {
        super(combineCause(message, expression, cause), cause);
    }
    public ParserException(final String message, final String expression) {
        super(message + " got while parsing \"" + expression + "\".");
    }
}
