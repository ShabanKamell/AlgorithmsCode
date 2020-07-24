package algs.sort;

import algs.shared.util.ArrayHelper;
import algs.shared.util.Testable;

/**
 * Best, Average, Worst: O(n log n)
 */
public class MergeSort implements Testable {

    /**
     * algs.Main function that sorts arr[l..r] using
     * merge()
     */
    private void sort(int[] arr, int l, int r) {
        if (l < r) {
            // Find the middle point
            int m = (l + r)/2;

            // Sort first and second halves
            sort(arr, l, m);
            sort(arr , m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    /**
     * Merges two sub-arrays of arr[].
     * First sub-array is arr[l..m]
     * Second sub-array is arr[m+1..r]
     */
    private void merge(int[] arr, int l, int m, int r) {
        // Find sizes of two sub-arrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        // Create temp arrays
        int[] L = new int [n1];
        int[] R = new int [n2];

        // Copy data to temp arrays
        for (int i = 0; i < n1; i++)
            L[i] = arr[l + i];

        for (int j = 0; j < n2; j++)
            R[j] = arr[m + 1 + j];

        // >> Merge the temp arrays

        // Initial indexes of first and second sub-arrays
        int i = 0, j = 0;

        // Initial index of merged sub-array array
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            }
            else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        // Copy remaining elements of L[] if any
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        // Copy remaining elements of R[] if any
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    @Override
    public void test() {
        int[] arr = { 12, 11, 13, 5, 6 };

        int n = arr.length;
        sort(arr, 0, n - 1);

        ArrayHelper.print(arr);
    }

}
