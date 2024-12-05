package com.example.thirdproject;

import java.util.Comparator;

public class Heap<T extends Comparable<T>> {

    public Heap() {}

    public void heapSort(T[] array, Comparator<T> comparator) {
        int N = array.length;
        heapify(array, N, comparator);
        while (N > 0) {
            exchange(array, 0, --N);
            sink(array, 0, N, comparator);
        }
    }

    public void heapify(T[] a, int N, Comparator<T> comparator) {
        for (int k = N / 2 - 1; k >= 0; k--) {
            sink(a, k, N, comparator);
        }
    }

    private void sink(T[] a, int k, int N, Comparator<T> comparator) {
        while (2 * k + 1 < N) {
            int target = 2 * k + 1;
            if (target + 1 < N && comparator.compare(a[target], a[target + 1]) < 0) {
                target++;
            }
            if (!(comparator.compare(a[k], a[target]) < 0)) break;
            exchange(a, k, target);
            k = target;
        }
    }

    private void exchange(T[] a, int k1, int k2) {
        T temp = a[k1];
        a[k1] = a[k2];
        a[k2] = temp;
    }
}