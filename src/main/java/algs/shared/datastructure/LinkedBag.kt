/******************************************************************************
 * Compilation:  javac LinkedBag.java
 * Execution:    java LinkedBag < input.txt
 * Dependencies: StdIn.java StdOut.java
 *
 * A generic bag or multiset, implemented using a singly linked list.
 *
 * % more tobe.txt
 * to be or not to - be - - that - - - is
 *
 * % java LinkedBag < tobe.txt
 * size of bag = 14
 * is
 * -
 * -
 * -
 * that
 * -
 * -
 * be
 * -
 * to
 * not
 * or
 * be
 * to
 *
 */
package algs.shared.datastructure

import algs.shared.util.StdIn
import algs.shared.util.StdOut
import java.util.*

/**
 * The `LinkedBag` class represents a bag (or multiset) of
 * generic items. It supports insertion and iterating over the
 * items in arbitrary order.
 *
 *
 * This implementation uses a singly linked list with a non-static nested class Node.
 * See [Bag] for a version that uses a static nested class.
 * The *add*, *isEmpty*, and *size* operations
 * take constant time. Iteration takes time proportional to the number of items.
 *
 *
 * For additional documentation, see [Section 1.3](https://algs4.cs.princeton.edu/13stacks) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class LinkedBag<Item>
/**
 * Initializes an empty bag.
 */
    : Iterable<Item> {
    private var first // beginning of bag
            : Node? = null
    private var n // number of elements in bag = 0

    // helper linked list class
    private inner class Node {
        val item: Item? = null
        val next: Node? = null
    }

    /**
     * Is this bag empty?
     * @return true if this bag is empty; false otherwise
     */
    val isEmpty: Boolean
        get() = first == null

    /**
     * Returns the number of items in this bag.
     * @return the number of items in this bag
     */
    fun size(): Int {
        return n
    }

    /**
     * Adds the item to this bag.
     * @param item the item to add to this bag
     */
    fun add(item: Item) {
        val oldfirst = first
        first = Node()
        first.item = item
        first.next = oldfirst
        n++
    }

    /**
     * Returns an iterator that iterates over the items in the bag.
     */
    override fun iterator(): MutableIterator<Item> {
        return LinkedIterator()
    }

    // an iterator over a linked list
    private inner class LinkedIterator : MutableIterator<Item> {
        private var current: Node?

        // is there a next item in the iterator?
        override fun hasNext(): Boolean {
            return current != null
        }

        // this method is optional in Iterator interface
        override fun remove() {
            throw UnsupportedOperationException()
        }

        // returns the next item in the iterator (and advances the iterator)
        override fun next(): Item {
            if (!hasNext()) throw NoSuchElementException()
            val item: Item = current!!.item
            current = current!!.next
            return item
        }

        // creates a new iterator
        init {
            current = first
        }
    }

    companion object {
        /**
         * Unit tests the `LinkedBag` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val bag = LinkedBag<String?>()
            while (!StdIn.isEmpty()) {
                val item = StdIn.readString()
                bag.add(item)
            }
            StdOut.println("size of bag = " + bag.size())
            for (s in bag) {
                StdOut.println(s)
            }
        }
    }

}