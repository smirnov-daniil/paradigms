package search;

import java.util.Arrays;

public class BinarySearchClosestI {
    // a[a.length]              := +inf
    // non-decreasing-seq(a)    -> ∀ 0 <= i < j <= a.length a[i] <= a[j]
    // max-index-not-less(x, a) -> 0 <= R <= a.length : a[R] >= x ∧ ∀ i < R a[i] < x
    // min-index-equal(x, a)    -> ∃ 0 <= R <= a.length, a[R] == x ⇒ ∀ i < R a[i] < a[R]
    // closest(x, a)            -> 0 <= R < a.length : ∀ 0 <= i < a.length ϱ(a[R], r) <= ϱ(a[i], x)


    // int-convertible(a)       -> ∀ a[i] ∃ Integer.parseInt(a[i])
    // print                    -> value, which was printed in System.out stream
    // Pred: int-compatible(args) ∧ not-decreasing-seq(args[1:])
    // Post: print == (closest(x, args[1:]) ∧ min-index-equal(x, args[1:]))
    public static void main(String[] args) {
        // int-convertible(args) ∧ not-decreasing-seq(args[1:])
        if (args.length <= 1) {
            // args.length <= 1 ⇒ args[1:].length == 0
            System.out.println(0);
            // args[1:].length == 0 == print ⇒
            //   print == args[1:][args[1:].length] == max-index-not-less(args[0], args[1:]) ∧ min-index-equal(x, args[1:]) ⇒
            //   print == (closest(x, args[1:]) ∧ min-index-equal(x, args[1:]))
            return;
        }
        // int-convertible(args) ∧ not-decreasing-seq(args[1:]) ∧ args.length > 1

        // int-convertible(args) ∧ not-decreasing-seq(args[1:]) ∧ args.length > 1
        int x = Integer.parseInt(args[0]);
        // int-convertible(args) ∧ not-decreasing-seq(args[1:]) ∧ args.length > 1

        // int-convertible(args) ∧ not-decreasing-seq(args[1:]) ∧ args.length > 1
        int[] a = Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray();
        // a.length > 0 ∧ not-decreasing-seq(a)

        // a.length > 0 ∧ not-decreasing-seq(a)
        int result = iterative(x, a);
        // result == max-index-not-less(x, a)

        // result == max-index-not-less(x, a)
        if (result == a.length) {
            // result == a.length ∧ a[result] = inf ⇒ a.length - 1 == closest(x, a)
            result = recursive(a[a.length - 1], a);
        } else if (result == 0) {
            // result == 0 ∧ max-index-not-less(x, a) == result ⇒ result == (closest(x, a) ∧ min-index-equal(x, a))
        } else if (x - a[result - 1] <= a[result] - x) {
            // x - a[result - 1] <= a[result] - x ∧ max-index-not-less(x, a) == result ⇒ closest(x, a) == result - 1
            result = iterative(a[result - 1], a);
        } else {
            // x - a[result - 1] > a[result] - x ∧ max-index-not-less(x, a) == result ⇒ closest(x, a) == result
            result = recursive(a[result], a);
        }
        // result' == (closest(x, a) ∧ min-index-equal(x, a))

        // true
        System.out.println(result);
        // print == (closest(x, a) ∧ min-index-equal(x, a))
    }

    // Pred: a.length > 0 ∧ not-decreasing-seq(a)
    // Post: R == max-index-not-less(x, a) ∧ R == min-index-equal(x, a)
    private static int iterative(int x, int[] a) {
        // not-decreasing-seq(a)
        int l = 0, r = a.length;
        // not-decreasing-seq(a)

        // Inv := not-decreasing-seq(a) ∧ l' <= R <= r'
        while (l < r) {
            // Inv ∧ l' < r'
            int m = l + (r - l) / 2;
            // Inv ∧ l' <= m < r'

            // Inv ∧ l' <= m < r'
            if (a[m] < x) {
                // a[m] < x ∧ l' <= m < r' ∧ Inv ⇔ a[m] < x ∧ l' <= m < r' ∧ l' <= m < R < r'
                l = m + 1;
                // a[l'] <= x ∧ Inv
            } else {
                // a[m] >= x ∧ l' <= m < r' ∧ Inv ⇔ a[m] >= x ∧ l' <= m < r' ∧ l' <= R < m < r'
                r = m;
                // a[r'] >= x ∧ Inv
            }
            // Inv
            // NB: hard variant (r' >= l' ∧ ϱ(l', r') <= prev-ϱ(l', r') / 2 ∧ Inv)
        }
        // l' == r' ∧ Inv ⇔ l' == r' ∧ l' <= R <= r' ⇔ l' == R == r'
        return l;
    }

    // Pred: a.length > 0 ∧ not-decreasing-seq(a)
    // Post: R == max-index-not-less(x, a) ∧ R == min-index-equal(x, a)
    private static int recursive(int x, int[] a) {
        return recursive(x, a, 0, a.length);
    }

    // Inv := not-decreasing-seq(a) ∧ l <= R <= r
    // Pred: a.length > 0 ∧ Inv
    // Post: R == max-index-not-less(x, a) ∧ R == min-index-equal(x, a)
    private static int recursive(int x, int[] a, int l, int r) {
        // Inv
        if (l < r) {
            // Inv ∧ l < r
            int m = (l + r) / 2;
            // Inv ∧ l <= m < r

            // Inv ∧ l <= m < r
            if (a[m] < x) {
                // a[m] < x ∧ l <= m < r ∧ Inv ⇔ a[m] < x ∧ l <= m < r ∧ l <= m < R <= r
                // NB: hard variant (r >= m + 1 ∧ r - m - 1 <= (r - l) / 2 ∧ Inv)
                return recursive(x, a, m + 1, r);
            } else {
                // a[m] >= x ∧ l <= m < r ∧ Inv ⇔ a[m] >= x ∧ l <= m < r ∧ l <= R <= m < r
                // NB: hard variant (l <= m ∧ m - l <= (r - l) / 2 ∧ Inv)
                return recursive(x, a, l, m);
            }
        } else {
            // l == r ∧ Inv ⇔ l == r ∧ l <= R <= r ⇔ l == R == r
            return l;
        }
    }
}
