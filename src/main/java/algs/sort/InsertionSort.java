package algs.sort;

import algs.shared.util.ArrayHelper;
import algs.shared.util.Testable;

/**
 * Best: O(n) Average, Worst: O(n2)
 */
public class InsertionSort implements Testable {

    /**
     * Function to algs.sort array using insertion algs.sort
     */
   private void sort(int[] arr) {
        int n = arr.length;

        for (int i = 1 ; i < n ; i++) {
            int value = arr[i];
            int j = i - 1;

            // Move elements of arr[0..i-1], that are
            // greater than value, to one position ahead
            // of their current position
            while (j >= 0 && arr[j] > value) {
                arr[j + 1] = arr[j];
                j -= 1;
            }

            arr[j + 1] = value;
        }
    }

    @Override
    public void test() {
        int[] arr = { 12, 11, 13, 5, 6 };
        sort(arr);
        ArrayHelper.print(arr);
    }

}
