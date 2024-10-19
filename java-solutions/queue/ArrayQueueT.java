package queue;

import queue.ArrayQueue;
import queue.ArrayQueueModule;

public class ArrayQueueT {
    public static void fill(ArrayQueue queue, String prefix) {
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
        for (int i = 0; i < 5; i++) {
            queue.dequeue();
        }
        for (int i = 10; i < 20; i++) {
            queue.enqueue(i);
        }

        for (int i = 0; i < 10; i++) {
            queue.push(i);
        }
        for (int i = 0; i < 5; i++) {
            queue.remove();
        }
        for (int i = 10; i < 20; i++) {
            queue.push(i);
        }
    }

    public static void dump(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(queue.size() + " " + queue.dequeue());
        }
    }

    public static void main(String[] args) {
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        fill(queue1, "q1_");
        fill(queue2, "q2_");
        dump(queue1);
        dump(queue2);
    }
}
