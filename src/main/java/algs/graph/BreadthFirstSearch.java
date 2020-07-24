package algs.graph;

import algs.shared.util.Testable;

import java.util.LinkedList;

/**
 * Best, Average, Worst: O(V+E)
 */
@SuppressWarnings("unchecked")
public class BreadthFirstSearch implements Testable {

    private final int V;
    private final LinkedList<Integer>[] adj;

    public BreadthFirstSearch() {
        V = 4;
        adj = new LinkedList[V];
        for (int i = 0; i < V; ++i)
            adj[i] = new LinkedList<>();
    }

    public void addEdge(int src, int dest) {
        adj[src].add(dest);
    }

    void bfs(int current) {
        boolean[] visited = new boolean[V];

        LinkedList<Integer> queue = new LinkedList<>();

        visited[current] = true;
        queue.add(current);

        while (!queue.isEmpty()) {
            current = queue.poll();
            System.out.print(current + " ");

            for (int n : adj[current]) {
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
    }

    @Override
    public void test() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(3, 3);

        bfs(2);

        System.out.println();
    }

}
