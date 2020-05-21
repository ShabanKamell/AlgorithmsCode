package algs.search;

import algs.util.Testable;

/**
 * Best: O(1) Average, Worst: O(log n)
 */
public class BinarySearch implements Testable {

    @Override
    public void test() {
        int[] arr = { 5, 6, 11, 12, 13 };

        int n = arr.length;
        int x = 6;

        int result = search(arr, 0, n - 1, x);
        if (result == -1)
            System.out.println("Element not present");
        else
            System.out.println("Element found at index " + result);
    }

    /**
     *  Returns index of x if it is present in arr[l..r], else return -1
     */
    private int search(int[] arr, int l, int r, int x) {
        if (r >= l) {
            int mid = l + (r - l) / 2;

            // If the element is present at the
            // middle itself
            if (arr[mid] == x)
                return mid;

            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (arr[mid] > x)
                return search(arr, l, mid - 1, x);

            // Else the element can only be present
            // in right subarray
            return search(arr, mid + 1, r, x);
        }

        // We reach here when element is not present
        // in array
        return -1;
    }

}
