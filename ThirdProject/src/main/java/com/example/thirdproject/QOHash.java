package com.example.thirdproject;
import java.util.Arrays;

public class QOHash<T extends Comparable<T>> {
    private HNode<T>[] table;
    private int M;
    public int collision = 0;

    public QOHash(int capacity) {
        M = getNextPrime(capacity * 2);
        table = new HNode[M];

        for (int i = 0; i < M; i++) {
            table[i] = new HNode<>(null);
        }
    }

    public void add(T data) {
        if (size() == M/2) rehash();
        int index = Math.abs(data.hashCode()) % M;
        int i = 0;
        for (; table[(index + i * (i++)) % M].getFlag() == 'f' && i <= M;);
        if (i > M) // a loop occurred
            System.out.println("This element cannot be added");
        else {
            index = (index + (--i) * i) % M;
            table[index].setValue(data);
            table[index].setFlag('f');
        }
    }


    public T delete(T value) {
        int index = hash(value);
        HNode<T> target = table[index];

        if (target.getFlag() == 'f' && target.getValue().equals(value)) {
            target.setFlag('d');
            return target.getValue();
        } else {
            int i = 1;
            int nextHash = hash(value, i);
            while (nextHash != index) {
                target = table[nextHash];
                if (target.getFlag() == 'e') {
                    return null;
                } else if (target.getFlag() == 'f' && target.getValue().equals(value)) {
                    target.setFlag('d');
                    return target.getValue();
                }

                nextHash = hash(value, ++i);
            }

            return null;
        }
    }

    private int hash(T value) {
        return hash(value, 0);
    }

    private int hash(T value, int i) {
        return (Math.abs(value.hashCode()) + (int) Math.pow(i, 2)) % M;
    }

    public void traverse() {
        for (int i = 0; i < M; i++) {
            HNode<T> curr = table[i];
            System.out.println(i + "\t|\t" + curr.getFlag() + "\t|\t" + (curr.getFlag() == 'd' ? "null" : curr.getValue()));
        }
    }

    public T search(T value) {
        int index = Math.abs(value.hashCode()) % M;
        int i = 0;
        char flag = table[(index + i * i) % M].getFlag();

        while (flag == 'd' || (flag == 'f' && !table[(index + i * i) % M].getValue().equals(value) && i <= M)) {
            i++;
            System.out.println("2");
            flag = table[(index + i * i) % M].getFlag();
        }

        if (flag == 'f' && table[(index + i * i) % M].getValue().equals(value)) {
            System.out.println("3");
            return table[(index + i * i) % M].getValue();
        }

        return null;
    }



    public boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

    public int getNextPrime(int number) {
        while (!isPrime(number)) {
            number++;
        }
        return number;
    }

    public int size() {
        int count = 0;
        for (HNode<T> node : table) {
            if (node.getFlag() == 'f') {
                count++;
            }
        }
        return count;
    }

    private void rehash() {
        HNode<T>[] oldTable = table;
        M = getNextPrime(M * 2);
        table = new HNode[M];

        for (int i = 0; i < M; i++) {
            table[i] = new HNode<>(null);
        }

        for (HNode<T> node : oldTable) {
            if (node.getFlag() == 'f') {
                add(node.getValue());
            }
        }
    }
    public void clear() {
        for (int i = 0; i < M; i++) {
            table[i] = new HNode<>(null);
        }
        collision = 0; // Reset collision count
    }


    public HNode<T>[] getTable() {
        return table;
    }

    public void setTable(HNode<T>[] table) {
        this.table = table;
    }

    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }

    public int getCollision() {
        return collision;
    }

    public void setCollision(int collision) {
        this.collision = collision;
    }

    @Override
    public String toString() {
        return "QOHash{" +
                "table=" + Arrays.toString(table) +
                ", M=" + M +
                ", collision=" + collision +
                '}';
    }

}
