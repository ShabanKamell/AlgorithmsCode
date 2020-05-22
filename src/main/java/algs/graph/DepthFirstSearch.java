package algs.graph;

import algs.util.Testable;

import java.util.*;

/**
 * Best, Average, Worst: O(V+E)
 */
public class DepthFirstSearch implements Testable {

    private final Map<Integer, List<Integer>> adjVertices = new HashMap<>();

    public void addVertex(int vertex) {
        adjVertices.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(int src, int dest) {
        adjVertices.get(src).add(dest);
    }

    public void dfs(int start) {
        boolean[] isVisited = new boolean[adjVertices.size()];
        dfsRecursive(start, isVisited);
    }

    private void dfsRecursive(int current, boolean[] isVisited) {
        isVisited[current] = true;
        visit(current);
        for (int dest : adjVertices.get(current)) {
            if (!isVisited[dest])
                dfsRecursive(dest, isVisited);
        }
    }

    public void dfsWithoutRecursion(int start) {
        Stack<Integer> stack = new Stack<>();
        boolean[] isVisited = new boolean[adjVertices.size()];
        stack.push(start);
        while (!stack.isEmpty()) {
            int current = stack.pop();
            isVisited[current] = true;
            visit(current);
            for (int dest : adjVertices.get(current)) {
                if (!isVisited[dest])
                    stack.push(dest);
            }
        }
    }

    public List<Integer> topologicalSort(int start) {
        LinkedList<Integer> result = new LinkedList<>();
        boolean[] isVisited = new boolean[adjVertices.size()];
        topologicalSortRecursive(start, isVisited, result);
        return result;
    }

    private void topologicalSortRecursive(int current, boolean[] isVisited, LinkedList<Integer> result) {
        isVisited[current] = true;
        for (int dest : adjVertices.get(current)) {
            if (!isVisited[dest])
                topologicalSortRecursive(dest, isVisited, result);
        }
        result.addFirst(current);
    }

    private void visit(int value) {
        System.out.print(" " + value);
    }

    @Override
    public void test() {
        for (int i = 0 ; i < 4 ; i++)
            addVertex(i);

        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(3, 3);

        dfs(2);

        System.out.println();
    }

}
