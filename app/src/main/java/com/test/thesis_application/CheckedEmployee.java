package com.test.thesis_application;

public class CheckedEmployee {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String address;
    private String profile;
    private String rating;
    private String distance;

    public CheckedEmployee(String employeeId, String firstName, String lastName, String address, String profile, String rating, String distance) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.profile = profile;
        this.rating = rating;
        this.distance = distance;
    }

    // getters and setters

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    @Override
    public String toString() {
        return "CheckedEmployee{" +
                "employeeId='" + employeeId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", profile='" + profile + '\'' +
                ", rating='" + rating + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
