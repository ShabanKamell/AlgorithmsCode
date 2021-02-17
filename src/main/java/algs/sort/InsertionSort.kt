package algs.sort

import algs.shared.util.ArrayHelper
import algs.shared.util.Testable

/**
 * Best: O(n) Average, Worst: O(n2)
 */
class InsertionSort : Testable {
    /**
     * Function to algs.sort array using insertion algs.sort
     */
    private fun sort(arr: IntArray) {
        val n = arr.size
        for (i in 1 until n) {
            val value = arr[i]
            var j = i - 1

            // Move elements of arr[0..i-1], that are
            // greater than value, to one position ahead
            // of their current position
            while (j >= 0 && arr[j] > value) {
                arr[j + 1] = arr[j]
                j -= 1
            }
            arr[j + 1] = value
        }
    }

    override fun test() {
        val arr = intArrayOf(12, 11, 13, 5, 6)
        sort(arr)
        ArrayHelper.print(arr)
    }
}