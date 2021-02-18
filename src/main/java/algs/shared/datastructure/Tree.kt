package algs.shared.datastructure

import java.util.*

class Tree<T> private constructor(var value: T) {
    var children: MutableList<Tree<T>> = ArrayList()

    fun addChild(value: T): Tree<T> {
        val newChild = Tree(value)
        children.add(newChild)
        return newChild
    }

    companion object {
        fun <T> of(value: T): Tree<T> {
            return Tree(value)
        }
    }

}