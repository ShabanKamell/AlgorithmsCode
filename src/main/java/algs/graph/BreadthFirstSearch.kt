package algs.graph

import algs.shared.datastructure.Tree
import algs.shared.util.Testable
import java.util.*


/**
 * BFS
 * https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
 * Best, Average, Worst: O(V+E)
 */
object BreadthFirstSearch : Testable {

    fun <T> search(value: T, root: Tree<T>): Tree<T>? {
        val queue: Queue<Tree<T>> = ArrayDeque()
        queue.add(root)
        var currentNode: Tree<T>

        while (!queue.isEmpty()) {
            currentNode = queue.remove()
            println("Visited node with value: ${currentNode.value}")
            if (currentNode.value == value) {
                return currentNode
            }
            queue.addAll(currentNode.children)
        }

        return null
    }

    fun <T> search(value: T, start: Node<T>): Node<T>? {
        val queue: Queue<Node<T>> = ArrayDeque()
        queue.add(start)
        var currentNode: Node<T>
        val visited: MutableSet<Node<T>> = HashSet<Node<T>>()

        while (!queue.isEmpty()) {
            currentNode = queue.remove()
            println("Visited node with value: ${currentNode.value}")
            if (currentNode.value == value) {
                return currentNode
            }
            visited.add(currentNode)
            queue.addAll(currentNode.neighbors)
            queue.removeAll(visited)
        }

        return null
    }

    override fun test() {
        testTree()
        testNode()
    }

    private fun testTree() {
        println("Tree Search")

        val root = Tree.of(10);
        val rootFirstChild = root.addChild(2);
        rootFirstChild.addChild(3);
        root.addChild(4);

        search(5, rootFirstChild)
    }

    private fun testNode() {
        println("Node Search")

        val start = Node(10)
        val firstNeighbor = Node(2)
        start.connect(firstNeighbor)

        val firstNeighborNeighbor = Node(3)
        firstNeighbor.connect(firstNeighborNeighbor)
        firstNeighborNeighbor.connect(start)

        val secondNeighbor = Node(4)
        start.connect(secondNeighbor)

        search(5, secondNeighbor)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        test()
    }

}

class Node<T>(val value: T) {
    val neighbors: MutableSet<Node<T>>

    fun connect(node: Node<T>) {
        require(!(this === node)) { "Can't connect node to itself" }
        neighbors.add(node)
        node.neighbors.add(this)
    }

    init {
        neighbors = HashSet()
    }
}
