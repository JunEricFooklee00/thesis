package com.test.thesis_application;

import java.util.List;

public class EmployeeList {
    private List<EmployeeList> employees;

    public EmployeeList(List<EmployeeList> employees) {
        this.employees = employees;
    }

    public List<EmployeeList> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeList> employees) {
        this.employees = employees;
    }
}

class Employee {
    private String objectId;
    private String firstName;
    private String lastName;
    private String address;
    private String profile;
    private String rating;
    private String distance;

    public Employee(String objectId, String firstName, String lastName, String address, String profile, String rating, String distance) {
        this.objectId = objectId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.profile = profile;
        this.rating = rating;
        this.distance = distance;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
