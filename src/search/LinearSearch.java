package search;

import util.Testable;

/**
 * Best: O(1), Average, Worst: O(n)
 */
public class LinearSearch implements Testable {

    @Override
    public void test() {
        int[] arr = { 12, 11, 13, 5, 6 };

        int x = 6;
        int result = search(arr, x);

        if(result == -1)
            System.out.println("Element is not present in array");
        else
            System.out.println("Element is present at index " + result);
    }

    static int search(int[] arr, int x)
    {
        int n = arr.length;
        for(int i = 0; i < n; i++)
        {
            if(arr[i] == x)
                return i;
        }
        return -1;
    }

}
