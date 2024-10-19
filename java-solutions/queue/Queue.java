package queue;

import java.util.List;
import java.util.function.Function;

// Model: a[1..n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(n, i, j): forall l=0..n-1: a'[i+l] = a[j+l]
public interface Queue {
    // Pre : element != null
    // Post: n' = n + 1 &&
    //       a'[n'] = element &&
    //       immutable(n, 1, 1)
    void enqueue(Object element);

    // Pre : n > 0
    // Post: R = a[1] &&
    //       n' = n - 1 &&
    //       immutable(n', 1, 2)
    Object dequeue();

    // Pre : n > 0
    // Post: R = a[1] &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    Object element();

    // Pre : true
    // Post: R = n &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    int size();

    // Pre : true
    // Post: R = (n == 0) &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    boolean isEmpty();

    // Pre : true
    // Post: n' = 0
    void clear();

    // Pre : func != null && forall i=1..n in func(a[i]) != null && forall y in func(a[i]): y != null
    // Let : Queue()        - new instance of Queue
    //       m |= k         - m' = [m[1]..m[m.length], k[1]..k[k.length]]
    // :NOTE: order
    // Post: R = Queue(for i in [1..n] Queue.a' |= func(a[i])) &&
    //       n' = n &&
    //       immutable(n, 1, 1)
    Queue flatMap(Function<Object, List<Object>> func);
}
