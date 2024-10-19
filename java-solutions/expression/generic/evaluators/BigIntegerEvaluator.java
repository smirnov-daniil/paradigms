package expression.generic.evaluators;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class BigIntegerEvaluator implements Evaluator<BigInteger> {
    @Override
    public BigInteger min(BigInteger a, BigInteger b) {
        return a.min(b);
    }

    @Override
    public BigInteger max(BigInteger a, BigInteger b) {
        return a.max(b);
    }

    @Override
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger sub(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    @Override
    public BigInteger mul(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    @Override
    public BigInteger div(BigInteger a, BigInteger b) {
        if (Objects.equals(b, BigInteger.ZERO)) {
            throw divByZero(a);
        }
        return a.divide(b);
    }

    @Override
    public BigInteger and(BigInteger a, BigInteger b) {
        return a.and(b);
    }

    @Override
    public BigInteger or(BigInteger a, BigInteger b) {
        return a.or(b);
    }

    @Override
    public BigInteger xor(BigInteger a, BigInteger b) {
        return a.xor(b);
    }

    @Override
    public BigInteger neg(BigInteger a) {
        return a.negate();
    }

    @Override
    public BigInteger count(BigInteger a) {
        return BigInteger.valueOf(a.bitCount());
    }

    @Override
    public BigInteger parse(String a) {
        if (a.contains(".")) {
            return BigDecimal.valueOf(Double.parseDouble(a)).toBigInteger();
        }
        return new BigInteger(a);
    }

    @Override
    public BigInteger cast(int a) {
        return BigInteger.valueOf(a);
    }
}
