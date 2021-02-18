/******************************************************************************
 * Compilation:  javac ResizingArrayBag.java
 * Execution:    java ResizingArrayBag
 * Dependencies: StdIn.java StdOut.java
 *
 * Bag implementation with a resizing array.
 *
 */
package algs.shared.datastructure

import algs.shared.util.StdOut
import java.util.*

/**
 * The `ResizingArrayBag` class represents a bag (or multiset) of
 * generic items. It supports insertion and iterating over the
 * items in arbitrary order.
 *
 *
 * This implementation uses a resizing array.
 * See [LinkedBag] for a version that uses a singly linked list.
 * The *add* operation takes constant amortized time; the
 * *isEmpty*, and *size* operations
 * take constant time. Iteration takes time proportional to the number of items.
 *
 *
 * For additional documentation, see [Section 1.3](https://algs4.cs.princeton.edu/13stacks) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class ResizingArrayBag<Item> : Iterable<Item> {
    private var a // array of items
            : Array<Item?>
    private var n // number of elements on bag
            : Int

    /**
     * Is this bag empty?
     * @return true if this bag is empty; false otherwise
     */
    val isEmpty: Boolean
        get() = n == 0

    /**
     * Returns the number of items in this bag.
     * @return the number of items in this bag
     */
    fun size(): Int {
        return n
    }

    // resize the underlying array holding the elements
    private fun resize(capacity: Int) {
        assert(capacity >= n)
        val copy = arrayOfNulls<Any>(capacity) as Array<Item?>
        for (i in 0 until n) copy[i] = a[i]
        a = copy
    }

    /**
     * Adds the item to this bag.
     * @param item the item to add to this bag
     */
    fun add(item: Item) {
        if (n == a.size) resize(2 * a.size) // double size of array if necessary
        a[n++] = item // add item
    }

    /**
     * Returns an iterator that iterates over the items in the bag in arbitrary order.
     * @return an iterator that iterates over the items in the bag in arbitrary order
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
            return a[i++]!!
        }
    }

    companion object {
        /**
         * Unit tests the `ResizingArrayBag` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val bag = ResizingArrayBag<String>()
            bag.add("Hello")
            bag.add("World")
            bag.add("how")
            bag.add("are")
            bag.add("you")
            for (s in bag) StdOut.println(s)
        }
    }

    /**
     * Initializes an empty bag.
     */
    init {
        a = arrayOfNulls<Any>(2) as Array<Item?>
        n = 0
    }
}