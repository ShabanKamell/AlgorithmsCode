/******************************************************************************
 * Compilation:  javac Edge.java
 * Execution:    java Edge
 * Dependencies: StdOut.java
 *
 * Immutable weighted edge.
 *
 */
package algs.shared.model

import algs.shared.util.StdOut

/**
 * The `Edge` class represents a weighted edge in an
 * [EdgeWeightedGraph]. Each edge consists of two integers
 * (naming the two vertices) and a real-value weight. The data type
 * provides methods for accessing the two endpoints of the edge and
 * the weight. The natural order for this data type is by
 * ascending order of weight.
 *
 *
 * For additional documentation, see [Section 4.3](https://algs4.cs.princeton.edu/43mst) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class Edge(v: Int, w: Int, weight: Double) : Comparable<Edge> {
    private val v: Int
    private val w: Int
    private val weight: Double

    /**
     * Returns the weight of this edge.
     *
     * @return the weight of this edge
     */
    fun weight(): Double {
        return weight
    }

    /**
     * Returns either endpoint of this edge.
     *
     * @return either endpoint of this edge
     */
    fun either(): Int {
        return v
    }

    /**
     * Returns the endpoint of this edge that is different from the given vertex.
     *
     * @param  vertex one endpoint of this edge
     * @return the other endpoint of this edge
     * @throws IllegalArgumentException if the vertex is not one of the
     * endpoints of this edge
     */
    fun other(vertex: Int): Int {
        return if (vertex == v) w else if (vertex == w) v else throw IllegalArgumentException("Illegal endpoint")
    }

    /**
     * Compares two edges by weight.
     * Note that `compareTo()` is not consistent with `equals()`,
     * which uses the reference equality implementation inherited from `Object`.
     *
     * @param  that the other edge
     * @return a negative integer, zero, or positive integer depending on whether
     * the weight of this is less than, equal to, or greater than the
     * argument edge
     */
    override fun compareTo(that: Edge): Int {
        return java.lang.Double.compare(weight, that.weight)
    }

    /**
     * Returns a string representation of this edge.
     *
     * @return a string representation of this edge
     */
    override fun toString(): String {
        return String.format("%d-%d %.5f", v, w, weight)
    }

    companion object {
        /**
         * Unit tests the `Edge` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val e = Edge(12, 34, 5.67)
            StdOut.println(e)
        }
    }

    /**
     * Initializes an edge between vertices `v` and `w` of
     * the given `weight`.
     *
     * @param  v one vertex
     * @param  w the other vertex
     * @param  weight the weight of this edge
     * @throws IllegalArgumentException if either `v` or `w`
     * is a negative integer
     * @throws IllegalArgumentException if `weight` is `NaN`
     */
    init {
        require(v >= 0) { "vertex index must be a nonnegative integer" }
        require(w >= 0) { "vertex index must be a nonnegative integer" }
        require(!java.lang.Double.isNaN(weight)) { "Weight is NaN" }
        this.v = v
        this.w = w
        this.weight = weight
    }
}