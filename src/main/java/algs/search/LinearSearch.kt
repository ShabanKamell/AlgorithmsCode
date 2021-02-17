package algs.search

import algs.shared.util.Testable

/**
 * Best: O(1), Average, Worst: O(n)
 */
class LinearSearch : Testable {
    override fun test() {
        val arr = intArrayOf(12, 11, 13, 5, 6)
        val x = 6
        val result = search(arr, x)
        if (result == -1) println("Element is not present in array") else println("Element is present at index $result")
    }

    companion object {
        fun search(arr: IntArray, x: Int): Int {
            val n = arr.size
            for (i in 0 until n) {
                if (arr[i] == x) return i
            }
            return -1
        }
    }
}