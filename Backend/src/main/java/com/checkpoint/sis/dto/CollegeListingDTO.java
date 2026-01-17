package com.checkpoint.sis.dto;

import com.checkpoint.sis.enums.Status;

public class CollegeListingDTO {

    private int collegeId;
    private String collegeName;
    private String address;
    private String logoUrl;
    private Status status;

    // Constructor
    public CollegeListingDTO(int collegeId, String collegeName, String address, String logoUrl, Status status) {
        this.collegeId = collegeId;
        this.collegeName = collegeName;
        this.address = address;
        this.logoUrl = logoUrl;
        this.status = status;
    }

    // Getters and Setters
    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}