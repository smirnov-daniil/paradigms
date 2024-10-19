package expression;

import java.math.BigInteger;

public class LeadingOnes extends UnaryOperator {
    public LeadingOnes(AnyExpression operand) {
        super(operand);
    }

    @Override
    public int eval(int a) {
        return Integer.numberOfLeadingZeros(~a);
    }

    /*@Override
    protected BigInteger eval(BigInteger a) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("LeadingOnes is not supported for BigInteger");
    }*/

    @Override
    public String getOperator() {
        return "l1";
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
