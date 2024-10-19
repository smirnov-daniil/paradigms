package queue;

import java.util.Objects;
import java.util.function.Predicate;

// Model: a[1..n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(n, i, j): forall l=0..n-1: a'[i+l] = a[j+l]
public class ArrayQueueModule {
    private static int head = 0, tail = 0;
    private static Object[] elements = new Object[3];


    // Pre : element != null
    // Post: n' = n + 1 &&
    //       a'[n'] = element &&
    //       immutable(n, 1, 1)
    public static void enqueue(Object element) {
        Objects.requireNonNull(element);

        tail = CircularUtils.inc(tail, elements.length);
        elements[tail] = element;
        extendCapacity();
    }

    // Pre : element != null
    // Post: n' = n + 1 &&
    //       a'[1] = element &&
    //       immutable(n, 2, 1)
    public static void push(Object element) {
        Objects.requireNonNull(element);

        elements[head] = element;
        head = CircularUtils.dec(head, elements.length);
        extendCapacity();
    }

    // Pre : true
    // Post: n' = n &&
    //       immutable(n, 1, 1)
    private static int newCapacity() {
        return 1 + (elements.length - 1) * 2;
    }

    // Pre : true
    // Post: n' = n &&
    //       immutable(n, 1, 1)
    private static void extendCapacity() {
        if (head == tail) {
            int oldCapacity = elements.length;
            elements = CircularUtils.copyOf(elements, head, tail, newCapacity());
            head = 0;
            tail = oldCapacity;
        }
    }

    // Pre : n > 0
    // Post: R = a[1] &&
    //       n' = n - 1 &&
    //       immutable(n', 1, 2)
    public static Object dequeue() {
        assert head != tail;

        head = CircularUtils.inc(head, elements.length);
        Object value = elements[head];
        elements[head] = null;
        return value;
    }

    // Pre : n > 0
    // Post: R = a[n - 1] &&
    //       n' = n - 1 &&
    //       immutable(n', 1, 1)
    public static Object remove() {
        assert head != tail;

        Object value = elements[tail];
        elements[tail] = null;
        tail = CircularUtils.dec(tail, elements.length);
        return value;
    }

    // Pre : n > 0
    // Post: R = a[n - 1] &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    public static Object peek() {
        assert head != tail;

        return elements[tail];
    }

    // Pre : n > 0
    // Post: R = a[1] &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    public static Object element() {
        assert head != tail;

        return elements[CircularUtils.inc(head, elements.length)];
    }

    // Pre : true
    // Post: R = n &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    public static int size() {
        return CircularUtils.size(head, tail, elements.length);
    }

    // Pre : predicate != null
    // Post: R = count all x in a for which the predicate(x) == true &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    public static int countIf(final Predicate<Object> predicate) {
        Objects.requireNonNull(predicate);

        int count = 0,
                i = CircularUtils.inc(head, elements.length),
                j = CircularUtils.inc(tail, elements.length);
        while (i != j) {
            if (predicate.test(elements[i])) {
                count++;
            }
            i = CircularUtils.inc(i, elements.length);
        }
        return count;
    }

    // Pre : true
    // Post: R = (n == 0) &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    public static boolean isEmpty() {
        return head == tail;
    }

    // Pre : true
    // Post: n' = 0
    public static void clear() {
        CircularUtils.fill(elements, head, tail, null);
        head = tail = 0;
    }
}
