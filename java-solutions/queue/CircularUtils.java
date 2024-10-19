package queue;

import java.util.Arrays;

class CircularUtils {
    public static Object[] copyOf(Object[] original, int head, int tail, int newLength) {
        Object[] copy = new Object[newLength--];

        head = inc(head, original.length);
        tail = inc(tail, original.length);

        if (tail > head) {
            System.arraycopy(original, head, copy, 1, Math.min(newLength, tail - head));
        }
        else {
            System.arraycopy(original, head, copy, 1, Math.min(newLength, original.length - head));
            if (newLength > original.length - head) {
                System.arraycopy(original, 0, copy, original.length - head + 1,
                        Math.min(newLength - 1 - original.length + head, tail));
            }
        }
        return copy;
    }

    public static void fill(Object[] array, int head, int tail, Object value) {
        head = inc(head, array.length);
        tail = inc(tail, array.length);

        if (tail < head) {
            Arrays.fill(array, head, array.length, value);
            Arrays.fill(array, 0, tail, value);
        } else {
            Arrays.fill(array, head, tail, value);
        }
    }

    public static int inc(int i, int mod) {
        return (i + 1) % mod;
    }

    public static int inc(int i, int distance, int mod) {
        return (i + distance) % mod;
    }

    public static int dec(int i, int mod) {
        return (--i + mod) % mod;
    }

    public static int size(int head, int tail, int mod) {
        return (tail - head + mod) % mod;
    }
}
