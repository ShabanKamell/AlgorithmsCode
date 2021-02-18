/******************************************************************************
 * Compilation:  javac IndexMinPQ.java
 * Execution:    java IndexMinPQ
 * Dependencies: StdOut.java
 *
 * Minimum-oriented indexed PQ implementation using a binary heap.
 *
 */
package algs.shared.datastructure

import algs.shared.util.StdOut
import java.util.*

/**
 * The `IndexMinPQ` class represents an indexed priority queue of generic keys.
 * It supports the usual *insert* and *delete-the-minimum*
 * operations, along with *delete* and *change-the-key*
 * methods. In order to let the client refer to keys on the priority queue,
 * an integer between `0` and `maxN - 1`
 * is associated with each key—the client uses this integer to specify
 * which key to delete or change.
 * It also supports methods for peeking at the minimum key,
 * testing if the priority queue is empty, and iterating through
 * the keys.
 *
 *
 * This implementation uses a binary heap along with an array to associate
 * keys with integers in the given range.
 * The *insert*, *delete-the-minimum*, *delete*,
 * *change-key*, *decrease-key*, and *increase-key*
 * operations take (log *n*) time in the worst case,
 * where *n* is the number of elements in the priority queue.
 * Construction takes time proportional to the specified capacity.
 *
 *
 * For additional documentation, see
 * [Section 2.4](https://algs4.cs.princeton.edu/24pq) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 *
 * @param <Key> the generic type of key on this priority queue
</Key> */
class IndexMinPQ<Key : Comparable<Key>?>(maxN: Int) : Iterable<Int?> {
    private val maxN // maximum number of elements on PQ
            : Int
    private var n // number of elements on PQ
            : Int
    private val pq // binary heap using 1-based indexing
            : IntArray
    private val qp // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
            : IntArray
    private val keys // keys[i] = priority of i
            : Array<Key?>

    /**
     * Returns true if this priority queue is empty.
     *
     * @return `true` if this priority queue is empty;
     * `false` otherwise
     */
    val isEmpty: Boolean
        get() = n == 0

