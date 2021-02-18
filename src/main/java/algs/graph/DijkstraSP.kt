/******************************************************************************
 * Compilation:  javac DijkstraSP.java
 * Execution:    java DijkstraSP input.txt s
 * Dependencies: EdgeWeightedDigraph.java IndexMinPQ.java Stack.java DirectedEdge.java
 * Data files:   https://algs4.cs.princeton.edu/44sp/tinyEWD.txt
 * https://algs4.cs.princeton.edu/44sp/mediumEWD.txt
 * https://algs4.cs.princeton.edu/44sp/largeEWD.txt
 *
 * Dijkstra's algorithm. Computes the shortest path tree.
 * Assumes all weights are nonnegative.
 *
 * % java DijkstraSP tinyEWD.txt 0
 * 0 to 0 (0.00)
 * 0 to 1 (1.05)  0->4  0.38   4->5  0.35   5->1  0.32
 * 0 to 2 (0.26)  0->2  0.26
 * 0 to 3 (0.99)  0->2  0.26   2->7  0.34   7->3  0.39
 * 0 to 4 (0.38)  0->4  0.38
 * 0 to 5 (0.73)  0->4  0.38   4->5  0.35
 * 0 to 6 (1.51)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52
 * 0 to 7 (0.60)  0->2  0.26   2->7  0.34
 *
 * % java DijkstraSP mediumEWD.txt 0
 * 0 to 0 (0.00)
 * 0 to 1 (0.71)  0->44  0.06   44->93  0.07   ...  107->1  0.07
 * 0 to 2 (0.65)  0->44  0.06   44->231  0.10  ...  42->2  0.11
 * 0 to 3 (0.46)  0->97  0.08   97->248  0.09  ...  45->3  0.12
 * 0 to 4 (0.42)  0->44  0.06   44->93  0.07   ...  77->4  0.11
 * ...
 *
 */
package algs.graph

import algs.shared.datastructure.IndexMinPQ
import algs.shared.datastructure.Stack
import algs.shared.model.DirectedEdge
import algs.shared.model.EdgeWeightedDigraph
import algs.shared.util.In
import algs.shared.util.StdOut
import algs.shared.util.Testable
import java.io.File
import java.lang.Double.POSITIVE_INFINITY

/**
 * The `DijkstraSP` class represents a data type for solving the
 * single-source shortest paths problem in edge-weighted digraphs
 * where the edge weights are nonnegative.
 *
 *
 * This implementation uses *Dijkstra's algorithm* with a
 * *binary heap*. The constructor takes
 * (*E* log *V*) time in the worst case,
 * where *V* is the number of vertices and *E* is
 * the number of edges. Each instance method takes (1) time.
 * It uses (*V*) extra space (not including the
 * edge-weighted digraph).
 *
 *
 * For additional documentation,
 * see [Section 4.4](https://algs4.cs.princeton.edu/44sp) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * Best, Average, Worst: O((V+E)*log V)
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class DijkstraSP : Testable {
    // distTo[v] = distance  of shortest s->v path
    private var distTo: DoubleArray = DoubleArray(0)
    // edgeTo[v] = last edge on shortest s->v path
    private var edgeTo: Array<DirectedEdge?> = emptyArray()
    private var pq // priority queue of vertices
            : IndexMinPQ<Double>? = null

    /**
     * This constructor is used only for test purposes
     */
    constructor() {}

    /**
     * Computes a shortest-paths tree from the source vertex `s` to every other
     * vertex in the edge-weighted digraph `G`.
     *
     * @param G the edge-weighted digraph
     * @param s the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless `0 <= s < V`
     */
    constructor(G: EdgeWeightedDigraph, s: Int) {
        setup(G, s)
    }

    private fun setup(G: EdgeWeightedDigraph, s: Int) {
        for (e in G.edges()) {
            require(e.weight() >= 0) { "edge $e has negative weight" }
        }
        val V = G.V()
        distTo = DoubleArray(V)
        edgeTo = arrayOfNulls<DirectedEdge>(V)
        validateVertex(s)
        for (v in 0 until V) distTo[v] = POSITIVE_INFINITY
        distTo[s] = 0.0

        // relax vertices in order of distance from s
        pq = IndexMinPQ(V)
        pq!!.insert(s, distTo[s])
        while (!pq!!.isEmpty) {
            val v = pq!!.delMin()
            for (e in G.adj(v)) relax(e)
        }
        assert(check(G, s))
    }

    // relax edge e and update pq if changed
    private fun relax(e: DirectedEdge) {
        val v = e.from()
        val w = e.to()
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight()
            edgeTo[w] = e
            if (pq!!.contains(w)) pq!!.decreaseKey(w, distTo[w]) else pq!!.insert(w, distTo[w])
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex `s` to vertex `v`.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex `s` to vertex `v`;
     * `Double.POSITIVE_INFINITY` if no such path
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun distTo(v: Int): Double {
        validateVertex(v)
        return distTo[v]
    }

    /**
     * Returns true if there is a path from the source vertex `s` to vertex `v`.
     *
     * @param  v the destination vertex
     * @return `true` if there is a path from the source vertex
     * `s` to vertex `v`; `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun hasPathTo(v: Int): Boolean {
        validateVertex(v)
        return distTo[v] < POSITIVE_INFINITY
    }

    /**
     * Returns a shortest path from the source vertex `s` to vertex `v`.
     *
     * @param  v the destination vertex
     * @return a shortest path from the source vertex `s` to vertex `v`
     * as an iterable of edges, and `null` if no such path
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun pathTo(v: Int): Iterable<DirectedEdge>? {
        validateVertex(v)
        if (!hasPathTo(v)) return null
        val path = Stack<DirectedEdge>()
        var e = edgeTo[v]
        while (e != null) {
            path.push(e)
            e = edgeTo[e.from()]
        }
        return path
    }

    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private fun check(G: EdgeWeightedDigraph, s: Int): Boolean {
        // check that edge weights are nonnegative
        for (e in G.edges()) {
            if (e!!.weight() < 0) {
                System.err.println("negative edge weight detected")
                return false
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent")
            return false
        }
        for (v in 0 until G.V()) {
            if (v == s) continue
            if (edgeTo[v] == null && distTo[v] != POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent")
                return false
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (v in 0 until G.V()) {
            for (e in G.adj(v)) {
                val w = e!!.to()
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("edge $e not relaxed")
                    return false
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (w in 0 until G.V()) {
            if (edgeTo[w] == null) continue
            val e = edgeTo[w]
            val v = e!!.from()
            if (w != e.to()) return false
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge $e on shortest path not tight")
                return false
            }
        }
        return true
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = distTo.size
        require(!(v < 0 || v >= V)) { "vertex " + v + " is not between 0 and " + (V - 1) }
    }

    override fun test() {
        val file = File(javaClass.classLoader.getResource("tinyEWD.txt").file)
        val `in` = In(file)
        val G = EdgeWeightedDigraph(`in`)
        val s = 0
        setup(G, s)

        // compute shortest paths

        // print shortest path
        for (t in 0 until G.V()) {
            if (hasPathTo(t)) {
                StdOut.printf("%d to %d (%.2f)  ", s, t, distTo(t))
                for (e in pathTo(t)!!) {
                    StdOut.print("$e   ")
                }
                StdOut.println()
            } else {
                StdOut.printf("%d to %d         no path\n", s, t)
            }
        }
    }
}