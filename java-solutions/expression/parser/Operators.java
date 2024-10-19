package expression.parser;

import expression.*;

import expression.parser.exceptions.ParserInvalidOperatorException;

import java.util.List;
import java.util.Set;

public interface Operators {
    enum Power {
        BINARY, UNARY, VOID
    }
    record OperatorsPriorityLevel(Power power, Set<String> operators) {}

    default List<OperatorsPriorityLevel> getOperatorsPriorityTable() {
        return List.of(
                new OperatorsPriorityLevel(Power.BINARY, Set.of("min", "max")),
                new OperatorsPriorityLevel(Power.BINARY, Set.of("|")),
                new OperatorsPriorityLevel(Power.BINARY, Set.of("^")),
                new OperatorsPriorityLevel(Power.BINARY, Set.of("&")),
                new OperatorsPriorityLevel(Power.BINARY, Set.of("+", "-")),
                new OperatorsPriorityLevel(Power.BINARY, Set.of("*", "/")),
                new OperatorsPriorityLevel(Power.UNARY,  Set.of("-", "l1", "t1")),
                new OperatorsPriorityLevel(Power.VOID,   Set.of("(", ")"))
        );
    }

    default AnyExpression construct(String operator,
                                    AnyExpression firstOperand,
                                    AnyExpression secondOperand)
            throws ParserInvalidOperatorException {
        return switch (operator) {
            case "|" -> new Or(firstOperand, secondOperand);
            case "^" -> new Xor(firstOperand, secondOperand);
            case "&" -> new And(firstOperand, secondOperand);
            case "+" -> new Add(firstOperand, secondOperand);
            case "-" -> new Subtract(firstOperand, secondOperand);
            case "*" -> new Multiply(firstOperand, secondOperand);
            case "/" -> new Divide(firstOperand, secondOperand);
            case "min" -> new Min(firstOperand, secondOperand);
            case "max" -> new Max(firstOperand, secondOperand);
            default -> throw new ParserInvalidOperatorException(operator, toString());
        };
    }

    default AnyExpression construct(String operator, AnyExpression expression)
            throws ParserInvalidOperatorException {
        return switch (operator) {
            case "-" -> new Negate(expression);
            case "l1" -> new LeadingOnes(expression);
            case "t1" -> new TrailingOnes(expression);
            default -> throw new ParserInvalidOperatorException(operator, toString());
        };
    }
}
