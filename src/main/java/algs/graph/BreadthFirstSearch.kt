package algs.graph

import algs.shared.util.Testable
import java.util.*

/**
 * Best, Average, Worst: O(V+E)
 */
class BreadthFirstSearch : Testable {
    private val V = 4
    private val adj: Array<LinkedList<Int>> = emptyArray()
    private fun addEdge(src: Int, dest: Int) {
        adj[src].add(dest)
    }

    fun bfs(current: Int) {
        var current = current
        val visited = BooleanArray(V)
        val queue = LinkedList<Int>()
        visited[current] = true
        queue.add(current)
        while (!queue.isEmpty()) {
            current = queue.poll()
            print("$current ")
            for (n in adj[current]) {
                if (!visited[n]) {
                    visited[n] = true
                    queue.add(n)
                }
            }
        }
    }

    override fun test() {
        addEdge(0, 1)
        addEdge(0, 2)
        addEdge(1, 2)
        addEdge(2, 0)
        addEdge(2, 3)
        addEdge(3, 3)
        bfs(2)
        println()
    }

    init {
        for (i in 0 until V) adj[i] = LinkedList()
    }
}