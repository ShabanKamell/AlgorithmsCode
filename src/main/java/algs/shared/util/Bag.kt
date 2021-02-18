package algs.shared.util

import algs.shared.datastructure.LinkedBag
import algs.shared.datastructure.ResizingArrayBag
import java.util.*

/**
 * The `Bag` class represents a bag (or multiset) of
 * generic items. It supports insertion and iterating over the
 * items in arbitrary order.
 *
 *
 * This implementation uses a singly linked list with a static nested class Node.
 * See [LinkedBag] for the version from the
 * textbook that uses a non-static nested class.
 * See [ResizingArrayBag] for a version that uses a resizing array.
 * The *add*, *isEmpty*, and *size* operations
 * take constant time. Iteration takes time proportional to the number of items.
 *
 *
 * For additional documentation, see [Section 1.3](https://algs4.cs.princeton.edu/13stacks) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 *
 * @param <Item> the generic type of an item in this bag
</Item> */
class Bag<Item>
/**
 * Initializes an empty bag.
 */
    : Iterable<Item> {
    private var first // beginning of bag
            : Node<Item>? = null
    private var n = 0 // number of elements in bag = 0

    // helper linked list class
    private class Node<Item> {
        var item: Item? = null
        var next: Node<Item>? = null
    }

    /**
     * Returns true if this bag is empty.
     *
     * @return `true` if this bag is empty;
     * `false` otherwise
     */
    val isEmpty: Boolean
        get() = first == null

    /**
     * Returns the number of items in this bag.
     *
     * @return the number of items in this bag
     */
    fun size(): Int {
        return n
    }

    /**
     * Adds the item to this bag.
     *
     * @param item the item to add to this bag
     */
    fun add(item: Item) {
        val oldfirst = first
        first = Node()
        first!!.item = item
        first!!.next = oldfirst
        n++
    }

    /**
     * Returns an iterator that iterates over the items in this bag in arbitrary order.
     *
     * @return an iterator that iterates over the items in this bag in arbitrary order
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

}