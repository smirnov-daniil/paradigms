package expression.exceptions;

import expression.*;
import expression.parser.exceptions.*;

public class CheckedOperators implements expression.parser.Operators {
    @Override
    public AnyExpression construct(String operator,
                                   AnyExpression firstOperand,
                                   AnyExpression secondOperand)
            throws ParserInvalidOperatorException {
        return switch (operator) {
            case "|" -> new Or(firstOperand, secondOperand);
            case "^" -> new Xor(firstOperand, secondOperand);
            case "&" -> new And(firstOperand, secondOperand);
            case "+" -> new CheckedAdd(firstOperand, secondOperand);
            case "-" -> new CheckedSubtract(firstOperand, secondOperand);
            case "*" -> new CheckedMultiply(firstOperand, secondOperand);
            case "/" -> new CheckedDivide(firstOperand, secondOperand);
            case "min" -> new Min(firstOperand, secondOperand);
            case "max" -> new Max(firstOperand, secondOperand);
            default -> throw new ParserInvalidOperatorException(operator, toString());
        };
    }

    @Override
    public AnyExpression construct(String operator, AnyExpression expression)
            throws ParserInvalidOperatorException {
        return switch (operator) {
            case "-" -> new CheckedNegate(expression);
            case "l1" -> new LeadingOnes(expression);
            case "t1" -> new TrailingOnes(expression);
            default -> throw new ParserInvalidOperatorException(operator, toString());
        };
    }
}
