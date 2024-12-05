package com.example.thirdproject;


public class TNode<T extends Comparable<T>> implements Comparable<TNode<Martyr>> {
    private T value;
    private TNode<T> left;
    private TNode<T> right;

    public TNode(T value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public TNode<T> getLeft() {
        return left;
    }

    public void setLeft(TNode<T> left) {
        this.left = left;
    }

    public TNode<T> getRight() {
        return right;
    }

    public void setRight(TNode<T> right) {
        this.right = right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }
    public boolean hasBoth() {
        return left!= null && right!= null;
    }

    @Override
    public String toString() {
        return "TNode{" +
                "value=" + value +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public int compareTo( TNode<Martyr> o) {
        return this.value.compareTo((T) o.value);
    }
}
