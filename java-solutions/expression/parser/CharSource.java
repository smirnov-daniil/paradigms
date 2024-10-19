package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 * @co-author DS2
 */
public interface CharSource {
    boolean hasNext();
    char next();
    void setAnchor();
    void resetToAnchor();
    IllegalArgumentException error(String message);
}
