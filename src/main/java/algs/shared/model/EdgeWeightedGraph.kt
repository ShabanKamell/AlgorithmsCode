/******************************************************************************
 * Compilation:  javac EdgeWeightedGraph.java
 * Execution:    java EdgeWeightedGraph filename.txt
 * Dependencies: Bag.java Edge.java In.java StdOut.java
 * Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 * https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 * https://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 * An edge-weighted undirected main.java.algs.graph, implemented using adjacency lists.
 * Parallel edges and self-loops are permitted.
 *
 * % java EdgeWeightedGraph tinyEWG.txt
 * 8 16
 * 0: 6-0 0.58000  0-2 0.26000  0-4 0.38000  0-7 0.16000
 * 1: 1-3 0.29000  1-2 0.36000  1-7 0.19000  1-5 0.32000
 * 2: 6-2 0.40000  2-7 0.34000  1-2 0.36000  0-2 0.26000  2-3 0.17000
 * 3: 3-6 0.52000  1-3 0.29000  2-3 0.17000
 * 4: 6-4 0.93000  0-4 0.38000  4-7 0.37000  4-5 0.35000
 * 5: 1-5 0.32000  5-7 0.28000  4-5 0.35000
 * 6: 6-4 0.93000  6-0 0.58000  3-6 0.52000  6-2 0.40000
 * 7: 2-7 0.34000  1-7 0.19000  0-7 0.16000  5-7 0.28000  4-7 0.37000
 *
 */
package algs.shared.model

import algs.shared.datastructure.Stack
import algs.shared.util.In
import algs.shared.util.StdOut
import algs.shared.util.StdRandom
import java.util.*

