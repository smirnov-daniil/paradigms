package search;

import java.util.Arrays;

public class BinarySearch {
    // a[a.length]              := -inf
    // non-increasing-seq(a)    -> ∀ 0 <= i < j <= a.length a[i] >= a[j]
    // min-index-not-more(x, a) -> 0 <= R <= a.length : a[R] <= x ∧ ∀ i < R a[i] > x


    // int-convertible(a)       -> ∀ a[i] ∃ Integer.parseInt(a[i])
    // print(x)                 -> x, which was printed in System.out stream
    // Pred: int-compatible(args) ∧ not-increasing-seq(args[1:])
    // Post: print(x) == min-index-not-more(x, args[1:])
    public static void main(String[] args) {
        // int-convertible(args) ∧ not-increasing-seq(args[1:])
        if (args.length <= 1) {
            // args.length <= 1 ⇒ args[1:].length == 0
            System.out.println(0);
            // args[1:].length == 0 == print(x) ⇒ print(x) == args[1:][args[1:].length] == min-index-not-more(args[0], args[1:])
            return;
        }
        // int-convertible(args) ∧ not-increasing-seq(args[1:]) ∧ args.length > 1

        // int-convertible(args) ∧ not-increasing-seq(args[1:]) ∧ args.length > 1
        int x = Integer.parseInt(args[0]);
        // int-convertible(args) ∧ not-increasing-seq(args[1:]) ∧ args.length > 1

        // int-convertible(args) ∧ not-increasing-seq(args[1:]) ∧ args.length > 1
        int[] a = Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray();
        // a.length > 0 ∧ not-increasing-seq(a)

        // a.length > 0 ∧ not-increasing-seq(a)
        /* int result = iterative(x, a); */
        int result = recursive(x, a);
        // result == min-index-not-more(x, a)

        // result == min-index-not-more(x, a)
        System.out.println(result);
        // print(x) == result == min-index-not-more(x, a)
    }

    // Pred: a.length > 0 ∧ not-increasing-seq(a)
    // Post: R == min-index-not-more(x, a)
    private static int iterative(int x, int[] a) {
        // not-increasing-seq(a)
        int l = 0, r = a.length;
        // not-increasing-seq(a)

        // Inv := not-increasing-seq(a) ∧ l' <= R <= r'
        while (l < r) {
            // Inv ∧ l' < r'
            int m = (l + r) / 2;
            // Inv ∧ l' <= m < r'

            // Inv ∧ l' <= m < r'
            if (a[m] > x) {
                // a[m] > x ∧ l' <= m < r' ∧ Inv ⇔ a[m] > x ∧ l' <= m < r' ∧ l' <= m < R <= r'
                l = m + 1;
                // a[l'] >= x ∧ Inv
                // l < l_new < R <= r
                // r - l > (r - l_new) * 2
            } else {
                // a[m] <= x ∧ l' <= m < r' ∧ Inv ⇔ a[m] <= x ∧ l' <= m < r' ∧ l' <= R <= m < r'
                r = m;
                // a[r'] <= x ∧ Inv
            }
            // Inv
            // NB: hard variant (r' >= l' ∧ dist(l', r') <= prev-dist(l', r') / 2 ∧ Inv)
        }
        // l' == r' ∧ Inv ⇔ l' == r' ∧ l' <= R <= r' ⇔ l' == R == r'
        return l;
    }

    // Pred: a.length > 0 ∧ not-increasing-seq(a)
    // Post: R == min-index-not-more(x, a)
    private static int recursive(int x, int[] a) {
        return recursive(x, a, 0, a.length);
    }

    // Inv := not-increasing-seq(a) ∧ l <= R <= r
    // Pred: a.length > 0 ∧ Inv
    // Post: R == min-index-not-more(x, a)
    private static int recursive(int x, int[] a, int l, int r) {
        // Inv
        if (l < r) {
            // Inv ∧ l < r
            int m = (l + r) / 2;
            // Inv ∧ l <= m < r

            // Inv ∧ l <= m < r
            if (a[m] > x) {
                // a[m] > x ∧ l <= m < r ∧ Inv ⇔ a[m] > x ∧ l <= m < r ∧ l <= m < R <= r
                // NB: hard variant (r >= m + 1 ∧ r - m - 1 <= (r - l) / 2 ∧ Inv)
                return recursive(x, a, m + 1, r);
            } else {
                // a[m] <= x ∧ l <= m < r ∧ Inv ⇔ a[m] <= x ∧ l <= m < r ∧ l <= R <= m < r
                // NB: hard variant (l <= m ∧ m - l <= (r - l) / 2 ∧ Inv)
                return recursive(x, a, l, m);
            }
        } else {
            // l == r ∧ Inv ⇔ l == r ∧ l <= R <= r ⇔ l == R == r
            return l;
        }
    }
}
