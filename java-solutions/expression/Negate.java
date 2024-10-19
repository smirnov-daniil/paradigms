package expression;

import java.math.BigInteger;

public class Negate extends UnaryOperator {
    public Negate(AnyExpression operand) {
        super(operand);
    }

    @Override
    public int eval(int a) {
        return -a;
    }

    /*@Override
    public BigInteger eval(BigInteger a) {
        return a.negate();
    }*/

    @Override
    public String getOperator() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 15;
    }

    @Override
    public Type getType() {
        return Type.PREFIX;
    }
}
