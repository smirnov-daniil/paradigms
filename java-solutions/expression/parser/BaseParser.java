package expression.parser;

import java.util.Arrays;
import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 * @co-author DS2
 */
public class BaseParser {
    private static final char END = '\0';
    private final CharSource source;
    private char ch = 0xffff;

    protected BaseParser(final String expression) {
        this(new StringSource(expression));
    }

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean test(final String expected) {
        char oldChar = ch;
        source.setAnchor();
        final boolean result = rawTest(expected);
        ch = oldChar;
        source.resetToAnchor();
        return result;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    private boolean rawTest(final String expected) {
        for (final char e : expected.toCharArray()) {
            if (test(e)) {
                take();
            } else {
                return false;
            }
        }
        return true;
    }

    protected boolean take(final String expected) {
        char oldChar = ch;
        source.setAnchor();
        final boolean result = rawTest(expected);
        if (!result) {
            ch = oldChar;
            source.resetToAnchor();
        }
        return result;
    }

    protected boolean testNumber() {
        char oldChar = ch;
        source.setAnchor();
        take('-');
        boolean result = between('0', '9');
        ch = oldChar;
        source.resetToAnchor();
        return result;
    }

    protected boolean testWhitespace() {
        return Character.isWhitespace(ch);
    }

    protected boolean takeWhitespace() {
        if (testWhitespace()) {
            take();
            return true;
        }
        return false;
    }

    /*protected void expectSpacing() {
        if (!testWhitespace() && !test('(') && !test('-')) {
            takeWhitespace();
            throw error("Expected 'spacing', found '" + (ch == END ? "end-of-file" : ch) + "'");
        }
    }*/

    protected void expectSpacing() {
        if (Character.isDigit(ch)) {
            throw error("Expected 'spacing', found '" + (ch == END ? "end-of-file" : ch) + "'");
        }
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + (ch == END ? "end-of-file" : ch) + "'");
        }
    }

    protected void skipWhitespace() {
        while (takeWhitespace()) {}
    }

    protected void expect(final String expected) {
        char[] chars = expected.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!take(chars[i])) {
                throw error("Expected '" + expected + "', found '" +
                        Arrays.toString(Arrays.copyOfRange(chars, 0, i - 1)) + ch + "'");
            }
        }
    }

    protected String expectAny(final List<String> values) {
        for (final String value : values) {
            if (take(value)) {
                return value;
            }
        }
        char found = take();
        throw error("Expected '" + values + "', found '" + (found == END ? "end-of-file" : found) + "'");
    }

    protected boolean eof() {
        return take(END);
    }

    protected void expectEOF() {
        if (!eof()) {
            throw error("Expected end of expression, found '" + take() + "'");
        }
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    @Override
    public String toString() {
        return source.toString();
    }
}
