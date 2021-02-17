/******************************************************************************
 * Compilation:  javac LinkedStack.java
 * Execution:    java LinkedStack < input.txt
 * Dependencies: StdIn.java StdOut.java
 * Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 * A generic stack, implemented using a linked list. Each stack
 * element is of type Item.
 *
 * % more tobe.txt
 * to be or not to - be - - that - - - is
 *
 * % java LinkedStack < tobe.txt
 * to be not that or be (2 left on stack)
 *
 */
package algs.shared.datastructure

import algs.shared.util.StdIn
import algs.shared.util.StdOut
import java.util.*

/**
 * The `LinkedStack` class represents a last-in-first-out (LIFO) stack of
 * generic items.
 * It supports the usual *push* and *pop* operations, along with methods
 * for peeking at the top item, testing if the stack is empty, and iterating through
 * the items in LIFO order.
 *
 *
 * This implementation uses a singly linked list with a non-static nested class for
 * linked-list nodes. See [Stack] for a version that uses a static nested class.
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
 */
class LinkedStack<Item> : Iterable<Item> {
    private var n // size of the stack = 0
    private var first // top of stack
            : Node? = null

    // helper linked list class
    private inner class Node {
        val item: Item? = null
        val next: Node? = null
    }

    /**
     * Is this stack empty?
     * @return true if this stack is empty; false otherwise
     */
    val isEmpty: Boolean
        get() = first == null

    /**
     * Returns the number of items in the stack.
     * @return the number of items in the stack
     */
    fun size(): Int {
        return n
    }

    /**
     * Adds the item to this stack.
     * @param item the item to add
     */
    fun push(item: Item) {
        val oldfirst = first
        first = Node()
        first.item = item
        first.next = oldfirst
        n++
        assert(check())
    }

    /**
     * Removes and returns the item most recently added to this stack.
     * @return the item most recently added
     * @throws NoSuchElementException if this stack is empty
     */
    fun pop(): Item {
        if (isEmpty) throw NoSuchElementException("Stack underflow")
        val item: Item = first!!.item // save item to return
        first = first!!.next // delete first node
        n--
        assert(check())
        return item // return the saved item
    }

    /**
     * Returns (but does not remove) the item most recently added to this stack.
     * @return the item most recently added to this stack
     * @throws NoSuchElementException if this stack is empty
     */
    fun peek(): Item {
        if (isEmpty) throw NoSuchElementException("Stack underflow")
        return first!!.item
    }

    /**
     * Returns a string representation of this stack.
     * @return the sequence of items in the stack in LIFO order, separated by spaces
     */
    override fun toString(): String {
        val s = StringBuilder()
        for (item in this) s.append(item.toString() + " ")
        return s.toString()
    }

    /**
     * Returns an iterator to this stack that iterates through the items in LIFO order.
     * @return an iterator to this stack that iterates through the items in LIFO order.
     */
    override fun iterator(): MutableIterator<Item> {
        return LinkedIterator()
    }

    // an iterator, doesn't implement remove() since it's optional
    private inner class LinkedIterator : MutableIterator<Item> {
        private var current = first
        override fun hasNext(): Boolean {
            return current != null
        }

        override fun remove() {
            throw UnsupportedOperationException()
        }

        override fun next(): Item {
            if (!hasNext()) throw NoSuchElementException()
            val item: Item = current!!.item
            current = current!!.next
            return item
        }
    }

    // check internal invariants
    private fun check(): Boolean {

        // check a few properties of instance variable 'first'
        if (n < 0) {
            return false
        }
        if (n == 0) {
            if (first != null) return false
        } else if (n == 1) {
            if (first == null) return false
            if (first.next != null) return false
        } else {
            if (first == null) return false
            if (first.next == null) return false
        }

        // check internal consistency of instance variable n
        var numberOfNodes = 0
        var x = first
        while (x != null && numberOfNodes <= n) {
            numberOfNodes++
            x = x.next
        }
        return if (numberOfNodes != n) false else true
    }

    companion object {
        /**
         * Unit tests the `LinkedStack` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val stack = LinkedStack<String?>()
            while (!StdIn.isEmpty()) {
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
        assert(check())
    }
}