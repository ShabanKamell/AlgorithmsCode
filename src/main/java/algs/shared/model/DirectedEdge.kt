/******************************************************************************
 * Compilation:  javac DirectedEdge.java
 * Execution:    java DirectedEdge
 * Dependencies: StdOut.java
 *
 * Immutable weighted directed edge.
 *
 */
package algs.shared.model

import algs.shared.util.StdOut

/**
 * The `DirectedEdge` class represents a weighted edge in an
 * [EdgeWeightedDigraph]. Each edge consists of two integers
 * (naming the two vertices) and a real-value weight. The data type
 * provides methods for accessing the two endpoints of the directed edge and
 * the weight.
 *
 *
 * For additional documentation, see [Section 4.4](https://algs4.cs.princeton.edu/44sp) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class DirectedEdge(v: Int, w: Int, weight: Double) {
    private val v: Int
    private val w: Int
    private val weight: Double

    /**
     * Returns the tail vertex of the directed edge.
     * @return the tail vertex of the directed edge
     */
    fun from(): Int {
        return v
    }

    /**
     * Returns the head vertex of the directed edge.
     * @return the head vertex of the directed edge
     */
    fun to(): Int {
        return w
    }

    /**
     * Returns the weight of the directed edge.
     * @return the weight of the directed edge
     */
    fun weight(): Double {
        return weight
    }

    /**
     * Returns a string representation of the directed edge.
     * @return a string representation of the directed edge
     */
    override fun toString(): String {
        return v.toString() + "->" + w + " " + String.format("%5.2f", weight)
    }

    companion object {
        /**
         * Unit tests the `DirectedEdge` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val e = DirectedEdge(12, 34, 5.67)
            StdOut.println(e)
        }
    }

    /**
     * Initializes a directed edge from vertex `v` to vertex `w` with
     * the given `weight`.
     * @param v the tail vertex
     * @param w the head vertex
     * @param weight the weight of the directed edge
     * @throws IllegalArgumentException if either `v` or `w`
     * is a negative integer
     * @throws IllegalArgumentException if `weight` is `NaN`
     */
    init {
        require(v >= 0) { "Vertex names must be nonnegative integers" }
        require(w >= 0) { "Vertex names must be nonnegative integers" }
        require(!java.lang.Double.isNaN(weight)) { "Weight is NaN" }
        this.v = v
        this.w = w
        this.weight = weight
    }
}