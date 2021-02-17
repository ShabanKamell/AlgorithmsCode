package algs.search.bst

import algs.shared.model.Node
import algs.shared.util.Testable

/**
 * Best: O(1) Average, Worst: O(log n)
 */
class AvlTree : Testable {
    // Root of BST
    var root: Node? = null
        private set

    fun search(key: Int): Node? {
        var current = root
        while (current != null) {
            if (current.key == key) {
                break
            }
            current = if (current.key < key) current.right else current.left
        }
        return current
    }

    fun insert(key: Int) {
        root = insert(root, key)
    }

    fun delete(key: Int) {
        root = delete(root, key)
    }

    fun height(): Int {
        return if (root == null) -1 else root!!.height
    }

    private fun insert(node: Node?, key: Int): Node {
        if (node == null) return Node(key) else if (node.key > key) node.left = insert(node.left, key) else if (node.key < key) node.right = insert(node.right, key) else throw IllegalArgumentException("duplicate Key!")
        return reBalance(node)
    }

    private fun delete(node: Node?, key: Int): Node? {
        var node = node
        if (node == null) return null else if (node.key > key) {
            node.left = delete(node.left, key)
        } else if (node.key < key) {
            node.right = delete(node.right, key)
        } else {
            if (node.left == null || node.right == null) {
                node = if (node.left == null) node.right else node.left
            } else {
                val mostLeftChild = mostLeftChild(node.right)
                node.key = mostLeftChild!!.key
                node.right = delete(node.right, node.key)
            }
        }
        if (node != null) node = reBalance(node)
        return node
    }

    private fun mostLeftChild(node: Node?): Node? {
        var current = node
        /* loop down to find the leftmost leaf */while (current!!.left != null) {
            current = current.left
        }
        return current
    }

    private fun reBalance(z: Node): Node {
        var z: Node? = z
        updateHeight(z)
        val balance = getBalance(z)
        if (balance > 1) {
            // Right Right
            if (height(z!!.right!!.right) > height(z.right!!.left)) {
                z = rotateLeft(z)
            } else {
                z.right = rotateRight(z.right)
                z = rotateLeft(z)
            }
        } else if (balance < -1) {
            // Left Left Case
            if (height(z!!.left!!.left) > height(z.left!!.right)) {
                z = rotateRight(z)
            } else {
                z.left = rotateLeft(z.left)
                z = rotateRight(z)
            }
        }
        return z!!
    }

    /**
     * y              x
     * /                \
     * x    --------->    y
     * \                /
     * z              z
     */
    private fun rotateRight(y: Node?): Node? {
        val x = y!!.left
        val z = x!!.right

        // Rotate
        x.right = y
        y.left = z
        updateHeight(y)
        updateHeight(x)
        return x
    }

    /**
     * y                     x
     * \                   /
     * x    --------->   y
     * /                   \
     * z                     z
     */
    private fun rotateLeft(y: Node?): Node? {
        val x = y!!.right
        val z = x!!.left

        // Rotate
        x.left = y
        y.right = z
        updateHeight(y)
        updateHeight(x)
        return x
    }

    private fun updateHeight(n: Node?) {
        n!!.height = 1 + Math.max(height(n.left), height(n.right))
    }

    private fun height(n: Node?): Int {
        return n?.height ?: -1
    }

    fun getBalance(n: Node?): Int {
        return if (n == null) 0 else height(n.right) - height(n.left)
    }

    // A utility function to print preorder traversal
    // of the tree.
    // The function also prints height of every node
    fun preOrder(node: Node?) {
        if (node != null) {
            print(node.key.toString() + " ")
            preOrder(node.left)
            preOrder(node.right)
        }
    }

    private fun inorder(root: Node?) {
        if (root != null) {
            inorder(root.left)
            print(root.key.toString() + " ")
            inorder(root.right)
        }
    }

    override fun test() {
        /* Constructing tree given in the above figure */
        root = insert(root, 10)
        root = insert(root, 50)
        root = insert(root, 40)
        root = insert(root, 25)
        root = insert(root, 30)
        root = insert(root, 20)

        /* The constructed AVL Tree would be
             30
            /  \
          20   40
         /  \     \
        10  25    50
        */println("Preorder traversal of constructed tree is: ")
        preOrder(root)
        println()
        println("inorder traversal of constructed tree is: ")
        inorder(root)
        println()
        delete(20)
        delete(40)
        println("Tree after deleting 20 & 40: ")
        inorder(root)
        println()
    }
}