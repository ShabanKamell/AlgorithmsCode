package algs.sort

import algs.shared.util.ArrayHelper
import algs.shared.util.Testable

/**
 * Best, Average, Worst: O(n2)
 */
class SelectionSort : Testable {
    private fun sort(arr: IntArray) {
        val n = arr.size

        // One by one move boundary of unsorted sub-array
        for (i in 0 until n - 1) {
            // Find the minimum element in unsorted array
            var minIdx = i
            for (j in i + 1 until n) if (arr[j] < arr[minIdx]) minIdx = j

            // Swap the found minimum element with the first
            // element
            val temp = arr[minIdx]
            arr[minIdx] = arr[i]
            arr[i] = temp
        }
    }

    override fun test() {
        val arr = intArrayOf(12, 11, 13, 5, 6)
        sort(arr)
        ArrayHelper.print(arr)
    }
}