package queue;

public class LinkedQueue extends AbstractQueue {
    private static class Node {
        public final Object value;
        public Node next;
        public Node(Object value) {
            this.value = value;
            this.next = null;
        }
    }
    private Node head, tail;

    @Override
    protected Queue create() {
        return new LinkedQueue();
    }

    @Override
    protected void addLast(Object element) {
        Node node = new Node(element);
        if (isEmpty()) {
            head = tail = node;
        } else {
            tail = tail.next = node;
        }
    }

    // :NOTE: memory leak
    @Override
    protected void removeFirst() {
        head = head.next;
    }

    @Override
    public Object getFirst() {
        return head.value;
    }

    @Override
    protected void clearAll() {
        head = tail = null;
    }

    @Override
    protected AbstractQueue.QueueIterator createIterator() {
        return new QueueIterator();
    }

    private class QueueIterator extends AbstractQueue.QueueIterator {
        private Node current = head;
        @Override
        public boolean hasNext() {
            return current != null;
        }
        @Override
        public Object next() {
            Object result = current.value;
            current = current.next;
            return result;
        }
    }
}
