package algs.graph

import algs.shared.util.Testable
import java.util.*

/**
 * Best, Average, Worst: O(V+E)
 */
object DepthFirstSearch : Testable {
    private val adjVertices: MutableMap<Int, MutableList<Int>> = HashMap()

    fun addVertex(vertex: Int) {
        adjVertices.putIfAbsent(vertex, ArrayList())
    }

    fun addEdge(src: Int, dest: Int) {
        adjVertices[src]!!.add(dest)
    }

    fun dfs(start: Int) {
        val isVisited = BooleanArray(adjVertices.size)
        dfsRecursive(start, isVisited)
    }

    private fun dfsRecursive(current: Int, isVisited: BooleanArray) {
        isVisited[current] = true
        visit(current)
        for (dest in adjVertices[current]!!) {
            if (!isVisited[dest]) dfsRecursive(dest, isVisited)
        }
    }

    fun dfsWithoutRecursion(start: Int) {
        val stack = Stack<Int>()
        val isVisited = BooleanArray(adjVertices.size)
        stack.push(start)
        while (!stack.isEmpty()) {
            val current = stack.pop()
            isVisited[current] = true
            visit(current)
            for (dest in adjVertices[current]!!) {
                if (!isVisited[dest]) stack.push(dest)
            }
        }
    }

    fun topologicalSort(start: Int): List<Int> {
        val result = LinkedList<Int>()
        val isVisited = BooleanArray(adjVertices.size)
        topologicalSortRecursive(start, isVisited, result)
        return result
    }

    private fun topologicalSortRecursive(current: Int, isVisited: BooleanArray, result: LinkedList<Int>) {
        isVisited[current] = true
        for (dest in adjVertices[current]!!) {
            if (!isVisited[dest]) topologicalSortRecursive(dest, isVisited, result)
        }
        result.addFirst(current)
    }

    private fun visit(value: Int) {
        print(" $value")
    }

    override fun test() {
        for (i in 0..3) addVertex(i)
        addEdge(0, 1)
        addEdge(0, 2)
        addEdge(1, 2)
        addEdge(2, 0)
        addEdge(2, 3)
        addEdge(3, 3)
        dfs(2)
        println()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        test()
    }
}