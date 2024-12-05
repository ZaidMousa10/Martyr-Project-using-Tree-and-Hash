package com.example.thirdproject;

public class BSTree<T extends Comparable<T>> {
    protected TNode<T> root;

    public BSTree() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int height() {
        return height(root);
    }

    protected int height(TNode<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.getLeft()), height(node.getRight()));
    }

    public void insert(T value) {
        root = insert(root, value);
    }

    protected TNode<T> insert(TNode<T> node, T value) {
        if (node == null) {
            return new TNode<>(value);
        }

        if (value.compareTo(node.getValue()) < 0) {
            node.setLeft(insert(node.getLeft(), value));
        } else {
            node.setRight(insert(node.getRight(), value));
        }

        return node;
    }

    public T delete(T value) {
        TNode<T> node = find(value, root);
        if (node != null) {
            root = delete(root, value);
            return node.getValue();
        }
        return null;
    }

    protected TNode<T> delete(TNode<T> node, T value) {
        if (node == null) return null;

        if (value.compareTo(node.getValue()) < 0) {
            node.setLeft(delete(node.getLeft(), value));
        } else if (value.compareTo(node.getValue()) > 0) {
            node.setRight(delete(node.getRight(), value));
        } else {
            if (node.isLeaf()) {
                return null;
            } else if (node.hasLeft() && !node.hasRight()) {
                return node.getLeft();
            } else if (node.hasRight() && !node.hasLeft()) {
                return node.getRight();
            } else {
                TNode<T> successor = getSuccessor(node);
                node.setValue(successor.getValue());
                node.setRight(delete(node.getRight(), successor.getValue()));
            }
        }

        return node;
    }

    public T find(T value) {
        TNode<T> foundNode = find(value, root);
        if (foundNode != null) {
            return foundNode.getValue();
        } else {
            // Value not found in the tree
            return null; // Or you can throw an exception if needed
        }
    }
    protected TNode<T> find(T value, TNode<T> node) {
        if (node == null) return null;
        if (value.compareTo(node.getValue()) == 0) return node;
        if (value.compareTo(node.getValue()) < 0) return find(value, node.getLeft());
        return find(value, node.getRight());
    }

    protected TNode<T> getSuccessor(TNode<T> node) {
        TNode<T> successor = node.getRight();
        while (successor != null && successor.getLeft() != null) {
            successor = successor.getLeft();
        }
        return successor;
    }
    public void traverseInOrder() {
        traverseInOrder(root);
    }

    private void traverseInOrder(TNode<T> node) {
        if (node != null) {
            traverseInOrder(node.getLeft());
            System.out.print(node.getValue() + " ");
            traverseInOrder(node.getRight());
        }
    }
    public int size() {
        return size(root);
    }

    protected int size(TNode<T> node) {
        if (node == null) {
            return 0;
        }
        int leftSize = size(node.getLeft());
        int rightSize = size(node.getRight());
        return leftSize + rightSize + 1;
    }

    public TNode<T> getRoot() {
        return root;
    }

    public void setRoot(TNode<T> root) {
        this.root = root;
    }
    public boolean contains(T value) {
        return find(value, root)!= null;
    }

    @Override
    public String toString() {
        return
                "root=" + root ;
    }

    public T get(int j) {
        return get(root, j);
    }
    protected T get(TNode<T> node, int j) {
        if (node == null) {
            return null;
        }
        int leftSize = size(node.getLeft());
        if (leftSize == j) {
            return node.getValue();
        } else if (leftSize > j) {
            return get(node.getLeft(), j);
        } else {
            return get(node.getRight(), j - leftSize - 1);
        }
    }
}
