package expression.generic.evaluators;

import java.math.BigDecimal;

public class BooleanEvaluator implements Evaluator<Boolean> {

    @Override
    public Boolean min(Boolean a, Boolean b) {
        return a ? b : a;
    }

    @Override
    public Boolean max(Boolean a, Boolean b) {
        return a ? a : b;
    }

    @Override
    public Boolean add(Boolean a, Boolean b) {
        return or(a, b);
    }

    @Override
    public Boolean sub(Boolean a, Boolean b) {
        return xor(a, b);
    }

    @Override
    public Boolean mul(Boolean a, Boolean b) {
        return and(a, b);
    }

    @Override
    public Boolean div(Boolean a, Boolean b) {
        if (!b) {
            throw divByZero(a);
        }
        return a;
    }

    @Override
    public Boolean and(Boolean a, Boolean b) {
        return a && b;
    }

    @Override
    public Boolean or(Boolean a, Boolean b) {
        return a || b;
    }

    @Override
    public Boolean xor(Boolean a, Boolean b) {
        return a ^ b;
    }

    @Override
    public Boolean neg(Boolean a) {
        return a;
    }

    @Override
    public Boolean count(Boolean a) {
        return a;
    }

    @Override
    public Boolean l1(Boolean a) {
        return a;
    }

    @Override
    public Boolean t1(Boolean a) {
        return a;
    }

    @Override
    public Boolean parse(String a) {
        return !new BigDecimal(a).equals(BigDecimal.ZERO);
    }

    @Override
    public Boolean cast(int a) {
        return a != 0;
    }
}
