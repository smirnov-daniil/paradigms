package expression.generic;

import expression.exceptions.ExpressionException;
import expression.generic.evaluators.BigIntegerEvaluator;
import expression.generic.evaluators.DoubleEvaluator;
import expression.generic.evaluators.IntegerEvaluator;
import expression.generic.operators.AnyExpression;
import expression.generic.parser.Operators;
import expression.generic.parser.RecursiveParser;
import expression.parser.exceptions.*;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        try {
            AnyExpression<BigInteger> parsedExpression =
                    new RecursiveParser<BigInteger>("10000000000000000 + x", new Operators<>() {}).parse();
            System.out.println(parsedExpression.evaluate(
                    new BigInteger("50000000"), BigInteger.ZERO, BigInteger.TEN, new BigIntegerEvaluator()));
        } catch (ParserException | ExpressionException e) {
            System.err.println(e.getMessage());
        }
        try {
            AnyExpression<Double> parsedExpression =
                    new RecursiveParser<Double>("1.5 + x", new Operators<>() {}).parse();
            System.out.println(parsedExpression.evaluate(1.2, 1.1, 0.0, new DoubleEvaluator()));
        } catch (ParserException | ExpressionException e) {
            System.err.println(e.getMessage());
        }

        System.out.println(new IntegerEvaluator().parse("46.9"));

    }
}
