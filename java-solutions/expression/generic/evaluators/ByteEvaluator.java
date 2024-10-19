package expression.generic.evaluators;

public class ByteEvaluator implements Evaluator<Byte> {
    @Override
    public Byte min(Byte a, Byte b) {
        return (byte) Math.min(a, b);
    }

    @Override
    public Byte max(Byte a, Byte b) {
        return (byte) Math.max(a, b);
    }

    @Override
    public Byte add(Byte a, Byte b) {
        return (byte) (a + b);
    }

    @Override
    public Byte sub(Byte a, Byte b) {
        return (byte) (a - b);
    }

    @Override
    public Byte mul(Byte a, Byte b) {
        return (byte) (a * b);
    }

    @Override
    public Byte div(Byte a, Byte b) {
        if (b == 0) {
            throw divByZero(a);
        }
        return (byte) (a / b);
    }

    @Override
    public Byte and(Byte a, Byte b) {
        return (byte) (a & b);
    }

    @Override
    public Byte or(Byte a, Byte b) {
        return (byte) (a | b);
    }

    @Override
    public Byte xor(Byte a, Byte b) {
        return (byte) (a ^ b);
    }

    @Override
    public Byte neg(Byte a) {
        return (byte) -a;
    }

    @Override
    public Byte count(Byte a) {
        return (byte) Integer.bitCount(Byte.toUnsignedInt(a));
    }

    @Override
    public Byte l1(Byte a) {
        return (byte) Integer.numberOfLeadingZeros(~a);
    }

    @Override
    public Byte t1(Byte a) {
        return (byte) Integer.numberOfTrailingZeros(~a);
    }

    @Override
    public Byte parse(String a) {
        return Byte.parseByte(a);
    }

    @Override
    public Byte cast(int a) {
        return (byte) a;
    }
}
