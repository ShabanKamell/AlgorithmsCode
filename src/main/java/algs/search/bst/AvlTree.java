package algs.search.bst;

import algs.shared.model.Node;
import algs.shared.util.Testable;

/**
 * Best: O(1) Average, Worst: O(log n)
 */
public class AvlTree implements Testable {
    // Root of BST
    private Node<Integer> root;

    public Node<Integer> search(int key) {
        Node<Integer> current = root;
        while (current != null) {
            if (current.key == key) {
                break;
            }
            current = current.key < key ? current.right : current.left;
        }
        return current;
    }

    public void insert(int key) {
        root = insert(root, key);
    }

    public void delete(int key) {
        root = delete(root, key);
    }

    public Node<Integer> getRoot() {
        return root;
    }

    public int height() {
        return root == null ? -1 : root.height;
    }

    private Node<Integer> insert(Node<Integer> node, int key) {
        if (node == null)
            return new Node<Integer>(key);
        else if (node.key > key)
            node.left = insert(node.left, key);
        else if (node.key < key)
            node.right = insert(node.right, key);

        else throw new IllegalArgumentException("duplicate Key!");

        return reBalance(node);
    }

    private Node<Integer> delete(Node<Integer> node, int key) {
        if (node == null)
            return null;
        else if (node.key > key) {
            node.left = delete(node.left, key);
        } else if (node.key < key) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left == null) ? node.right : node.left;
            } else {
                Node<Integer> mostLeftChild = mostLeftChild(node.right);
                node.key = mostLeftChild.key;
                node.right = delete(node.right, node.key);
            }
        }

        if (node != null)
            node = reBalance(node);

        return node;
    }

    private Node<Integer> mostLeftChild(Node<Integer> node) {
        Node<Integer> current = node;
        /* loop down to find the leftmost leaf */
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private Node<Integer> reBalance(Node<Integer> z) {
        updateHeight(z);
        int balance = getBalance(z);
        if (balance > 1) {
            // Right Right
            if (height(z.right.right) > height(z.right.left)) {
                z = rotateLeft(z);
            }
            // Right Left
            else {
                z.right = rotateRight(z.right);
                z = rotateLeft(z);
            }
        }
        else if (balance < -1) {
            // Left Left Case
            if (height(z.left.left) > height(z.left.right)) {
                z = rotateRight(z);
            }
            // Left Right Case
            else {
                z.left = rotateLeft(z.left);
                z = rotateRight(z);
            }
        }
        return z;
    }

    /**
          y              x
         /                \
        x    --------->    y
         \                /
          z              z
    */
    private Node<Integer> rotateRight(Node<Integer> y) {
        Node<Integer> x = y.left;
        Node<Integer> z = x.right;

        // Rotate
        x.right = y;
        y.left = z;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    /**
        y                     x
         \                   /
          x    --------->   y
         /                   \
        z                     z
     */
    private Node<Integer> rotateLeft(Node<Integer> y) {
        Node<Integer> x = y.right;
        Node<Integer> z = x.left;

        // Rotate
        x.left = y;
        y.right = z;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private void updateHeight(Node<Integer> n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int height(Node<Integer> n) {
        return n == null ? -1 : n.height;
    }

    public int getBalance(Node<Integer> n) {
        return (n == null) ? 0 : height(n.right) - height(n.left);
    }

    // A utility function to print preorder traversal
    // of the tree.
    // The function also prints height of every node
    void preOrder(Node<Integer> node) {
        if (node != null) {
            System.out.print(node.key + " ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    private void inorder(Node<Integer> root) {
        if (root != null) {
            inorder(root.left);
            System.out.print(root.key + " ");
            inorder(root.right);
        }
    }

    @Override
    public void test() {
        /* Constructing tree given in the above figure */
        root = insert(root, 10);
        root = insert(root, 50);
        root = insert(root, 40);
        root = insert(root, 25);
        root = insert(root, 30);
        root = insert(root, 20);

        /* The constructed AVL Tree would be
             30
            /  \
          20   40
         /  \     \
        10  25    50
        */
        System.out.println("Preorder traversal of constructed tree is: ");

        preOrder(root);

        System.out.println();

        System.out.println("inorder traversal of constructed tree is: ");
        inorder(root);

        System.out.println();

        delete(20);
        delete(40);
        System.out.println("Tree after deleting 20 & 40: ");
        inorder(root);
        System.out.println();
    }
}
