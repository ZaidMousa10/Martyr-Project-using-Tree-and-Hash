package com.example.thirdproject;

import java.util.Comparator;

public class MartyrCompareAge implements Comparator<Martyr> {
    @Override
    public int compare(Martyr o1, Martyr o2) {
        return o1.getAge() - o2.getAge();
    }
}
