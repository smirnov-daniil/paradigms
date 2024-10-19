package expression.generic;

import expression.exceptions.ExpressionException;
import expression.generic.evaluators.*;
import expression.generic.operators.AnyExpression;
import expression.generic.parser.Operators;
import expression.generic.parser.RecursiveParser;
import expression.parser.exceptions.ParserException;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private final static Map<String, Evaluator<?>> evaluators = Map.of(
            "bool", new BooleanEvaluator(),
            "b",    new ByteEvaluator(),
            "i",    new IntegerEvaluator(),
            "u",    new UncheckedIntegerEvaluator(),
            "d",    new DoubleEvaluator(),
            "bi",   new BigIntegerEvaluator()

    );

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2)
            throws Exception {
        assert x1 <= x2 && y1 <= y2 && z1 <= z2;
        return fillTable(evaluators.get(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private <T> Object[][][] fillTable(Evaluator<T> evaluator, String expression,
                                       int x1, int x2, int y1, int y2, int z1, int z2) throws ParserException {
        AnyExpression<T> parsedExpression = new RecursiveParser<T>(expression, new Operators<>() {}).parse();

        x2 -= x1;
        y2 -= y1;
        z2 -= z1;
        Object[][][] result = new Object[x2 + 1][y2 + 1][z2 + 1];
        for (int i = 0; i <= x2; i++) {
            for (int j = 0; j <= y2; j++) {
                for (int k = 0; k <= z2; k++) {
                    try {
                        result[i][j][k] = parsedExpression.evaluate(
                                evaluator.cast(x1 + i),
                                evaluator.cast(y1 + j),
                                evaluator.cast(z1 + k),
                                evaluator
                        );
                    } catch (ExpressionException e) {
                        result[i][j][k] = null;
                    }
                }
            }
        }
        return result;
    }
}
