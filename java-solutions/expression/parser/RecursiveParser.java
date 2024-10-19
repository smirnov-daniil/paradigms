package expression.parser;

import expression.*;
import expression.parser.exceptions.*;

import java.util.List;
import java.util.Objects;

/**
 * @author DS2
 */
public class RecursiveParser extends BaseParser {
    private final List<Operators.OperatorsPriorityLevel> OPERATORS_PRIORITY_TABLE;
    private final Operators operators;

    private final List<String> variables;
    private final boolean isList;

    public RecursiveParser(String expression, Operators operators) {
        super(expression);
        this.operators = operators;
        this.variables = List.of("x", "y", "z");
        this.isList = false;
        OPERATORS_PRIORITY_TABLE = operators.getOperatorsPriorityTable();
    }

    public RecursiveParser(String expression, Operators operators, List<String> variables) {
        super(expression);
        this.operators = operators;
        this.variables = variables;
        this.isList = true;
        OPERATORS_PRIORITY_TABLE = operators.getOperatorsPriorityTable();
    }

    public AnyExpression parse()
            throws ParserTokenException, ParserInvalidOperatorException,
            ParserInvalidConstantException, ParserEndOfFileException {
        AnyExpression expression = parseExpression(0);
        skipWhitespace();
        try {
            expectEOF();
        } catch (IllegalArgumentException e) {
            throw new ParserTokenException(toString(), e);
        }
        return expression;
    }

    private AnyExpression parseExpression(int priority)
            throws ParserTokenException, ParserInvalidOperatorException,
            ParserInvalidConstantException, ParserEndOfFileException {
        return switch (OPERATORS_PRIORITY_TABLE.get(priority).power()) {
            case VOID -> parseScopedExpression();
            case UNARY -> parseUnaryExpression(priority);
            case BINARY -> parseBinaryExpression(priority);
        };
    }

    private AnyExpression parseScopedExpression()
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
        AnyExpression expression = parseExpression(0);
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

    private AnyExpression parseUnaryExpression(int priority)
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

    private AnyExpression parseBinaryExpression(int priority)
            throws ParserTokenException, ParserInvalidOperatorException,
            ParserInvalidConstantException, ParserEndOfFileException {
        skipWhitespace();
        AnyExpression expression = parseExpression(priority + 1);
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

    private AnyExpression parsePrimaryExpression() throws ParserTokenException, ParserInvalidConstantException {
        skipWhitespace();
        return testVariable() ? parseVariable() : parseConst();
    }

    private AnyExpression parseVariable() throws ParserTokenException {
        try {
            String variable = expectAny(variables);
            if (isList) {
                return new Variable(variable, variables.indexOf(variable));
            }
            return new Variable(variable);
        } catch (IllegalArgumentException e) {
            throw new ParserTokenException(toString(), e);
        }
    }

    private boolean testConst() {
        return testNumber();
    }

    private AnyExpression parseConst() throws ParserInvalidConstantException {
        final StringBuilder integerBuilder = new StringBuilder();
        if (take('-')) {
            integerBuilder.append('-');
        }
        if (take('0')) {
            integerBuilder.append('0');
        } else if (between('1', '9')) {
            while (between('0', '9')) {
                integerBuilder.append(take());
            }
        }
        try {
            return new Const(Integer.parseInt(integerBuilder.toString()));
        } catch (NumberFormatException e) {
            throw new ParserInvalidConstantException(integerBuilder.toString(), toString(), e);
        }
    }

    private boolean testVariable() {
        boolean result = false;
        for (String var : variables) {
            result = result || test(var);
        }
        return result;
    }
}
