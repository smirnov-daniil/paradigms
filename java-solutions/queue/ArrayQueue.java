package queue;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

// Model: a[1..n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(n, i, j): forall l=0..n-1: a'[i+l] = a[j+l]
public class ArrayQueue extends AbstractQueue {
    private int head = 0;
    private Object[] elements = new Object[3];

    @Override
    protected Queue create() {
        return new ArrayQueue();
    }

    // Pre : element != null
    // Post: n' = n + 1 &&
    //       a'[1] = element &&
    //       immutable(n, 2, 1)
    public void push(Object element) {
        Objects.requireNonNull(element);

        elements[head] = element;
        head = CircularUtils.dec(head, elements.length);
        extendCapacity();
        size++;
    }

    // Pre : true
    // Post: n' = n &&
    //       immutable(n, 1, 1)
    private int newCapacity() {
        return 1 + (elements.length - 1) * 2;
    }

    // Pre : true
    // Post: n' = n &&
    //       immutable(n, 1, 1)
    private void extendCapacity() {
        int tail = CircularUtils.inc(head, size + 1, elements.length);
        if (head == tail) {
            elements = CircularUtils.copyOf(elements, head, tail, newCapacity());
            head = 0;
        }
    }

    // Pre : n > 0
    // Post: R = a[n] &&
    //       n' = n - 1 &&
    //       immutable(n', 1, 1)
    public Object remove() {
        int tail = CircularUtils.inc(head, size, elements.length);

        assert head != tail;

        Object value = elements[tail];
        elements[tail] = null;
        size--;
        return value;
    }

    // Pre : n > 0
    // Post: R = a[n] &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    public Object peek() {
        int tail = CircularUtils.inc(head, size, elements.length);

        assert head != tail;

        return elements[tail];
    }

    // Pre : predicate != null
    // Post: R = count all x in a for which the predicate(x) == true &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    public int countIf(final Predicate<Object> predicate) {
        Objects.requireNonNull(predicate);

        int count = 0,
                i = CircularUtils.inc(head, elements.length),
                j = CircularUtils.inc(head, size + 1, elements.length);
        while (i != j) {
            if (predicate.test(elements[i])) {
                count++;
            }
            i = CircularUtils.inc(i, elements.length);
        }
        return count;
    }

    @Override
    protected void addLast(Object element) {
        elements[CircularUtils.inc(head, size + 1, elements.length)] = element;
        extendCapacity();
    }

    @Override
    protected void removeFirst() {
        head = CircularUtils.inc(head, elements.length);
        elements[head] = null;
    }

    @Override
    protected Object getFirst() {
        return elements[CircularUtils.inc(head, elements.length)];
    }

    @Override
    protected void clearAll() {
        CircularUtils.fill(elements, head, CircularUtils.inc(head, size + 1, elements.length), null);
        head = 0;
    }

    @Override
    protected AbstractQueue.QueueIterator createIterator() {
        return new QueueIterator();
    }

    private class QueueIterator extends AbstractQueue.QueueIterator {
        private int current = head;
        private final int last = CircularUtils.inc(head, size, elements.length);

        @Override
        public boolean hasNext() {
            return current != last;
        }

        @Override
        public Object next() {
            current = CircularUtils.inc(current, elements.length);
            return elements[current];
        }
    }

    public static void main(String[] args) {
        final AbstractQueue queue = new ArrayQueue();
        for (int i = 0; i < 3; i++) {
            queue.enqueue("a" + i);
        }
        final Iterator<Object> it = queue.iterator();
        while (!queue.isEmpty()) {
            queue.dequeue();
        }
        for (int i = 0; i < 3; i++) {
            queue.enqueue("b" + i);
        }
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}
