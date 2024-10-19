package queue;

public class ArrayQueueModuleT {
    public static void fill() {
        for (int i = 0; i < 10; i++) {
            ArrayQueueModule.enqueue(i);
        }
        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.dequeue();
        }
        for (int i = 10; i < 35; i++) {
            ArrayQueueModule.enqueue(i);
        }

        for (int i = 0; i < 10; i++) {
            ArrayQueueModule.push(i);
        }
        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.remove();
        }
        for (int i = 10; i < 20; i++) {
            ArrayQueueModule.push(i);
        }
    }

    public static void dump() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(
                    ArrayQueueModule.size() + " " +
                    ArrayQueueModule.element() + " " +
                    ArrayQueueModule.dequeue()
            );
        }
    }

    public static void main(String[] args) {
        fill();
        dump();
    }
}
