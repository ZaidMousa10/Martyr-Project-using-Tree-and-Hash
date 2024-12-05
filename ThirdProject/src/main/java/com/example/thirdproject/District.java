package com.example.thirdproject;


public class District implements Comparable<District> {
    String districtName;
    AVLTree<String> location ;

    public District(String districtName, AVLTree<String> location) {
        this.districtName = districtName;
        this.location = location;
    }
    public District(String districtName) {
        this.districtName = districtName;
    }

    public static District get(District districtName) {
        return districtName;
    }

    public void addLocation(String locationName) {
        if (location == null) {
            location = new AVLTree<>();
        }
        location.insert(locationName);
    }
    public String getDistrictName() {
        return districtName;
    }
    public AVLTree<String> getLocation() {
        return location;
    }
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
    public void setLocation(AVLTree<String> location) {
        this.location = location;
    }


    @Override
    public String toString() {
        return "District{" +
                "districtName='" + districtName + '\'' +
                ", location=" + location +
                '}';
    }

    @Override
    public int compareTo( District o) {
        return this.districtName.compareTo(o.districtName);
    }
}