/**
 * The `EdgeWeightedGraph` class represents an edge-weighted
 * main.java.algs.graph of vertices named 0 through *V* – 1, where each
 * undirected edge is of type [Edge] and has a real-valued weight.
 * It supports the following two primary operations: add an edge to the main.java.algs.graph,
 * iterate over all of the edges incident to a vertex. It also provides
 * methods for returning the degree of a vertex, the number of vertices
 * *V* in the main.java.algs.graph, and the number of edges *E* in the main.java.algs.graph.
 * Parallel edges and self-loops are permitted.
 * By convention, a self-loop *v*-*v* appears in the
 * adjacency list of *v* twice and contributes two to the degree
 * of *v*.
 *
 *
 * This implementation uses an *adjacency-lists representation*, which
 * is a vertex-indexed array of [Bag] objects.
 * It uses (*E* + *V*) space, where *E* is
 * the number of edges and *V* is the number of vertices.
 * All instance methods take (1) time. (Though, iterating over
 * the edges returned by [.adj] takes time proportional
 * to the degree of the vertex.)
 * Constructing an empty edge-weighted main.java.algs.graph with *V* vertices takes
 * (*V*) time; constructing a edge-weighted main.java.algs.graph with
 * *E* edges and *V* vertices takes
 * (*E* + *V*) time.
 *
 *
 * For additional documentation,
 * see [Section 4.3](https://algs4.cs.princeton.edu/43mst) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class EdgeWeightedGraph {
    private val V: Int
    private var E = 0
    private var adj: Array<Bag<Edge>>

    /**
     * Initializes an empty edge-weighted main.java.algs.graph with `V` vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if `V < 0`
     */
    constructor(V: Int) {
        require(V >= 0) { "Number of vertices must be nonnegative" }
        this.V = V
        E = 0
        adj = arrayOfNulls<Bag<*>>(V) as Array<Bag<Edge>>
        for (v in 0 until V) {
            adj[v] = Bag()
        }
    }

    /**
     * Initializes a random edge-weighted main.java.algs.graph with `V` vertices and *E* edges.
     *
     * @param  V the number of vertices
     * @param  E the number of edges
     * @throws IllegalArgumentException if `V < 0`
     * @throws IllegalArgumentException if `E < 0`
     */
    constructor(V: Int, E: Int) : this(V) {
        require(E >= 0) { "Number of edges must be nonnegative" }
        for (i in 0 until E) {
            val v = StdRandom.uniform(V)
            val w = StdRandom.uniform(V)
            val weight = Math.round(100 * StdRandom.uniform()) / 100.0
            val e = Edge(v, w, weight)
            addEdge(e)
        }
    }

    /**
     * Initializes an edge-weighted main.java.algs.graph from an input stream.
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
            adj = arrayOfNulls<Bag<*>>(V) as Array<Bag<Edge>>
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
                val e = Edge(v, w, weight)
                addEdge(e)
            }
        } catch (e: NoSuchElementException) {
            throw IllegalArgumentException("invalid input format in EdgeWeightedGraph constructor", e)
        }
    }

    /**
     * Initializes a new edge-weighted main.java.algs.graph that is a deep copy of `G`.
     *
     * @param  G the edge-weighted main.java.algs.graph to copy
     */
    constructor(G: EdgeWeightedGraph) : this(G.V()) {
        E = G.E()
        for (v in 0 until G.V()) {
            // reverse so that adjacency list is in same order as original
            val reverse = Stack<Edge>()
            for (e in G.adj[v]) {
                reverse.push(e!!)
            }
            for (e in reverse) {
                adj[v].add(e!!)
            }
        }
    }

    /**
     * Returns the number of vertices in this edge-weighted main.java.algs.graph.
     *
     * @return the number of vertices in this edge-weighted main.java.algs.graph
     */
    fun V(): Int {
        return V
    }

    /**
     * Returns the number of edges in this edge-weighted main.java.algs.graph.
     *
     * @return the number of edges in this edge-weighted main.java.algs.graph
     */
    fun E(): Int {
        return E
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        require(!(v < 0 || v >= V)) { "vertex " + v + " is not between 0 and " + (V - 1) }
    }

    /**
     * Adds the undirected edge `e` to this edge-weighted main.java.algs.graph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless both endpoints are between `0` and `V-1`
     */
    fun addEdge(e: Edge) {
        val v = e.either()
        val w = e.other(v)
        validateVertex(v)
        validateVertex(w)
        adj[v].add(e)
        adj[w].add(e)
        E++
    }

    /**
     * Returns the edges incident on vertex `v`.
     *
     * @param  v the vertex
     * @return the edges incident on vertex `v` as an Iterable
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun adj(v: Int): Iterable<Edge> {
        validateVertex(v)
        return adj[v]
    }

    /**
     * Returns the degree of vertex `v`.
     *
     * @param  v the vertex
     * @return the degree of vertex `v`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun degree(v: Int): Int {
        validateVertex(v)
        return adj[v].size()
    }

    /**
     * Returns all edges in this edge-weighted main.java.algs.graph.
     * To iterate over the edges in this edge-weighted main.java.algs.graph, use foreach notation:
     * `for (Edge e : G.edges())`.
     *
     * @return all edges in this edge-weighted main.java.algs.graph, as an iterable
     */
    fun edges(): Iterable<Edge> {
        val list = Bag<Edge>()
        for (v in 0 until V) {
            var selfLoops = 0
            for (e in adj(v)) {
                if (e.other(v) > v) {
                    list.add(e)
                } else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e)
                    selfLoops++
                }
            }
        }
        return list
    }

    /**
     * Returns a string representation of the edge-weighted main.java.algs.graph.
     * This method takes time proportional to *E* + *V*.
     *
     * @return the number of vertices *V*, followed by the number of edges *E*,
     * followed by the *V* adjacency lists of edges
     */
    override fun toString(): String {
        val s = StringBuilder()
        s.append("$V $E$NEWLINE")
        for (v in 0 until V) {
            s.append("$v: ")
            for (e in adj[v]) {
                s.append("$e  ")
            }
            s.append(NEWLINE)
        }
        return s.toString()
    }

    companion object {
        private val NEWLINE = System.getProperty("line.separator")

        /**
         * Unit tests the `EdgeWeightedGraph` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = EdgeWeightedGraph(`in`)
            StdOut.println(G)
        }
    }
}