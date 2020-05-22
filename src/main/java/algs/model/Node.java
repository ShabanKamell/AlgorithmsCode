package algs.model;

public class Node {
    public int key;
    public int height = -1;
    public Node left;
    public Node right;

    public Node(int item) {
        key = item;
        left = right = null;
    }
}
