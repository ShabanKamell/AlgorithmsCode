/******************************************************************************
 * Compilation:  javac EdgeWeightedDigraph.java
 * Execution:    java EdgeWeightedDigraph digraph.txt
 * Dependencies: Bag.java DirectedEdge.java
 * Data files:   https://algs4.cs.princeton.edu/44sp/tinyEWD.txt
 * https://algs4.cs.princeton.edu/44sp/mediumEWD.txt
 * https://algs4.cs.princeton.edu/44sp/largeEWD.txt
 *
 * An edge-weighted digraph, implemented using adjacency lists.
 *
 */
package algs.shared.model

import algs.shared.datastructure.Stack
import algs.shared.util.In
import algs.shared.util.StdOut
import algs.shared.util.StdRandom
import java.util.*

/**
 * The `EdgeWeightedDigraph` class represents a edge-weighted
 * digraph of vertices named 0 through *V* - 1, where each
 * directed edge is of type [DirectedEdge] and has a real-valued weight.
 * It supports the following two primary operations: add a directed edge
 * to the digraph and iterate over all of edges incident from a given vertex.
 * It also provides methods for returning the indegree or outdegree of a
 * vertex, the number of vertices *V* in the digraph, and
 * the number of edges *E* in the digraph.
 * Parallel edges and self-loops are permitted.
 *
 *
 * This implementation uses an *adjacency-lists representation*, which
 * is a vertex-indexed array of [Bag] objects.
 * It uses (*E* + *V*) space, where *E* is
 * the number of edges and *V* is the number of vertices.
 * All instance methods take (1) time. (Though, iterating over
 * the edges returned by [.adj] takes time proportional
 * to the outdegree of the vertex.)
 * Constructing an empty edge-weighted digraph with *V* vertices
 * takes (*V*) time; constructing an edge-weighted digraph
 * with *E* edges and *V* vertices takes
 * (*E* + *V*) time.
 *
 *
 * For additional documentation,
 * see [Section 4.4](https://algs4.cs.princeton.edu/44sp) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class EdgeWeightedDigraph {
    private val V // number of vertices in this digraph
            : Int
    private var E = 0 // number of edges in this digraph = 0
    private val adj // adj[v] = adjacency list for vertex v
            : Array<Bag<DirectedEdge>?>
    private val indegree // indegree[v] = indegree of vertex v
            : IntArray

    /**
     * Initializes an empty edge-weighted digraph with `V` vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if `V < 0`
     */
    constructor(V: Int) {
        require(V >= 0) { "Number of vertices in a Digraph must be nonnegative" }
        this.V = V
        E = 0
        indegree = IntArray(V)
        adj = arrayOfNulls(V)
        for (v in 0 until V) adj[v] = Bag<DirectedEdge>()
    }

    /**
     * Initializes a random edge-weighted digraph with `V` vertices and *E* edges.
     *
     * @param  V the number of vertices
     * @param  E the number of edges
     * @throws IllegalArgumentException if `V < 0`
     * @throws IllegalArgumentException if `E < 0`
     */
    constructor(V: Int, E: Int) : this(V) {
        require(E >= 0) { "Number of edges in a Digraph must be nonnegative" }
        for (i in 0 until E) {
            val v = StdRandom.uniform(V)
            val w = StdRandom.uniform(V)
            val weight = 0.01 * StdRandom.uniform(100)
            val e = DirectedEdge(v, w, weight)
            addEdge(e)
        }
    }

    /**
     * Initializes an edge-weighted digraph from the specified input stream.
     * The format is the number of vertices *V*,
     * followed by the number of edges *E*,
     * followed by *E* pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IllegalArgumentException if `in` is `null`
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    constructor(`in`: In?) {
        requireNotNull(`in`) { "argument is null" }
        try {
            V = `in`.readInt()
            require(V >= 0) { "number of vertices in a Digraph must be nonnegative" }
            indegree = IntArray(V)
            adj = arrayOfNulls(V)
            for (v in 0 until V) {
                adj[v] = Bag()
            }
            val E = `in`.readInt()
            require(E >= 0) { "Number of edges must be nonnegative" }
            for (i in 0 until E) {
                val v = `in`.readInt()
                val w = `in`.readInt()
                validateVertex(v)
                validateVertex(w)
                val weight = `in`.readDouble()
                addEdge(DirectedEdge(v, w, weight))
            }
        } catch (e: NoSuchElementException) {
            throw IllegalArgumentException("invalid input format in EdgeWeightedDigraph constructor", e)
        }
    }

    /**
     * Initializes a new edge-weighted digraph that is a deep copy of `G`.
     *
     * @param  G the edge-weighted digraph to copy
     */
    constructor(G: EdgeWeightedDigraph) : this(G.V()) {
        E = G.E()
        for (v in 0 until G.V()) indegree[v] = G.indegree(v)
        for (v in 0 until G.V()) {
            // reverse so that adjacency list is in same order as original
            val reverse = Stack<DirectedEdge>()
            for (e in G.adj[v]!!) {
                reverse.push(e)
            }
            for (e in reverse) {
                adj[v]!!.add(e)
            }
        }
    }

    /**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
    fun V(): Int {
        return V
    }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
    fun E(): Int {
        return E
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        require(!(v < 0 || v >= V)) { "vertex " + v + " is not between 0 and " + (V - 1) }
    }

    /**
     * Adds the directed edge `e` to this edge-weighted digraph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between `0`
     * and `V-1`
     */
    fun addEdge(e: DirectedEdge) {
        val v = e.from()
        val w = e.to()
        validateVertex(v)
        validateVertex(w)
        adj[v]!!.add(e)
        indegree[w]++
        E++
    }

    /**
     * Returns the directed edges incident from vertex `v`.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex `v` as an Iterable
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun adj(v: Int): Iterable<DirectedEdge> {
        validateVertex(v)
        return adj[v]!!
    }

    /**
     * Returns the number of directed edges incident from vertex `v`.
     * This is known as the *outdegree* of vertex `v`.
     *
     * @param  v the vertex
     * @return the outdegree of vertex `v`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun outdegree(v: Int): Int {
        validateVertex(v)
        return adj[v]!!.size()
    }

    /**
     * Returns the number of directed edges incident to vertex `v`.
     * This is known as the *indegree* of vertex `v`.
     *
     * @param  v the vertex
     * @return the indegree of vertex `v`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun indegree(v: Int): Int {
        validateVertex(v)
        return indegree[v]
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * `for (DirectedEdge e : G.edges())`.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    fun edges(): Iterable<DirectedEdge> {
        val list = Bag<DirectedEdge>()
        for (v in 0 until V) {
            for (e in adj(v)) {
                list.add(e)
            }
        }
        return list
    }

    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices *V*, followed by the number of edges *E*,
     * followed by the *V* adjacency lists of edges
     */
    override fun toString(): String {
        val s = StringBuilder()
        s.append(V)
                .append(" ")
                .append(E)
                .append(NEWLINE)
        for (v in 0 until V) {
            s.append(v).append(": ")
            for (e in adj[v]!!) {
                s.append(e).append("  ")
            }
            s.append(NEWLINE)
        }
        return s.toString()
    }

    companion object {
        private val NEWLINE = System.getProperty("line.separator")

        /**
         * Unit tests the `EdgeWeightedDigraph` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = EdgeWeightedDigraph(`in`)
            StdOut.println(G)
        }
    }
}