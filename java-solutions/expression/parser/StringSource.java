package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 * @co-author DS2
 */
public class StringSource implements CharSource {
    private final String data;
    private int pos;
    private int anchor;

    public StringSource(final String data) {
        this(data, 0);
    }

    public StringSource(final String data, int anchor) {
        this.data = data;
        this.anchor = anchor;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public void setAnchor() {
        anchor = pos;
    }

    @Override
    public void resetToAnchor() {
        pos = anchor;
    }

    public String toString() {
        return data;
    }

    @Override
    public IllegalArgumentException error(final String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }
}
