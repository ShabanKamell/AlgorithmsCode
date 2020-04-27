import sort.*;
import util.Testable;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Testable> algorithms() {
        List<Testable> list = new ArrayList<>();
        list.add(new InsertionSort());
        list.add(new SelectionSort());
        list.add(new HeapSort());
        list.add(new QuickSort());
        list.add(new MergeSort());
        return list;
    }

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

}
