package algs.search.bst

import algs.shared.model.Node
import algs.shared.util.Testable

/**
 * Best: O(1) Average, Worst: O(log n)
 */
class BinarySearchTree : Testable {
    // Root of BST
    private var root: Node? = null

    /**
     * Create the following BST
     * 50
     * /     \
     * 30      70
     * /  \    /  \
     * 20   40  60   80
     */
    private fun createTree() {
        insert(50)
        insert(30)
        insert(20)
        insert(40)
        insert(70)
        insert(60)
        insert(80)

        // print inorder traversal of the BST
        inorder(root)
    }

    private fun testDelete() {
        println("Inorder traversal of the given tree")
        inorder()

        /*
          50                            50
           /     \         delete(20)      /   \
          30      70       --------->    30     70
         /  \    /  \                     \    /  \
       20   40  60   80                   40  60   80
         */println("\nDelete 20")
        delete(20)
        println("Inorder traversal of the modified tree")
        inorder()

        /*
              50                            50
           /     \         delete(30)      /   \
          30      70       --------->    40     70
            \    /  \                          /  \
            40  60   80                       60   80
         */println("\nDelete 30")
        delete(30)
        println("Inorder traversal of the modified tree")
        inorder()

        /*
              50                            60
           /     \         delete(50)      /   \
          40      70       --------->    40    70
                 /  \                            \
                60   80                           80
         */println("\nDelete 50")
        delete(50)
        println("Inorder traversal of the modified tree")
        inorder()
    }

    private fun insert(key: Int) {
        root = insert(root, key)
    }

    /**
     * A recursive function to insert a new key in BST
     */
    private fun insert(root: Node?, key: Int): Node {
        // If the tree is empty, return a new node
        var root = root
        if (root == null) {
            root = Node(key)
            return root
        }

        // Otherwise, recur down the tree
        if (key < root.key) root.left = insert(root.left, key) else if (key > root.key) root.right = insert(root.right, key)

        // return the (unchanged) node pointer
        return root
    }

    /**
     * A utility function to algs.search a given key in BST
     */
    private fun search(root: Node?, key: Int): Node? {
        // Base Cases: root is null or key is present at root
        if (root == null || root.key == key) return root

        // val is greater than root's key
        return if (root.key > key) search(root.left, key) else search(root.right, key)

        // val is less than root's key
    }

    private fun inorder(root: Node? = this.root) {
        if (root != null) {
            inorder(root.left)
            print(root.key.toString() + " ")
            inorder(root.right)
        }
    }

    // This method mainly calls deleteRec()
    fun delete(key: Int) {
        root = delete(root, key)
    }

    /**
     * A recursive function to insert a new key in BST.
     */
    fun delete(root: Node?, key: Int): Node? {
        // Base Case: If the tree is empty
        if (root == null) return null

        // Recur down the tree
        if (key < root.key) root.left = delete(root.left, key) else if (key > root.key) root.right = delete(root.right, key) else {
            // If key is same as root's key, then This is the node
            // to be deleted

            // Node with only one child or no child
            if (root.left == null) return root.right else if (root.right == null) return root.left

            // Node with two children:

            // 1- Get the inorder successor (smallest in the right subtree).
            // 2- Copy inorder successor to current node.
            root.key = minValue(root.right)

            // 3- Delete the inorder successor
            root.right = delete(root.right, root.key)
        }
        return root
    }

    private fun minValue(root: Node?): Int {
        var root = root
        var min = root!!.key
        while (root!!.left != null) {
            min = root.left!!.key
            root = root.left
        }
        return min
    }

    private val isBST: Boolean
        private get() = isBST(root, null)

    /* Returns true if given algs.search tree is binary
       algs.search tree (efficient version) */
    private fun isBST(node: Node?, prev: Node?): Boolean {
        // traverse the tree in inorder fashion and
        // keep a track of previous node
        if (node == null) return true
        if (!isBST(node.left, prev)) return false

        // allows only distinct values node
        return if (prev != null && node.key <= prev.key) false else isBST(node.right, node)
    }

    override fun test() {
        createTree()
        if (isBST) println("IS BST") else println("Not a BST")
        val result = search(root, 60)
        if (result == null) println("Element not present") else println("Element found: " + result.key)
        testDelete()
        println()
    }
}