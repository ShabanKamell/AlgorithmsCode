package algs

import algs.graph.BreadthFirstSearch
import algs.graph.DepthFirstSearch
import algs.graph.DijkstraSP
import algs.search.BinarySearch
import algs.search.LinearSearch
import algs.search.bst.AvlTree
import algs.search.bst.BinarySearchTree
import algs.shared.util.Testable
import algs.sort.*
import java.util.*

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        algorithms().forEach { item ->
            val name = item.javaClass.simpleName
            println()
            println(">>> $name")
            item.test()
            println("<<< $name")
            println()
        }
    }

    private fun sortAlgorithms(): List<Testable> {
        val list: MutableList<Testable> = ArrayList()
        list.add(InsertionSort())
        list.add(SelectionSort())
        list.add(HeapSort())
        list.add(QuickSort())
        list.add(MergeSort())
        return list
    }

    private fun searchAlgorithms(): List<Testable> {
        val list: MutableList<Testable> = ArrayList()
        list.add(LinearSearch())
        list.add(BinarySearch())
        list.add(BinarySearchTree())
        list.add(AvlTree())
        return list
    }

    private fun graphAlgorithms(): List<Testable> {
        val list: MutableList<Testable> = ArrayList()
        list.add(DepthFirstSearch())
        list.add(BreadthFirstSearch())
        list.add(DijkstraSP())
        return list
    }

    private fun algorithms(): List<Testable> {
        val list: MutableList<Testable> = ArrayList()
        list.addAll(sortAlgorithms())
        list.addAll(searchAlgorithms())
        list.addAll(graphAlgorithms())
        return list
    }
}