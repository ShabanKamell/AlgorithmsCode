package algs.sort

import algs.shared.util.ArrayHelper
import algs.shared.util.Testable

/**
 * Best, Average, Worst: O(n log n)
 */
class MergeSort : Testable {
    /**
     * algs.Main function that sorts arr{l..r} using
     * merge()
     */
    private fun sort(arr: IntArray, l: Int, r: Int) {
        if (l < r) {
            // Find the middle point
            val m = (l + r) / 2

            // Sort first and second halves
            sort(arr, l, m)
            sort(arr, m + 1, r)

            // Merge the sorted halves
            merge(arr, l, m, r)
        }
    }

    /**
     * Merges two sub-arrays of arr[].
     * First sub-array is arr{l..m}
     * Second sub-array is arr[m+1..r]
     */
    private fun merge(arr: IntArray, l: Int, m: Int, r: Int) {
        // Find sizes of two sub-arrays to be merged
        val n1 = m - l + 1
        val n2 = r - m

        // Create temp arrays
        val L = IntArray(n1)
        val R = IntArray(n2)

        // Copy data to temp arrays
        for (i in 0 until n1) L[i] = arr[l + i]
        for (j in 0 until n2) R[j] = arr[m + 1 + j]

        // >> Merge the temp arrays

        // Initial indexes of first and second sub-arrays
        var i = 0
        var j = 0

        // Initial index of merged sub-array array
        var k = l
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i]
                i++
            } else {
                arr[k] = R[j]
                j++
            }
            k++
        }

        // Copy remaining elements of L[] if any
        while (i < n1) {
            arr[k] = L[i]
            i++
            k++
        }

        // Copy remaining elements of R[] if any
        while (j < n2) {
            arr[k] = R[j]
            j++
            k++
        }
    }

    override fun test() {
        val arr = intArrayOf(12, 11, 13, 5, 6)
        val n = arr.size
        sort(arr, 0, n - 1)
        ArrayHelper.print(arr)
    }
}