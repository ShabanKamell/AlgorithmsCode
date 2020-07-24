package algs.sort;

import algs.shared.util.ArrayHelper;
import algs.shared.util.Testable;

/**
 * Best, Average, Worst: O(n2)
 */
public class SelectionSort implements Testable {

    private void sort(int[] arr) {
        int n = arr.length;

        // One by one move boundary of unsorted sub-array
        for (int i = 0; i < n-1; i++) {
            // Find the minimum element in unsorted array
            int minIdx = i;
            for (int j = i + 1; j < n; j++)
                if (arr[j] < arr[minIdx]) minIdx = j;

            // Swap the found minimum element with the first
            // element
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    @Override
    public void test() {
        int[] arr = { 12, 11, 13, 5, 6 };
        sort(arr);
        ArrayHelper.print(arr);
    }

}
