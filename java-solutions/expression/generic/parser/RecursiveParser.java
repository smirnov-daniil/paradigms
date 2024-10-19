package expression.generic.parser;

import expression.generic.evaluators.Evaluator;
import expression.generic.operators.AnyExpression;
import expression.generic.operators.Const;
import expression.generic.operators.Variable;
import expression.parser.BaseParser;
import expression.parser.exceptions.ParserEndOfFileException;
import expression.parser.exceptions.ParserInvalidConstantException;
import expression.parser.exceptions.ParserInvalidOperatorException;
import expression.parser.exceptions.ParserTokenException;

import java.util.List;
import java.util.Objects;

/**
 * @author DS2
 */
public class RecursiveParser<T> extends BaseParser {
    private final List<Operators.OperatorsPriorityLevel> OPERATORS_PRIORITY_TABLE;
    private final Operators<T> operators;

    private final List<String> variables;
    private final boolean isList;

    public RecursiveParser(String expression, Operators<T> operators) {
        super(expression);
        this.operators = operators;
        this.variables = List.of("x", "y", "z");
        this.isList = false;
        OPERATORS_PRIORITY_TABLE = operators.getOperatorsPriorityTable();
    }

    public RecursiveParser(String expression, Operators<T> operators, List<String> variables) {
        super(expression);
        this.operators = operators;
        this.variables = variables;
        this.isList = true;
        OPERATORS_PRIORITY_TABLE = operators.getOperatorsPriorityTable();
    }

    public AnyExpression<T> parse()
            throws ParserTokenException, ParserInvalidOperatorException,
            ParserInvalidConstantException, ParserEndOfFileException {
        AnyExpression<T> expression = parseExpression(0);
        skipWhitespace();
        try {
            expectEOF();
        } catch (IllegalArgumentException e) {
            throw new ParserTokenException(toString(), e);
        }
        return expression;
    }

    private AnyExpression<T> parseExpression(int priority)
            throws ParserTokenException, ParserInvalidOperatorException,
            ParserInvalidConstantException, ParserEndOfFileException {
        return switch (OPERATORS_PRIORITY_TABLE.get(priority).power()) {
            case VOID -> parseScopedExpression();
            case UNARY -> parseUnaryExpression(priority);
            case BINARY -> parseBinaryExpression(priority);
        };
    }

    private AnyExpression<T> parseScopedExpression()
            throws ParserTokenException, ParserInvalidOperatorException,
            ParserInvalidConstantException, ParserEndOfFileException {
        String scope;
        if (eof())
            throw new ParserEndOfFileException(toString());
        try {
            scope = expectAny(List.of("(", "[", "{"));
        } catch (IllegalArgumentException e) {
            throw new ParserTokenException(toString(), e);
        }
        AnyExpression<T> expression = parseExpression(0);
        if (eof())
            throw new ParserEndOfFileException(toString());
        try {
            expect(switch (scope) {
                case "(" -> ")";
                case "[" -> "]";
                case "{" -> "}";
                default -> "";
            });
        } catch (IllegalArgumentException e) {
            throw new ParserTokenException(toString(), e);
        }

        return expression;
    }

    private boolean isSpacingRequired(String operator, Operators.Power type) {
        return switch (type) {
            case BINARY -> Objects.equals(operator, "min") || Objects.equals(operator, "max");
            case UNARY -> !Objects.equals(operator, "-");
            case VOID -> false;
        };
    }

    private AnyExpression<T> parseUnaryExpression(int priority)
            throws ParserTokenException, ParserInvalidOperatorException,
            ParserInvalidConstantException, ParserEndOfFileException {
        skipWhitespace();
        if (testConst() || testVariable()) {
            return parsePrimaryExpression();
        }
        for (String operator : OPERATORS_PRIORITY_TABLE.get(priority).operators()) {
            if (take(operator)) {
                if (isSpacingRequired(operator, Operators.Power.UNARY)) {
                    try {
                        expectSpacing();
                    } catch (IllegalArgumentException e) {
                        throw new ParserTokenException(toString(), e);
                    }
                }
                return operators.construct(operator, parseExpression(priority));
            }
        }
        return parseExpression(priority + 1);
    }

    private AnyExpression<T> parseBinaryExpression(int priority)
            throws ParserTokenException, ParserInvalidOperatorException,
            ParserInvalidConstantException, ParserEndOfFileException {
        skipWhitespace();
        AnyExpression<T> expression = parseExpression(priority + 1);
        eof: while (!eof()) {
            skipWhitespace();
            for (String operator : OPERATORS_PRIORITY_TABLE.get(priority).operators()) {
                if (take(operator)) {
                    if (isSpacingRequired(operator, Operators.Power.BINARY)) {
                        try {
                            expectSpacing();
                        } catch (IllegalArgumentException e) {
                            throw new ParserTokenException(toString(), e);
                        }
                    }
                    expression = operators.construct(operator, expression, parseExpression(priority + 1));
                    continue eof;
                }
            }
            break;
        }
        return expression;
    }

    private AnyExpression<T> parsePrimaryExpression() throws ParserTokenException, ParserInvalidConstantException {
        skipWhitespace();
        return testVariable() ? parseVariable() : parseConst();
    }

    private AnyExpression<T> parseVariable() throws ParserTokenException {
        try {
            String variable = expectAny(variables);
            if (isList) {
                return new Variable<>(variable, variables.indexOf(variable));
            }
            return new Variable<>(variable);
        } catch (IllegalArgumentException e) {
            throw new ParserTokenException(toString(), e);
        }
    }

    private boolean testConst() {
        return testNumber();
    }

    private AnyExpression<T> parseConst() throws ParserInvalidConstantException {
        final StringBuilder numberBuilder = new StringBuilder();
        boolean wasDot = false;
        if (take('-')) {
            numberBuilder.append('-');
        }
        if (take('0')) {
            numberBuilder.append('0');
        } else if (between('1', '9') || !wasDot && (wasDot = test('.'))) {
            while (between('0', '9') || !wasDot && (wasDot = test('.'))) {
                numberBuilder.append(take());
            }
        }
        if (numberBuilder.isEmpty()) {
            throw new ParserInvalidConstantException("", toString());
        }
        return new Const<>(numberBuilder.toString());
    }

    private boolean testVariable() {
        boolean result = false;
        for (String var : variables) {
            result = result || test(var);
        }
        return result;
    }
}
