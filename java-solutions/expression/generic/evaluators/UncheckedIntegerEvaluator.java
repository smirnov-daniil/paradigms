package expression.generic.evaluators;

import expression.exceptions.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class UncheckedIntegerEvaluator implements Evaluator<Integer> {
    @Override
    public Integer min(Integer a, Integer b) {
        return Math.min(a, b);
    }

    @Override
    public Integer max(Integer a, Integer b) {
        return Math.max(a, b);
    }

    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer sub(Integer a, Integer b) {
        return a - b;
    }

    @Override
    public Integer mul(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public Integer div(Integer a, Integer b) {
        if (b == 0) {
            throw divByZero(a);
        }
        return a / b;
    }

    @Override
    public Integer and(Integer a, Integer b) {
        return a & b;
    }

    @Override
    public Integer or(Integer a, Integer b) {
        return a | b;
    }

    @Override
    public Integer xor(Integer a, Integer b) {
        return a ^ b;
    }

    @Override
    public Integer neg(Integer a) {
        return -a;
    }

    @Override
    public Integer count(Integer a) {
        return Integer.bitCount(a);
    }

    @Override
    public Integer l1(Integer a) {
        return Integer.numberOfLeadingZeros(~a);
    }

    @Override
    public Integer t1(Integer a) {
        return Integer.numberOfTrailingZeros(a);
    }

    @Override
    public Integer parse(String a) {
        if (a.contains(".")) {
            return new BigDecimal(a).intValue();
        }
        try {
            return Integer.parseInt(a);
        } catch (NumberFormatException e) {
            return new BigInteger(a).intValue();
        }
    }

    @Override
    public Integer cast(int a) {
        return a;
    }
}
