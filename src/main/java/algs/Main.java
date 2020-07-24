package algs;

import algs.graph.BreadthFirstSearch;
import algs.graph.DepthFirstSearch;
import algs.graph.DijkstraSP;
import algs.search.BinarySearch;
import algs.search.LinearSearch;
import algs.search.bst.AvlTree;
import algs.search.bst.BinarySearchTree;
import algs.sort.*;
import algs.shared.util.Testable;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        algorithms().forEach(item -> {
            String name = item.getClass().getSimpleName();
            System.out.println();
            System.out.println(">>> " + name);
            item.test();
            System.out.println("<<< " + name);
            System.out.println();
        });
    }

    private static List<Testable> sortAlgorithms() {
        List<Testable> list = new ArrayList<>();
        list.add(new InsertionSort());
        list.add(new SelectionSort());
        list.add(new HeapSort());
        list.add(new QuickSort());
        list.add(new MergeSort());
        return list;
    }

    private static List<Testable> searchAlgorithms() {
        List<Testable> list = new ArrayList<>();
        list.add(new LinearSearch());
        list.add(new BinarySearch());
        list.add(new BinarySearchTree());
        list.add(new AvlTree());
        return list;
    }

    private static List<Testable> graphAlgorithms() {
        List<Testable> list = new ArrayList<>();
        list.add(new DepthFirstSearch());
        list.add(new BreadthFirstSearch());
        list.add(new DijkstraSP());
        return list;
    }

    private static List<Testable> algorithms() {
        List<Testable> list = new ArrayList<>();
        list.addAll(sortAlgorithms());
        list.addAll(searchAlgorithms());
        list.addAll(graphAlgorithms());
        return list;
    }

}
