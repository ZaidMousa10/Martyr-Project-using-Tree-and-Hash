package com.example.thirdproject;

public class AVLTree<T extends Comparable<T>> extends BSTree<T>  {

    private Stack<T> nextStack = new Stack<>();
    private Stack<T> prefStack = new Stack<>();

    @Override
    public void insert(T value) {
        if (isEmpty()) {
            root = new TNode<>(value);
        } else {
            root = addEntry(value, root);
        }
    }

    private TNode<T> addEntry(T value, TNode<T> node) {
        if (node == null) {
            return new TNode<>(value);
        }

        if (value.compareTo(node.getValue()) < 0) {
            node.setLeft(addEntry(value, node.getLeft()));
        } else {
            node.setRight(addEntry(value, node.getRight()));
        }

        return rebalance(node);
    }

    public T delete(T data) {
        if (isEmpty()) {
            return null;
        }
        TNode<T> deletedNode = findNode(data, root);
        if (deletedNode != null) {
            root = deleteEntry(root, data);
        }
        return deletedNode != null ? deletedNode.getValue() : null;
    }

    private TNode<T> deleteEntry(TNode<T> node, T data) {
        if (node == null) {
            return null;
        }
        int diff = data.compareTo(node.getValue());
        if (diff < 0) {
            node.setLeft(deleteEntry(node.getLeft(), data));
        } else if (diff > 0) {
            node.setRight(deleteEntry(node.getRight(), data));
        } else {
            if (node.getLeft() == null || node.getRight() == null) {
                node = (node.getLeft() != null) ? node.getLeft() : node.getRight();
            } else {
                TNode<T> minLargerNode = findMin(node.getRight());
                node.setValue(minLargerNode.getValue());
                node.setRight(deleteEntry(node.getRight(), minLargerNode.getValue()));
            }
        }
        return node != null ? rebalance(node) : null;
    }


    private TNode<T> findMin(TNode<T> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }


    public TNode<T> findNode(T value, TNode<T> node) {
        if (node == null) return null;
        if (value.compareTo(node.getValue()) == 0) return node;
        if (value.compareTo(node.getValue()) < 0) return findNode(value, node.getLeft());
        return findNode(value, node.getRight());
    }


    protected TNode<T> rebalance(TNode<T> node) {
        int heightDiff = getHeightDifference(node);
        if (heightDiff > 1) {
            if (getHeightDifference(node.getLeft()) >= 0) {
                node = rotateRight(node);
            } else {
                node = rotateLeftRight(node);
            }
        } else if (heightDiff < -1) {
            if (getHeightDifference(node.getRight()) <= 0) {
                node = rotateLeft(node);
            } else {
                node = rotateRightLeft(node);
            }
        }
        return node;
    }

    protected int getHeightDifference(TNode<T> node) {
        return height(node.getLeft()) - height(node.getRight());
    }

    protected TNode<T> rotateRight(TNode<T> node) {
        TNode<T> temp = node.getLeft();
        node.setLeft(temp.getRight());
        temp.setRight(node);
        return temp;
    }

    protected TNode<T> rotateLeft(TNode<T> node) {
        TNode<T> temp = node.getRight();
        node.setRight(temp.getLeft());
        temp.setLeft(node);
        return temp;
    }

    protected TNode<T> rotateRightLeft(TNode<T> node) {
        node.setRight(rotateRight(node.getRight()));
        return rotateLeft(node);
    }

    protected TNode<T> rotateLeftRight(TNode<T> node) {
        node.setLeft(rotateLeft(node.getLeft()));
        return rotateRight(node);
    }

    public void traverse() {
        inOrderTraversal(root);
        System.out.println();
    }

    private void inOrderTraversal(TNode<T> node) {
        if (node != null) {
            inOrderTraversal(node.getLeft());
            System.out.print(node.getValue() + " ");
            inOrderTraversal(node.getRight());
        }
    }
    public void traverseInOrder() {
        nextStack.clear();
        prefStack.clear();
        traverseInOrder(root);
        TNode<T> currData = null;
        while (!prefStack.isEmpty()) {
            currData = (TNode<T>) prefStack.pop();
            nextStack.push((T) currData);
        }
    }

    private void traverseInOrder(TNode<T> node) {
        if (node != null) {
            if (node.getLeft() != null)
                traverseInOrder(node.getLeft());
            prefStack.push((T) node);

            if (node.getRight() != null)
                traverseInOrder(node.getRight());

        }

    }


    public Stack<T> getNextStack() {
        return nextStack;
    }

    public void setNextStack(Stack<T> nextStack) {
        this.nextStack = nextStack;
    }

    public Stack<T> getPrefStack() {
        return prefStack;
    }

    public void setPrefStack(Stack<T> prefStack) {
        this.prefStack = prefStack;
    }


}
