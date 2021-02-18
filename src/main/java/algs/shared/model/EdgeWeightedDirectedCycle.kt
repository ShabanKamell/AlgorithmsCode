/******************************************************************************
 * Compilation:  javac EdgeWeightedDirectedCycle.java
 * Execution:    java EdgeWeightedDirectedCycle V E F
 * Dependencies: EdgeWeightedDigraph.java DirectedEdge.java Stack.java
 *
 * Finds a directed cycle in an edge-weighted digraph.
 * Runs in O(E + V) time.
 *
 *
 */
package algs.shared.model

import algs.shared.datastructure.Stack
import algs.shared.util.StdOut
import algs.shared.util.StdRandom

/**
 * The `EdgeWeightedDirectedCycle` class represents a data type for
 * determining whether an edge-weighted digraph has a directed cycle.
 * The *hasCycle* operation determines whether the edge-weighted
 * digraph has a directed cycle and, if so, the *cycle* operation
 * returns one.
 *
 *
 * This implementation uses *depth-first main.java.algs.search*.
 * The constructor takes (*V* + *E*) time in the
 * worst case, where *V* is the number of vertices and
 * *E* is the number of edges.
 * Each instance method takes (1) time.
 * It uses (*V*) extra space (not including the
 * edge-weighted digraph).
 *
 *
 * See [Topological] to compute a topological order if the
 * edge-weighted digraph is acyclic.
 *
 *
 * For additional documentation,
 * see [Section 4.4](https://algs4.cs.princeton.edu/44sp) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class EdgeWeightedDirectedCycle(G: EdgeWeightedDigraph) {
    private val marked // marked[v] = has vertex v been marked?
            : BooleanArray
    private val edgeTo // edgeTo[v] = previous edge on path to v
            : Array<DirectedEdge?>
    private val onStack // onStack[v] = is vertex on the stack?
            : BooleanArray
    private var cycle // directed cycle (or null if no such cycle)
            : Stack<DirectedEdge>? = null

    // check that algorithm computes either the topological order or finds a directed cycle
    private fun dfs(G: EdgeWeightedDigraph, v: Int) {
        onStack[v] = true
        marked[v] = true
        for (e in G.adj(v)) {
            val w = e!!.to()

            // short circuit if directed cycle found
            if (cycle != null) return else if (!marked[w]) {
                edgeTo[w] = e
                dfs(G, w)
            } else if (onStack[w]) {
                cycle = Stack()
                var f = e
                while (f.from() != w) {
                    cycle!!.push(f)
                    f = edgeTo[f.from()]!!
                }
                cycle!!.push(f)
                return
            }
        }
        onStack[v] = false
    }

    /**
     * Does the edge-weighted digraph have a directed cycle?
     * @return `true` if the edge-weighted digraph has a directed cycle,
     * `false` otherwise
     */
    fun hasCycle(): Boolean {
        return cycle != null
    }

    /**
     * Returns a directed cycle if the edge-weighted digraph has a directed cycle,
     * and `null` otherwise.
     * @return a directed cycle (as an iterable) if the edge-weighted digraph
     * has a directed cycle, and `null` otherwise
     */
    fun cycle(): Iterable<DirectedEdge>? {
        return cycle
    }

    // certify that digraph is either acyclic or has a directed cycle
    private fun check(): Boolean {

        // edge-weighted digraph is cyclic
        if (hasCycle()) {
            // verify cycle
            var first: DirectedEdge? = null
            var last: DirectedEdge? = null
            for (e in cycle()!!) {
                if (first == null) first = e
                if (last != null) {
                    if (last.to() != e.from()) {
                        System.err.printf("cycle edges %s and %s not incident\n", last, e)
                        return false
                    }
                }
                last = e
            }
            if (last!!.to() != first!!.from()) {
                System.err.printf("cycle edges %s and %s not incident\n", last, first)
                return false
            }
        }
        return true
    }

    companion object {
        /**
         * Unit tests the `EdgeWeightedDirectedCycle` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {

            // create random DAG with V vertices and E edges; then add F random edges
            val V = args[0].toInt()
            val E = args[1].toInt()
            val F = args[2].toInt()
            val G = EdgeWeightedDigraph(V)
            val vertices = IntArray(V)
            for (i in 0 until V) vertices[i] = i
            StdRandom.shuffle(vertices)
            for (i in 0 until E) {
                var v: Int
                var w: Int
                do {
                    v = StdRandom.uniform(V)
                    w = StdRandom.uniform(V)
                } while (v >= w)
                val weight = StdRandom.uniform()
                G.addEdge(DirectedEdge(v, w, weight))
            }

            // add F extra edges
            for (i in 0 until F) {
                val v = StdRandom.uniform(V)
                val w = StdRandom.uniform(V)
                val weight = StdRandom.uniform(0.0, 1.0)
                G.addEdge(DirectedEdge(v, w, weight))
            }
            StdOut.println(G)

            // find a directed cycle
            val finder = EdgeWeightedDirectedCycle(G)
            if (finder.hasCycle()) {
                StdOut.print("Cycle: ")
                for (e in finder.cycle()!!) {
                    StdOut.print("$e ")
                }
                StdOut.println()
            } else {
                StdOut.println("No directed cycle")
            }
        }
    }

    /**
     * Determines whether the edge-weighted digraph `G` has a directed cycle and,
     * if so, finds such a cycle.
     * @param G the edge-weighted digraph
     */
    init {
        marked = BooleanArray(G.V())
        onStack = BooleanArray(G.V())
        edgeTo = arrayOfNulls(G.V())
        for (v in 0 until G.V()) if (!marked[v]) dfs(G, v)
        assert(check())
    }
}