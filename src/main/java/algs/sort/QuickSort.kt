package algs.sort

import algs.shared.util.ArrayHelper
import algs.shared.util.Testable

/**
 * Best, Average: O(n log n), Worst: O(n2)
 */
class QuickSort : Testable {
    /**
     * The main function that implements QuickSort
     * arr[] --> Array to be sorted,
     * low   --> Starting index,
     * high  --> Ending index
     */
    private fun sort(arr: IntArray, low: Int, high: Int) {
        if (low < high) {
            // pi is partitioning index, arr[pi] is
            // now at right place
            val pi = partition(arr, low, high)

            // Recursively algs.sort elements before
            // partition and after partition
            sort(arr, low, pi - 1)
            sort(arr, pi + 1, high)
        }
    }

    /**
     * This function takes last element as pivot,
     * places the pivot element at its correct
     * position in sorted array,
     * and places all smaller (smaller than pivot) to left of pivot
     * and all greater elements to right of pivot
     */
    private fun partition(arr: IntArray, low: Int, high: Int): Int {
        val pivot = arr[high]
        var i = low - 1 // index of smaller element

        // places all smaller elements to left of pivot
        for (j in low until high) {
            // If current element is smaller than the pivot
            if (arr[j] < pivot) {
                i++

                // swap arr[i] and arr[j]
                val temp = arr[i]
                arr[i] = arr[j]
                arr[j] = temp
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        val temp = arr[i + 1]
        arr[i + 1] = arr[high]
        arr[high] = temp
        return i + 1
    }

    override fun test() {
        val arr = intArrayOf(12, 11, 13, 5, 6)
        val n = arr.size
        sort(arr, 0, n - 1)
        ArrayHelper.print(arr)
    }
}