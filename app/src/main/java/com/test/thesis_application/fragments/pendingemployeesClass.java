package com.test.thesis_application.fragments;

public class pendingemployeesClass {
    private String name;
    private String jobtype;
    private Double EcontactNumber;
    private String address;
    private String avatar;



    public pendingemployeesClass(String name, String jobtype, Double econtactNumber, String address, String avatar) {
        this.name = name;
        this.jobtype = jobtype;
        EcontactNumber = econtactNumber;
        this.address = address;
        this.avatar = avatar;
    }

    public pendingemployeesClass() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public Double getEcontactNumber() {
        return EcontactNumber;
    }

    public void setEcontactNumber(Double econtactNumber) {
        EcontactNumber = econtactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "pendingemployeesClass{" +
                "name='" + name + '\'' +
                ", jobtype='" + jobtype + '\'' +
                ", EcontactNumber=" + EcontactNumber +
                ", address='" + address + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
