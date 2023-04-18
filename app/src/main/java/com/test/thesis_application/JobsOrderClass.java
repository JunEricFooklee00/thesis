package com.test.thesis_application;

import org.bson.types.ObjectId;

public class JobsOrderClass {
    private ObjectId _id;
    private String userId;
    private String scopeofwork;
    private String jobTitle;
    private String Area;
    private String Location;
    private String StartingDate;
    private String ExpectedFinishDate;



    private String Unit;

    // empty constructor required for MongoDB Data Access POJO codec compatibility
    public JobsOrderClass() {
    }
    // constructor
    public JobsOrderClass(ObjectId _id, String userId , String scopeofwork, String jobTitle, String area, String location, String startingDate, String expectedFinishDate, String unit) {
        this._id = _id;
        this.userId = userId;
        this.scopeofwork = scopeofwork;
        this.jobTitle = jobTitle;
        this.Area = area;
        this.Location = location;
        this.StartingDate = startingDate;
        this.ExpectedFinishDate = expectedFinishDate;
        this.Unit = unit;
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
                ", userId=" + userId +
                ", scopeofwork='" + scopeofwork + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", Area='" + Area + '\'' +
                ", Location='" + Location + '\'' +
                ", StartingDate='" + StartingDate + '\'' +
                ", ExpectedFinishDate='" + ExpectedFinishDate + '\'' +
                '}';
    }
}