    /**
     * Is `i` an index on this priority queue?
     *
     * @param  i an index
     * @return `true` if `i` is an index on this priority queue;
     * `false` otherwise
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     */
    operator fun contains(i: Int): Boolean {
        validateIndex(i)
        return qp[i] != -1
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    fun size(): Int {
        return n
    }

    /**
     * Associates key with index `i`.
     *
     * @param  i an index
     * @param  key the key to associate with index `i`
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     * @throws IllegalArgumentException if there already is an item associated
     * with index `i`
     */
    fun insert(i: Int, key: Key) {
        validateIndex(i)
        require(!contains(i)) { "index is already in the priority queue" }
        n++
        qp[i] = n
        pq[n] = i
        keys[i] = key
        swim(n)
    }

    /**
     * Returns an index associated with a minimum key.
     *
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    fun minIndex(): Int {
        if (n == 0) throw NoSuchElementException("Priority queue underflow")
        return pq[1]
    }

    /**
     * Returns a minimum key.
     *
     * @return a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    fun minKey(): Key? {
        if (n == 0) throw NoSuchElementException("Priority queue underflow")
        return keys[pq[1]]
    }

    /**
     * Removes a minimum key and returns its associated index.
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    fun delMin(): Int {
        if (n == 0) throw NoSuchElementException("Priority queue underflow")
        val min = pq[1]
        exch(1, n--)
        sink(1)
        assert(min == pq[n + 1])
        qp[min] = -1 // delete
        keys[min] = null // to help with garbage collection
        pq[n + 1] = -1 // not needed
        return min
    }

    /**
     * Returns the key associated with index `i`.
     *
     * @param  i the index of the key to return
     * @return the key associated with index `i`
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     * @throws NoSuchElementException no key is associated with index `i`
     */
    fun keyOf(i: Int): Key? {
        validateIndex(i)
        return if (!contains(i)) throw NoSuchElementException("index is not in the priority queue") else keys[i]
    }

    /**
     * Change the key associated with index `i` to the specified value.
     *
     * @param  i the index of the key to change
     * @param  key change the key associated with index `i` to this key
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     * @throws NoSuchElementException no key is associated with index `i`
     */
    fun changeKey(i: Int, key: Key) {
        validateIndex(i)
        if (!contains(i)) throw NoSuchElementException("index is not in the priority queue")
        keys[i] = key
        swim(qp[i])
        sink(qp[i])
    }

    /**
     * Change the key associated with index `i` to the specified value.
     *
     * @param  i the index of the key to change
     * @param  key change the key associated with index `i` to this key
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     */
    @Deprecated("Replaced by {@code changeKey(int, Key)}.")
    fun change(i: Int, key: Key) {
        changeKey(i, key)
    }

    /**
     * Decrease the key associated with index `i` to the specified value.
     *
     * @param  i the index of the key to decrease
     * @param  key decrease the key associated with index `i` to this key
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     * @throws IllegalArgumentException if `key >= keyOf(i)`
     * @throws NoSuchElementException no key is associated with index `i`
     */
    fun decreaseKey(i: Int, key: Key) {
        validateIndex(i)
        if (!contains(i)) throw NoSuchElementException("index is not in the priority queue")
        require(keys[i]!!.compareTo(key) != 0) { "Calling decreaseKey() with a key equal to the key in the priority queue" }
        require(keys[i]!!.compareTo(key) >= 0) { "Calling decreaseKey() with a key strictly greater than the key in the priority queue" }
        keys[i] = key
        swim(qp[i])
    }

    /**
     * Increase the key associated with index `i` to the specified value.
     *
     * @param  i the index of the key to increase
     * @param  key increase the key associated with index `i` to this key
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     * @throws IllegalArgumentException if `key <= keyOf(i)`
     * @throws NoSuchElementException no key is associated with index `i`
     */
    fun increaseKey(i: Int, key: Key) {
        validateIndex(i)
        if (!contains(i)) throw NoSuchElementException("index is not in the priority queue")
        require(keys[i]!!.compareTo(key) != 0) { "Calling increaseKey() with a key equal to the key in the priority queue" }
        require(keys[i]!!.compareTo(key) <= 0) { "Calling increaseKey() with a key strictly less than the key in the priority queue" }
        keys[i] = key
        sink(qp[i])
    }

    /**
     * Remove the key associated with index `i`.
     *
     * @param  i the index of the key to remove
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     * @throws NoSuchElementException no key is associated with index `i`
     */
    fun delete(i: Int) {
        validateIndex(i)
        if (!contains(i)) throw NoSuchElementException("index is not in the priority queue")
        val index = qp[i]
        exch(index, n--)
        swim(index)
        sink(index)
        keys[i] = null
        qp[i] = -1
    }

    // throw an IllegalArgumentException if i is an invalid index
    private fun validateIndex(i: Int) {
        require(i >= 0) { "index is negative: $i" }
        require(i < maxN) { "index >= capacity: $i" }
    }

    /***************************************************************************
     * General helper functions.
     */
    private fun greater(i: Int, j: Int): Boolean {
        return keys[pq[i]]!! > keys[pq[j]]!!
    }

    private fun exch(i: Int, j: Int) {
        val swap = pq[i]
        pq[i] = pq[j]
        pq[j] = swap
        qp[pq[i]] = i
        qp[pq[j]] = j
    }

    /***************************************************************************
     * Heap helper functions.
     */
    private fun swim(k: Int) {
        var k = k
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2)
            k = k / 2
        }
    }

    private fun sink(k: Int) {
        var k = k
        while (2 * k <= n) {
            var j = 2 * k
            if (j < n && greater(j, j + 1)) j++
            if (!greater(k, j)) break
            exch(k, j)
            k = j
        }
    }
    /***************************************************************************
     * Iterators.
     */
    /**
     * Returns an iterator that iterates over the keys on the
     * priority queue in ascending order.
     * The iterator doesn't implement `remove()` since it's optional.
     *
     * @return an iterator that iterates over the keys in ascending order
     */
    override fun iterator(): MutableIterator<Int> {
        return HeapIterator()
    }

    private inner class HeapIterator : MutableIterator<Int> {
        // create a new pq
        private val copy: IndexMinPQ<Key> = IndexMinPQ(pq.size - 1)
        override fun hasNext(): Boolean {
            return !copy.isEmpty
        }

        override fun remove() {
            throw UnsupportedOperationException()
        }

        override fun next(): Int {
            if (!hasNext()) throw NoSuchElementException()
            return copy.delMin()
        }

        // add all elements to copy of heap
        // takes linear time since already in heap order so no keys move
        init {
            for (i in 1..n) copy.insert(pq[i], keys[pq[i]]!!)
        }
    }

    companion object {
        /**
         * Unit tests the `IndexMinPQ` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            // insert a bunch of strings
            val strings = arrayOf("it", "was", "the", "best", "of", "times", "it", "was", "the", "worst")
            val pq = IndexMinPQ<String>(strings.size)
            for (i in strings.indices) {
                pq.insert(i, strings[i])
            }

            // delete and print each key
            while (!pq.isEmpty) {
                val i = pq.delMin()
                StdOut.println(i.toString() + " " + strings[i])
            }
            StdOut.println()

            // reinsert the same strings
            for (i in strings.indices) {
                pq.insert(i, strings[i])
            }

            // print each key using the iterator
            for (i in pq) {
                StdOut.println(i.toString() + " " + strings[i])
            }
            while (!pq.isEmpty) {
                pq.delMin()
            }
        }
    }

    /**
     * Initializes an empty indexed priority queue with indices between `0`
     * and `maxN - 1`.
     * @param  maxN the keys on this priority queue are index from `0`
     * to `maxN - 1`
     * @throws IllegalArgumentException if `maxN < 0`
     */
    init {
        require(maxN >= 0)
        this.maxN = maxN
        n = 0
        keys = arrayOfNulls<Comparable<*>?>(maxN + 1) as Array<Key?> // make this of length maxN??
        pq = IntArray(maxN + 1)
        qp = IntArray(maxN + 1) // make this of length maxN??
        for (i in 0..maxN) qp[i] = -1
    }
}