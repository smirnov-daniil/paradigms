package queue;

import java.util.Objects;
import java.util.function.Predicate;

// Model: a[1..n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(n, i, j): forall l=0..n-1: a'[i+l] = a[j+l]
public class ArrayQueueADT {
    private int head = 0, tail = 0;
    private Object[] elements = new Object[3];

    // Pre : queue != null && element != null
    // Post: n' = n + 1 &&
    //       a'[n'] = element &&
    //       immutable(n, 1, 1)
    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(queue);

        queue.tail = CircularUtils.inc(queue.tail, queue.elements.length);
        queue.elements[queue.tail] = element;
        extendCapacity(queue);
    }

    // Pre : queue != null && element != null
    // Post: n' = n + 1 &&
    //       a'[1] = element &&
    //       immutable(n, 2, 1)
    public static void push(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(queue);
        Objects.requireNonNull(element);

        queue.elements[queue.head] = element;
        queue.head = CircularUtils.dec(queue.head, queue.elements.length);
        extendCapacity(queue);
    }

    // Pre : queue != null
    // Post: n' = n &&
    //       immutable(n, 1, 1)
    private static int newCapacity(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);

        return 1 + (queue.elements.length - 1) * 2;
    }

    // Pre : queue != null
    // Post: n' = n &&
    //       immutable(n, 1, 1)
    private static void extendCapacity(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);

        if (queue.head == queue.tail) {
            int oldCapacity = queue.elements.length;
            queue.elements = CircularUtils.copyOf(queue.elements, queue.head, queue.tail, newCapacity(queue));
            queue.head = 0;
            queue.tail = oldCapacity;
        }
    }

    // Pre : queue != null && n > 0
    // Post: R = a[1] &&
    //       n' = n - 1 &&
    //       immutable(n', 1, 2)
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.head != queue.tail;
        Objects.requireNonNull(queue);

        queue.head = CircularUtils.inc(queue.head, queue.elements.length);
        Object value = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        return value;
    }

    // Pre : queue != null && n > 0
    // Post: R = a[n - 1] &&
    //       n' = n - 1 &&
    //       immutable(n', 1, 1)
    public static Object remove(ArrayQueueADT queue) {
        assert queue.head != queue.tail;
        Objects.requireNonNull(queue);

        Object value = queue.elements[queue.tail];
        queue.elements[queue.tail] = null;
        queue.tail = CircularUtils.dec(queue.tail, queue.elements.length);
        return value;
    }

    // Pre : queue != null && n > 0
    // Post: R = a[n - 1] &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    public static Object peek(ArrayQueueADT queue) {
        assert queue.head != queue.tail;
        Objects.requireNonNull(queue);

        return queue.elements[queue.tail];
    }

    // Pre : queue != null && n > 0
    // Post: R = a[1] &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    public static Object element(ArrayQueueADT queue) {
        assert queue.head != queue.tail;
        Objects.requireNonNull(queue);

        return queue.elements[CircularUtils.inc(queue.head, queue.elements.length)];
    }

    // Pre : queue != null
    // Post: R = n &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    public static int size(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);

        return CircularUtils.size(queue.head, queue.tail, queue.elements.length);
    }

    // Pre : queue != null && predicate != null
    // Post: R = count all x in a for which the predicate(x) == true &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    public static int countIf(ArrayQueueADT queue, final Predicate<Object> predicate) {
        Objects.requireNonNull(queue);
        Objects.requireNonNull(predicate);

        // :NOTE: multi-vars
        int count = 0,
                i = CircularUtils.inc(queue.head, queue.elements.length),
                j = CircularUtils.inc(queue.tail, queue.elements.length);
        // :NOTE: for
        // :NOTE: performance
        while (i != j) {
            if (predicate.test(queue.elements[i])) {
                count++;
            }
            i = CircularUtils.inc(i, queue.elements.length);
        }
        return count;
    }

    // Pre : queue != null
    // Post: R = (n == 0) &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    public static boolean isEmpty(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);

        return queue.head == queue.tail;
    }

    // Pre : queue != null &&
    // Post: n' = 0
    public static void clear(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);

        CircularUtils.fill(queue.elements, queue.head, queue.tail, null);
        queue.head = queue.tail = 0;
    }
}
