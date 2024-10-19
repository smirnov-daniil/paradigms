package expression;

public interface Operator {
    enum Type {
        PREFIX, SUFFIX, INFIX
    }

    int getPriority();
    default boolean getIsNotAssociative() {
        return false;
    }
    default boolean getIsIntegerDependent() {
        return false;
    }

    default int getSpecial() {
        return 0;
    }

    default Type getType() {
        return Type.INFIX;
    }

    default String getOperator() {
        return null;
    }
}
