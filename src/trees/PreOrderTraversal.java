package trees;

import java.util.*;

/*
 * Iterative preorder traversal. Uses prev and curr pointers to track current
 * direction of traversal. Instead of recursion stack, we use a handcreated
 * stack.
 */
public class PreOrderTraversal {

    public static List<Integer> getPreorder(TreeNode<Integer> root) {
        List<Integer> preorderItems = new ArrayList<Integer>();
        
        if (root == null)
            return preorderItems;
        
        Deque<TreeNode<Integer>> stack = new ArrayDeque<>();
        TreeNode<Integer> prev = null;
        TreeNode<Integer> curr;
        
        stack.addLast(root);
        while(!stack.isEmpty()) {
            curr = stack.getLast();
            
            if (prev == null || prev.left == curr || prev.right == curr) {
                /*
                 * prev == null: we are just starting at the root of the tree
                 * prev.left/right == curr: result of recursively calling left/right
                 *                    subtree of prev
                 */
                preorderItems.add(curr.value); // print current
                if (curr.left != null) {
                    stack.addLast(curr.left);
                } else if (curr.right != null) {
                        stack.addLast(curr.right);
                } else {
                        // both left and right subtree are done. Pop and move up
                        stack.removeLast();
                }
            } else if (curr.left == prev) {
                // done with left subtree. moving up towards root
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
        
        return preorderItems;
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
        
        System.out.println(bstTree.getPreorder().toString());
        System.out.println(getPreorder(bstTree.getRoot()));
    }
}