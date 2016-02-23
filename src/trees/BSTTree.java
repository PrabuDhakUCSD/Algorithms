package trees;

import java.util.*;

class TreeNode<T extends Comparable<T>> implements Comparable<TreeNode<T>> {
    public TreeNode(T value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
    
    @Override
    public int compareTo(TreeNode<T> other) {
        return this.value.compareTo(other.value);
    }
    
    T value;
    TreeNode<T> left;
    TreeNode<T> right;
}
public class BSTTree<T extends Comparable<T>> {
    public BSTTree() {
        root = null;
    }
    
    public void insert(T value) {
        TreeNode<T> node = new TreeNode<T>(value);
        root = insertHelper(root, node);
    }
    
    private TreeNode<T> insertHelper(TreeNode<T> current,
            TreeNode<T> nodeToInsert) {
        if (current == null)
            return nodeToInsert;
        
        if (nodeToInsert.compareTo(current) <= 0)
            current.left = insertHelper(current.left, nodeToInsert);
        else
            current.right = insertHelper(current.right, nodeToInsert);
        
        return current;
    }
    
    public TreeNode<T> getRoot() {
        return root;
    }
    
    public List<T> getInorder() {
        List<T> inorderItems = new ArrayList<T>();
        getInorderHelper(root, inorderItems);
        return inorderItems;
    }
    
    private void getInorderHelper(TreeNode<T> current, List<T> inorderItems) {
        if (current == null)
            return;
        
        getInorderHelper(current.left, inorderItems);
        inorderItems.add(current.value);
        getInorderHelper(current.right, inorderItems);
    }
    
    private TreeNode<T> root;
}