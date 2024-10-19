package expression.generic.evaluators;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DoubleEvaluator implements Evaluator<Double> {
    @Override
    public Double min(Double a, Double b) {
        return Math.min(a, b);
    }

    @Override
    public Double max(Double a, Double b) {
        return Math.max(a, b);
    }

    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double sub(Double a, Double b) {
        return a - b;
    }

    @Override
    public Double mul(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double div(Double a, Double b) {
        return a / b;
    }

    @Override
    public Double neg(Double a) {
        return -a;
    }

    @Override
    public Double count(Double a) {
        return (double) Long.bitCount(Double.doubleToLongBits(a));
    }

    @Override
    public Double parse(String a) {
        try {
            return Double.parseDouble(a);
        } catch (NumberFormatException e) {
            return new BigDecimal(a).doubleValue();
        }
    }

    @Override
    public Double cast(int a) {
        return (double) a;
    }
}
