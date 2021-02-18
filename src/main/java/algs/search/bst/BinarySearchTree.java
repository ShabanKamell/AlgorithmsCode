package algs.search.bst;

import algs.shared.model.Node;
import algs.shared.util.Testable;

/**
 * Best: O(1) Average, Worst: O(log n)
 */
public class BinarySearchTree implements Testable {
    // Root of BST
    private Node<Integer> root;

    /**
      Create the following BST
              50
           /     \
          30      70
         /  \    /  \
       20   40  60   80
     */
    private void createTree() {
        insert(50);
        insert(30);
        insert(20);
        insert(40);
        insert(70);
        insert(60);
        insert(80);

        // print inorder traversal of the BST
        inorder(root);
    }

    private void testDelete() {
        System.out.println("Inorder traversal of the given tree");
        inorder();

        /*
          50                            50
           /     \         delete(20)      /   \
          30      70       --------->    30     70
         /  \    /  \                     \    /  \
       20   40  60   80                   40  60   80
         */
        System.out.println("\nDelete 20");
        delete(20);
        System.out.println("Inorder traversal of the modified tree");
        inorder();

        /*
              50                            50
           /     \         delete(30)      /   \
          30      70       --------->    40     70
            \    /  \                          /  \
            40  60   80                       60   80
         */
        System.out.println("\nDelete 30");
        delete(30);
        System.out.println("Inorder traversal of the modified tree");
        inorder();

        /*
              50                            60
           /     \         delete(50)      /   \
          40      70       --------->    40    70
                 /  \                            \
                60   80                           80
         */
        System.out.println("\nDelete 50");
        delete(50);
        System.out.println("Inorder traversal of the modified tree");
        inorder();
    }

    private void insert(int key) {
        root = insert(root, key);
    }

    /**
     * A recursive function to insert a new key in BST
     */
    private Node<Integer> insert(Node<Integer> root, int key) {
        // If the tree is empty, return a new node
        if (root == null) {
            root = new Node<Integer>(key);
            return root;
        }

        // Otherwise, recur down the tree
        if (key < root.key)
            root.left = insert(root.left, key);
        else if (key > root.key)
            root.right = insert(root.right, key);

        // return the (unchanged) node pointer
        return root;
    }

    /**
     * A utility function to algs.search a given key in BST
     */
    private Node<Integer> search(Node<Integer> root, int key) {
        // Base Cases: root is null or key is present at root
        if (root == null || root.key == key)
            return root;

        // val is greater than root's key
        if (root.key > key)
            return search(root.left, key);

        // val is less than root's key
        return search(root.right, key);
    }

   private void inorder() {
        inorder(root);
   }

   private void inorder(Node<Integer> root) {
        if (root != null) {
            inorder(root.left);
            System.out.print(root.key + " ");
            inorder(root.right);
        }
    }

    // This method mainly calls deleteRec()
    void delete(int key) {
        root = delete(root, key);
    }

    /**
     * A recursive function to insert a new key in BST.
     */
    Node<Integer> delete(Node<Integer> root, int key) {
        // Base Case: If the tree is empty
        if (root == null)  return null;

        // Recur down the tree
        if (key < root.key)
            root.left = delete(root.left, key);
        else if (key > root.key)
            root.right = delete(root.right, key);
        else {
            // If key is same as root's key, then This is the node
            // to be deleted

            // Node<Integer> with only one child or no child
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            // Node<Integer> with two children:

            // 1- Get the inorder successor (smallest in the right subtree).
            // 2- Copy inorder successor to current node.
            root.key = minValue(root.right);

            // 3- Delete the inorder successor
            root.right = delete(root.right, root.key);
        }

        return root;
    }

   private int minValue(Node<Integer> root) {
        int min = root.key;
        while (root.left != null) {
            min = root.left.key;
            root = root.left;
        }
        return min;
    }

    private boolean isBST()  {
        return isBST(root, null);
    }

    /* Returns true if given algs.search tree is binary
       algs.search tree (efficient version) */
    private boolean isBST(Node<Integer> node, Node<Integer> prev) {
        // traverse the tree in inorder fashion and
        // keep a track of previous node
        if (node == null)
            return true;

        if (!isBST(node.left, prev))
            return false;

        // allows only distinct values node
        if (prev != null && node.key <= prev.key )
            return false;

        return isBST(node.right, node);
    }

    @Override
    public void test() {
        createTree();

        if (isBST())
            System.out.println("IS BST");
        else
            System.out.println("Not a BST");

        Node<Integer> result = search(root, 60);

        if (result == null)
            System.out.println("Element not present");
        else
            System.out.println("Element found: " + result.key);

        testDelete();

        System.out.println();
    }

}
