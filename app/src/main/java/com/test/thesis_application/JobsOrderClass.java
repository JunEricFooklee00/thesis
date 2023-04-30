package com.test.thesis_application;

import org.bson.types.ObjectId;

public class JobsOrderClass {
    private ObjectId _id;

    private String userId;
    private String name;
    private Double contactNumber;
    private String scopeofwork;
    private String jobTitle;
    private String Area;
    private String Unit;

    private String Location;
    private String StartingDate;
    private String ExpectedFinishDate;

    private String Worker1;

    private String Worker2;
    private String Worker3;
    private String Worker4;
    private String Worker5;

    // empty constructor required for MongoDB Data Access POJO codec compatibility
    public JobsOrderClass() {
    }

    // constructor


    public JobsOrderClass(ObjectId _id, String userId, String name, Double contactNumber, String scopeofwork, String jobTitle, String area, String unit, String location, String startingDate, String expectedFinishDate, String worker1, String worker2, String worker3, String worker4, String worker5) {
        this._id = _id;
        this.userId = userId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.scopeofwork = scopeofwork;
        this.jobTitle = jobTitle;
        Area = area;
        Unit = unit;
        Location = location;
        StartingDate = startingDate;
        ExpectedFinishDate = expectedFinishDate;
        Worker1 = worker1;
        Worker2 = worker2;
        Worker3 = worker3;
        Worker4 = worker4;
        Worker5 = worker5;
    }

    public String getWorker1() {
        return Worker1;
    }

    public void setWorker1(String worker1) {
        Worker1 = worker1;
    }

    public String getWorker2() {
        return Worker2;
    }

    public void setWorker2(String worker2) {
        Worker2 = worker2;
    }

    public String getWorker3() {
        return Worker3;
    }

    public void setWorker3(String worker3) {
        Worker3 = worker3;
    }

    public String getWorker4() {
        return Worker4;
    }

    public void setWorker4(String worker4) {
        Worker4 = worker4;
    }

    public String getWorker5() {
        return Worker5;
    }

    public void setWorker5(String worker5) {
        Worker5 = worker5;
    }

    public Double getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(Double contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScopeofwork() {
        return scopeofwork;
    }

    public void setScopeofwork(String scopeofwork) {
        this.scopeofwork = scopeofwork;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getStartingDate() {
        return StartingDate;
    }

    public void setStartingDate(String startingDate) {
        StartingDate = startingDate;
    }

    public String getExpectedFinishDate() {
        return ExpectedFinishDate;
    }

    public void setExpectedFinishDate(String expectedFinishDate) {
        ExpectedFinishDate = expectedFinishDate;
    }

    @Override
    public String toString() {
        return "JobsOrderClass{" +
                "_id=" + _id +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", scopeofwork='" + scopeofwork + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", Area='" + Area + '\'' +
                ", Unit='" + Unit + '\'' +
                ", Location='" + Location + '\'' +
                ", StartingDate='" + StartingDate + '\'' +
                ", ExpectedFinishDate='" + ExpectedFinishDate + '\'' +
                '}';
    }
}