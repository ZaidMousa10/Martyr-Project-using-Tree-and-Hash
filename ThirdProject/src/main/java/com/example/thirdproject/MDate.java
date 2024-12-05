package com.example.thirdproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

public class MDate<T extends Comparable<T>> implements Comparable<MDate<T>> {
    private Date date;
    private AVLTree<T> martyrTree;

    public MDate(Date date, AVLTree<T> martyrTree) {
        this.date = date;
        this.martyrTree = martyrTree;
    }

    public MDate(Date date) {
        this.date = date;
        this.martyrTree = new AVLTree<T>();
    }


    public Date getDate() {
        return date;
    }

    public AVLTree<T> getMartyrTree() {
        return martyrTree;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMartyrTree(AVLTree<T> martyrTree) {
        this.martyrTree = martyrTree;
    }
    public void addMartyr(Martyr martyr) {
        martyrTree.insert((T) martyr);
    }
    public void removeMartyr(Martyr martyr) {
        martyrTree.delete((T) martyr);
    }
    public boolean search(Martyr currentMartyr) {
        T result = martyrTree.find((T) currentMartyr);
        return result != null;
    }


    public void calculateStatistics() {
        int totalMartyrs = 0;
        double totalAge = 0;
        int maleCount = 0;
        int femaleCount = 0;

        // Traverse the AVL tree to calculate statistics
        for (int i = 0; i < martyrTree.size(); i++) {
            Martyr martyr = (Martyr) martyrTree.get(i);
            if (martyr.getAge() != -1) { // Check if age is not -1
                totalMartyrs++;
                totalAge += martyr.getAge();
                if (martyr.getGender().equalsIgnoreCase("M")) {
                    maleCount++;
                } else if (martyr.getGender().equalsIgnoreCase("F")) {
                    femaleCount++;
                }
            }
        }

        // Calculate average age
        double avgAge = totalMartyrs > 0 ? totalAge / totalMartyrs : 0;

        // Print or store the statistics as needed
        System.out.println("Total Martyrs: " + totalMartyrs);
        System.out.println("Average Age: " + avgAge);
        System.out.println("Male Count: " + maleCount);
        System.out.println("Female Count: " + femaleCount);
    }

    @Override
    public int compareTo(MDate<T> o) {
        return this.date.compareTo(o.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass()!= o.getClass()) return false;
        MDate<T> mDate = (MDate<T>) o;
        return this.date.equals( mDate.date) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public String toString() {
        return
                "date=" + date.toString() ;
    }


}
