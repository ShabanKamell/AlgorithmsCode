package algs.shared.model;

public class Node<T> {
    public T key;
    public int height = -1;
    public Node<T> left;
    public Node<T> right;

    public Node(T item) {
        key = item;
        left = right = null;
    }
}
