package expression.generic.evaluators;

public interface Evaluator<T> {
    T min(T a, T b);
    T max(T a, T b);
    T add(T a, T b);
    T sub(T a, T b);
    T mul(T a, T b);
    T div(T a, T b);
    default T and(T a, T b) {
        throw new UnsupportedOperationException("and");
    }
    default T or(T a, T b) {
        throw new UnsupportedOperationException("or");
    }
    default T xor(T a, T b) {
        throw new UnsupportedOperationException("xor");
    }
    T neg(T a);
    T count(T a);
    default T l1(T a) {
        throw new UnsupportedOperationException("l1");
    }
    default T t1(T a) {
        throw new UnsupportedOperationException("t1");
    }
    T parse(String a);

    T cast(int a);

    default expression.exceptions.ExpressionDivisionByZeroException divByZero(T a) {
        return new expression.exceptions.ExpressionDivisionByZeroException(a + " / 0");
    }
}
