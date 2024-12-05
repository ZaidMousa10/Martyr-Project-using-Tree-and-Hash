package com.example.thirdproject;

public class HNode<T extends Comparable<T>> {
    private int index;
    private T value;
    private char flag;

    public HNode(int index, T value) {
        this.index = index;
        this.value = value;
        this.flag = 'e';
    }
    public HNode(T value) {
        this.value = value;
        this.flag = 'e';
    }

    public HNode(int i, char flag, String value) {
        this.index = i;
        this.value = (T) value;
        this.flag = flag;
    }

    public int getIndex() {
        return index;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public char getFlag() {
        return flag;
    }

    public void setFlag(char flag) {
        this.flag = flag;
    }
}
