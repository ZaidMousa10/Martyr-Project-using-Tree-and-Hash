package com.example.thirdproject;

import java.util.Objects;

public class Martyr implements Comparable<Martyr>{
    private String name;
    private int age;
    private String location;
    private String district;
    private String gender;

    public Martyr(String name, int age, String location, String district, String gender) {
        this.name = name;
        this.age = age;
        this.location = location;
        this.district = district;
        this.gender = gender;
    }

    public Martyr(){
    }
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getLocation() {
        return location;
    }

    public String getDistrict() {
        return district;
    }

    public String getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    @Override
    public int compareTo(Martyr other) {
        int districtComparison = this.district.compareTo(other.district);
        if (districtComparison != 0) {
            return districtComparison;
        }
        return this.name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Martyr martyr = (Martyr) o;
        return age == martyr.age && Objects.equals(name, martyr.name) && Objects.equals(location, martyr.location) && Objects.equals(district, martyr.district) && Objects.equals(gender, martyr.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, location, district, gender);
    }

    @Override
    public String toString() {
        return "name=" + name + ", age=" + age + ", location=" + location + ", district=" + district + ", gender=" + gender  ;
    }

}
