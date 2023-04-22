package com.test.thesis_application;

public class Jobsinfoclass {
    private String _id;
    private String availability;
    private String Profile;
    private String Rating;
    private String address;
    private String first_name;
    private String id;
    private String last_name;

    public Jobsinfoclass() {
    }

    public Jobsinfoclass(String _id, String availability, String profile, String rating, String address, String first_name, String id, String last_name) {
        this._id = _id;
        this.availability = availability;
        Profile = profile;
        Rating = rating;
        this.address = address;
        this.first_name = first_name;
        this.id = id;
        this.last_name = last_name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public String toString() {
        return "Jobsinfoclass{" +
                "_id='" + _id + '\'' +
                ", availability='" + availability + '\'' +
                ", Profile='" + Profile + '\'' +
                ", Rating='" + Rating + '\'' +
                ", address='" + address + '\'' +
                ", first_name='" + first_name + '\'' +
                ", id='" + id + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }
}
