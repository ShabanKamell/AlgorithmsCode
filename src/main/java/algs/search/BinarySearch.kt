package algs.search

import algs.shared.util.Testable

/**
 * Best: O(1) Average, Worst: O(log n)
 */
class BinarySearch : Testable {
    /**
     * Returns index of x if it is present in arr[l..r], else return -1
     */
    private fun search(arr: IntArray, l: Int, r: Int, x: Int): Int {
        if (r >= l) {
            val mid = l + (r - l) / 2

            // If the element is present at the
            // middle itself
            if (arr[mid] == x) return mid

            // If element is smaller than mid, then
            // it can only be present in left subarray
            return if (arr[mid] > x) search(arr, l, mid - 1, x) else search(arr, mid + 1, r, x)

            // Else the element can only be present
            // in right subarray
        }

        // We reach here when element is not present
        // in array
        return -1
    }

    override fun test() {
        val arr = intArrayOf(5, 6, 11, 12, 13)
        val n = arr.size
        val x = 6
        val result = search(arr, 0, n - 1, x)
        if (result == -1) println("Element not present") else println("Element found at index $result")
    }
}