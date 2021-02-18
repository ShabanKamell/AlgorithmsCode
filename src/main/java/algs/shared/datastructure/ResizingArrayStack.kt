/******************************************************************************
 * Compilation:  javac ResizingArrayStack.java
 * Execution:    java ResizingArrayStack < input.txt
 * Dependencies: StdIn.java StdOut.java
 * Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 * Stack implementation with a resizing array.
 *
 * % more tobe.txt
 * to be or not to - be - - that - - - is
 *
 * % java ResizingArrayStack < tobe.txt
 * to be not that or be (2 left on stack)
 *
 */
package algs.shared.datastructure

import algs.shared.util.StdIn
import algs.shared.util.StdOut
import java.util.*

/**
 * The `ResizingArrayStack` class represents a last-in-first-out (LIFO) stack
 * of generic items.
 * It supports the usual *push* and *pop* operations, along with methods
 * for peeking at the top item, testing if the stack is empty, and iterating through
 * the items in LIFO order.
 *
 *
 * This implementation uses a resizing array, which double the underlying array
 * when it is full and halves the underlying array when it is one-quarter full.
 * The *push* and *pop* operations take constant amortized time.
 * The *size*, *peek*, and *is-empty* operations takes
 * constant time in the worst case.
 *
 *
 * For additional documentation,
 * see [Section 1.3](https://algs4.cs.princeton.edu/13stacks) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class ResizingArrayStack<Item> : Iterable<Item> {
    private var a // array of items
            : Array<Item?>
    private var n // number of elements on stack
            : Int

    /**
     * Is this stack empty?
     * @return true if this stack is empty; false otherwise
     */
    val isEmpty: Boolean
        get() = n == 0

    /**
     * Returns the number of items in the stack.
     * @return the number of items in the stack
     */
    fun size(): Int {
        return n
    }

    // resize the underlying array holding the elements
    private fun resize(capacity: Int) {
        assert(capacity >= n)

        // textbook implementation
        val copy = arrayOfNulls<Any>(capacity) as Array<Item?>
        for (i in 0 until n) {
            copy[i] = a[i]
        }
        a = copy

        // alternative implementation
        // a = java.main.java.algs.shared.util.Arrays.copyOf(a, capacity);
    }

    /**
     * Adds the item to this stack.
     * @param item the item to add
     */
    fun push(item: Item) {
        if (n == a.size) resize(2 * a.size) // double size of array if necessary
        a[n++] = item // add item
    }

    /**
     * Removes and returns the item most recently added to this stack.
     * @return the item most recently added
     * @throws NoSuchElementException if this stack is empty
     */
    fun pop(): Item? {
        if (isEmpty) throw NoSuchElementException("Stack underflow")
        val item = a[n - 1]
        a[n - 1] = null // to avoid loitering
        n--
        // shrink size of array if necessary
        if (n > 0 && n == a.size / 4) resize(a.size / 2)
        return item
    }

    /**
     * Returns (but does not remove) the item most recently added to this stack.
     * @return the item most recently added to this stack
     * @throws NoSuchElementException if this stack is empty
     */
    fun peek(): Item? {
        if (isEmpty) throw NoSuchElementException("Stack underflow")
        return a[n - 1]
    }

    /**
     * Returns an iterator to this stack that iterates through the items in LIFO order.
     * @return an iterator to this stack that iterates through the items in LIFO order.
     */
    override fun iterator(): Iterator<Item> {
        return ReverseArrayIterator()
    }

    // an iterator, doesn't implement remove() since it's optional
    private inner class ReverseArrayIterator : MutableIterator<Item> {
        private var i: Int
        override fun hasNext(): Boolean {
            return i >= 0
        }

        override fun remove() {
            throw UnsupportedOperationException()
        }

        override fun next(): Item {
            if (!hasNext()) throw NoSuchElementException()
            return a[i--]!!
        }

        init {
            i = n - 1
        }
    }

    companion object {
        /**
         * Unit tests the `Stack` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val stack = ResizingArrayStack<String?>()
            while (!StdIn.isEmpty) {
                val item = StdIn.readString()
                if (item != "-") stack.push(item) else if (!stack.isEmpty) StdOut.print(stack.pop().toString() + " ")
            }
            StdOut.println("(" + stack.size() + " left on stack)")
        }
    }

    /**
     * Initializes an empty stack.
     */
    init {
        a = arrayOfNulls<Any>(2) as Array<Item?>
        n = 0
    }
}