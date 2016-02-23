package trees;

import java.util.*;

/*
 * Iterative inorder traversal. Uses prev and curr pointers to track current
 * direction of traversal. Instead of recursion stack, we use a handcreated
 * stack.
 */
public class InOrderTraversal {

    public static List<Integer> getInorder(TreeNode<Integer> root) {
        List<Integer> inorderItems = new ArrayList<Integer>();
        
        if (root == null)
            return inorderItems;
        
        Deque<TreeNode<Integer>> stack = new ArrayDeque<>();
        TreeNode<Integer> prev = null;
        TreeNode<Integer> curr = root;
        
        stack.addLast(root);
        while(!stack.isEmpty()) {
            curr = stack.getLast();
            
            if (prev == null || prev.left == curr || prev.right == curr) {
                /*
                 * prev == null: we are just starting at the root of the tree
                 * prev.left/right == curr: result of recursively calling left
                 *                    subtree of prev
                 */
                if (curr.left != null) {
                    stack.addLast(curr.left);
                } else {
                    inorderItems.add(curr.value);
                    if (curr.right != null) {
                        stack.addLast(curr.right);
                    } else {
                        // both left and right subtree are done. Pop and move up
                        stack.removeLast();
                    }
                }
            } else if (curr.left == prev) {
                // done with left subtree. moving up towards root
                inorderItems.add(curr.value);
                if (curr.right != null) {
                    stack.addLast(curr.right);
                } else {
                    stack.removeLast();
                }
            } else { // curr.right == prev
                // done with both left and right subtree. pop and move up.
                stack.removeLast();
            }
            
            prev = curr;
        }
        
        return inorderItems;
    }
    
    public static void main(String[] args) {
        BSTTree<Integer> bstTree = new BSTTree<>();
        bstTree.insert(20);
        bstTree.insert(15);
        bstTree.insert(7);
        bstTree.insert(18);
        bstTree.insert(19);
        bstTree.insert(30);
        bstTree.insert(38);
        bstTree.insert(33);
        
        System.out.println(bstTree.getInorder().toString());
        System.out.println(getInorder(bstTree.getRoot()));
    }
}