import util.Testable;
import sort.InsertionSort;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Testable> algorithms() {
        List<Testable> list = new ArrayList<>();
        list.add(new InsertionSort());
        return list;
    }

    public static void main(String[] args) {
        algorithms().forEach(Testable::test);
    }

}
