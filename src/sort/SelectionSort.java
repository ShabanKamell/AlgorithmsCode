package sort;

import util.ArrayHelper;
import util.Testable;

public class SelectionSort implements Testable {

    @Override
    public void test() {
        int[] arr = { 12, 11, 13, 5, 6 };
        sort(arr);
        ArrayHelper.print(arr);
    }

    private void sort(int[] arr) {
        int n = arr.length;

        // One by one move boundary of unsorted sub-array
        for (int i = 0; i < n-1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < n; j++)
                if (arr[j] < arr[min_idx]) min_idx = j;

            // Swap the found minimum element with the first
            // element
            int temp = arr[min_idx];
            arr[min_idx] = arr[i];
            arr[i] = temp;
        }
    }

}
