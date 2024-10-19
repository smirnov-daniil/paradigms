package queue;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class AbstractQueue implements Queue, Iterable<Object> {
    protected int size;
    protected abstract void addLast(Object element);
    protected abstract void removeFirst();
    protected abstract Object getFirst();
    protected abstract void clearAll();
    protected abstract Queue create();

    // Model: iterator points to current element in queue &&
    //        iterator.next points to next element in queue
    // Let  : *iterator - value of element in queue where iterator points\
    // :NOTE: !queue.isEmpty()
    // Inv  : !queue.isEmpty() + queue' = queue, while instance of iterator exists
    protected static abstract class QueueIterator implements Iterator<Object> {
        // Pre : true
        // Post: R = exists next element in queue &&
        //       iterator' = iterator
        // public boolean hasNext()

        // Pre : hasNext()
        // Post: R = *iterator.next &&
        //       iterator' = iterator.next
        // public Object next() {
    }

    // Pre : n > 0
    // Post: R = iterator(a[1]) &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    protected abstract QueueIterator createIterator();

    // Pre : n > 0
    // Post: R = iterator(a[1]) &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    @Override
    public Iterator<Object> iterator() {
        return createIterator();
    }

    @Override
    public Queue flatMap(Function<Object, List<Object>> func) {
        Objects.requireNonNull(func);

        Queue result = create();
        for (Object q : this) {
            for (Object p : func.apply(q)) {
                result.enqueue(p);
            }
        }
        return result;
    }

    @Override
    public void enqueue(Object element) {
        Objects.requireNonNull(element);

        addLast(element);
        size++;
    }

    @Override
    public Object dequeue() {
        assert size > 0;

        Object element = element();
        removeFirst();
        size--;
        return element;
    }

    @Override
    public Object element() {
        assert size > 0;

        return getFirst();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        clearAll();
        size = 0;
    }
}
