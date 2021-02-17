package algs.shared.model

class Node(var key: Int) {
    var height = -1
    var left: Node?
    var right: Node? = null

    init {
        left = right
    }
}