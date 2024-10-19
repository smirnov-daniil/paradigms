package queue;

public class ArrayQueueADTT {
    public static void fill(ArrayQueueADT queue, String prefix) {
        for (int i = 0; i < 10; i++) {
            ArrayQueueADT.enqueue(queue, prefix + i);
        }
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.dequeue(queue);
        }
        for (int i = 10; i < 20; i++) {
            ArrayQueueADT.enqueue(queue, prefix + i);
        }

        for (int i = 0; i < 10; i++) {
            ArrayQueueADT.push(queue, prefix + i);
        }
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.remove(queue);
        }
        for (int i = 10; i < 20; i++) {
            ArrayQueueADT.push(queue, prefix + i);
        }
    }

    public static void dump(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(
                    ArrayQueueADT.size(queue) + " " +
                    ArrayQueueADT.element(queue) + " " +
                    ArrayQueueADT.dequeue(queue)
            );
        }
    }

    public static void main(String[] args) {
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();
        fill(queue1, "q1_");
        fill(queue2, "q2_");
        dump(queue1);
        dump(queue2);
    }
}
