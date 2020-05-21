package algs.search.bst;

public class Node {
    int key;
    int height = -1;
    Node left;
    Node right;

    Node(int item) {
        key = item;
        left = right = null;
    }
}
