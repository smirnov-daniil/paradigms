package expression;

import java.math.BigInteger;

public class TrailingOnes extends UnaryOperator {
    public TrailingOnes(AnyExpression operand) {
        super(operand);
    }

    @Override
    public int eval(int a) {
        return Integer.numberOfTrailingZeros(~a);
    }

    /*@Override
    protected BigInteger eval(BigInteger a) {
        throw new UnsupportedOperationException("TrailingOnes is not supported for BigInteger");
    }*/

    @Override
    public String getOperator() {
        return "t1";
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
