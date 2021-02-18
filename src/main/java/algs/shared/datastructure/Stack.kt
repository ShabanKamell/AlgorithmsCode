/******************************************************************************
 * Compilation:  javac Stack.java
 * Execution:    java Stack < input.txt
 * Dependencies: StdIn.java StdOut.java
 * Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 * A generic stack, implemented using a singly linked list.
 * Each stack element is of type Item.
 *
 * This version uses a static nested class Node (to save 8 bytes per
 * Node), whereas the version in the textbook uses a non-static nested
 * class (for simplicity).
 *
 * % more tobe.txt
 * to be or not to - be - - that - - - is
 *
 * % java Stack < tobe.txt
 * to be not that or be (2 left on stack)
 *
 */
package algs.shared.datastructure

import algs.shared.util.StdIn
import algs.shared.util.StdOut
import java.util.*

/**
 * The `Stack` class represents a last-in-first-out (LIFO) stack of generic items.
 * It supports the usual *push* and *pop* operations, along with methods
 * for peeking at the top item, testing if the stack is empty, and iterating through
 * the items in LIFO order.
 *
 *
 * This implementation uses a singly linked list with a static nested class for
 * linked-list nodes. See [LinkedStack] for the version from the
 * textbook that uses a non-static nested class.
 * See [ResizingArrayStack] for a version that uses a resizing array.
 * The *push*, *pop*, *peek*, *size*, and *is-empty*
 * operations all take constant time in the worst case.
 *
 *
 * For additional documentation,
 * see [Section 1.3](https://algs4.cs.princeton.edu/13stacks) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 *
 * @param <Item> the generic type of an item in this stack
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
</Item> */
class Stack<Item>
/**
 * Initializes an empty stack.
 */
    : Iterable<Item> {
    private var first // top of stack
            : Node<Item>? = null
    private var n = 0 // size of the stack = 0

    // helper linked list class
    private class Node<Item> {
        var item: Item? = null
        var next: Node<Item>? = null
    }

    /**
     * Returns true if this stack is empty.
     *
     * @return true if this stack is empty; false otherwise
     */
    val isEmpty: Boolean
        get() = first == null

    /**
     * Returns the number of items in this stack.
     *
     * @return the number of items in this stack
     */
    fun size(): Int {
        return n
    }

    /**
     * Adds the item to this stack.
     *
     * @param  item the item to add
     */
    fun push(item: Item) {
        val oldfirst = first
        first = Node()
        first?.item = item
        first?.next = oldfirst
        n++
    }

    /**
     * Removes and returns the item most recently added to this stack.
     *
     * @return the item most recently added
     * @throws NoSuchElementException if this stack is empty
     */
    fun pop(): Item {
        if (isEmpty) throw NoSuchElementException("Stack underflow")
        val item: Item = first!!.item!! // save item to return
        first = first!!.next // delete first node
        n--
        return item // return the saved item
    }

    /**
     * Returns (but does not remove) the item most recently added to this stack.
     *
     * @return the item most recently added to this stack
     * @throws NoSuchElementException if this stack is empty
     */
    fun peek(): Item {
        if (isEmpty) throw NoSuchElementException("Stack underflow")
        return first!!.item!!
    }

    /**
     * Returns a string representation of this stack.
     *
     * @return the sequence of items in this stack in LIFO order, separated by spaces
     */
    override fun toString(): String {
        val s = StringBuilder()
        for (item in this) {
            s.append(item)
            s.append(' ')
        }
        return s.toString()
    }

    /**
     * Returns an iterator to this stack that iterates through the items in LIFO order.
     *
     * @return an iterator to this stack that iterates through the items in LIFO order
     */
    override fun iterator(): MutableIterator<Item> {
        return LinkedIterator(first)
    }

    // an iterator, doesn't implement remove() since it's optional
    private inner class LinkedIterator(private var current: Node<Item>?) : MutableIterator<Item> {
        override fun hasNext(): Boolean {
            return current != null
        }

        override fun remove() {
            throw UnsupportedOperationException()
        }

        override fun next(): Item {
            if (!hasNext()) throw NoSuchElementException()
            val item: Item = current!!.item!!
            current = current!!.next
            return item
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
            val stack = Stack<String?>()
            while (!StdIn.isEmpty) {
                val item = StdIn.readString()
                if (item != "-") stack.push(item) else if (!stack.isEmpty) StdOut.print(stack.pop().toString() + " ")
            }
            StdOut.println("(" + stack.size() + " left on stack)")
        }
    }

}