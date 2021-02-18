/******************************************************************************
 * Compilation:  javac ResizingArrayQueue.java
 * Execution:    java ResizingArrayQueue < input.txt
 * Dependencies: StdIn.java StdOut.java
 * Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 * Queue implementation with a resizing array.
 *
 * % java ResizingArrayQueue < tobe.txt
 * to be or not to be (2 left on queue)
 *
 */
package algs.shared.datastructure

import algs.shared.util.StdIn
import algs.shared.util.StdOut
import java.util.*

/**
 * The `ResizingArrayQueue` class represents a first-in-first-out (FIFO)
 * queue of generic items.
 * It supports the usual *enqueue* and *dequeue*
 * operations, along with methods for peeking at the first item,
 * testing if the queue is empty, and iterating through
 * the items in FIFO order.
 *
 *
 * This implementation uses a resizing array, which double the underlying array
 * when it is full and halves the underlying array when it is one-quarter full.
 * The *enqueue* and *dequeue* operations take constant amortized time.
 * The *size*, *peek*, and *is-empty* operations takes
 * constant time in the worst case.
 *
 *
 * For additional documentation, see [Section 1.3](https://algs4.cs.princeton.edu/13stacks) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class ResizingArrayQueue<Item> : Iterable<Item> {
    private var q // queue elements
            : Array<Item?>
    private var n // number of elements on queue
            : Int
    private var first // index of first element of queue
            : Int
    private var last // index of next available slot
            : Int

    /**
     * Is this queue empty?
     * @return true if this queue is empty; false otherwise
     */
    val isEmpty: Boolean
        get() = n == 0

    /**
     * Returns the number of items in this queue.
     * @return the number of items in this queue
     */
    fun size(): Int {
        return n
    }

    // resize the underlying array
    private fun resize(capacity: Int) {
        assert(capacity >= n)
        val copy = arrayOfNulls<Any>(capacity) as Array<Item?>
        for (i in 0 until n) {
            copy[i] = q[(first + i) % q.size]
        }
        q = copy
        first = 0
        last = n
    }

    /**
     * Adds the item to this queue.
     * @param item the item to add
     */
    fun enqueue(item: Item) {
        // double size of array if necessary and recopy to front of array
        if (n == q.size) resize(2 * q.size) // double size of array if necessary
        q[last++] = item // add item
        if (last == q.size) last = 0 // wrap-around
        n++
    }

    /**
     * Removes and returns the item on this queue that was least recently added.
     * @return the item on this queue that was least recently added
     * @throws NoSuchElementException if this queue is empty
     */
    fun dequeue(): Item? {
        if (isEmpty) throw NoSuchElementException("Queue underflow")
        val item = q[first]
        q[first] = null // to avoid loitering
        n--
        first++
        if (first == q.size) first = 0 // wrap-around
        // shrink size of array if necessary
        if (n > 0 && n == q.size / 4) resize(q.size / 2)
        return item
    }

    /**
     * Returns the item least recently added to this queue.
     * @return the item least recently added to this queue
     * @throws NoSuchElementException if this queue is empty
     */
    fun peek(): Item? {
        if (isEmpty) throw NoSuchElementException("Queue underflow")
        return q[first]
    }

    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    override fun iterator(): Iterator<Item> {
        return ArrayIterator()
    }

    // an iterator, doesn't implement remove() since it's optional
    private inner class ArrayIterator : MutableIterator<Item> {
        private var i = 0
        override fun hasNext(): Boolean {
            return i < n
        }

        override fun remove() {
            throw UnsupportedOperationException()
        }

        override fun next(): Item {
            if (!hasNext()) throw NoSuchElementException()
            val item = q[(i + first) % q.size]
            i++
            return item!!
        }
    }

    companion object {
        /**
         * Unit tests the `ResizingArrayQueue` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val queue = ResizingArrayQueue<String?>()
            while (!StdIn.isEmpty) {
                val item = StdIn.readString()
                if (item != "-") queue.enqueue(item) else if (!queue.isEmpty) StdOut.print(queue.dequeue().toString() + " ")
            }
            StdOut.println("(" + queue.size() + " left on queue)")
        }
    }

    /**
     * Initializes an empty queue.
     */
    init {
        q = arrayOfNulls<Any>(2) as Array<Item?>
        n = 0
        first = 0
        last = 0
    }
}