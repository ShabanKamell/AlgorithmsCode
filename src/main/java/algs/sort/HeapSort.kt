package algs.sort

import algs.shared.util.ArrayHelper
import algs.shared.util.Testable

/**
 * Best, Average, Worst: O(n log n)
 */
class HeapSort : Testable {
    fun sort(arr: IntArray) {
        val n = arr.size

        // Build heap (rearrange array)
        for (i in n / 2 - 1 downTo 0) heapify(arr, n, i)

        // One by one extract an element from heap
        for (i in n - 1 downTo 1) {
            // Move current root to end
            val temp = arr[0]
            arr[0] = arr[i]
            arr[i] = temp

            // call max heapify on the reduced heap
            heapify(arr, i, 0)
        }
    }

    // To heapify a subtree rooted with node i which is
    // an index in arr[]. n is size of heap
    private fun heapify(arr: IntArray, n: Int, i: Int) {
        var largest = i // Initialize largest as root
        val l = 2 * i + 1 // left = 2*i + 1
        val r = 2 * i + 2 // right = 2*i + 2

        // If left child is larger than root
        if (l < n && arr[l] > arr[largest]) largest = l

        // If right child is larger than largest so far
        if (r < n && arr[r] > arr[largest]) largest = r

        // If largest is not root
        if (largest != i) {
            val swap = arr[i]
            arr[i] = arr[largest]
            arr[largest] = swap

            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest)
        }
    }

    override fun test() {
        val arr = intArrayOf(12, 11, 13, 5, 6)
        sort(arr)
        ArrayHelper.print(arr)
    }
}