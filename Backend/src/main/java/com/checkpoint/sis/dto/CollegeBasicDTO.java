package com.checkpoint.sis.dto;

public class CollegeBasicDTO {
    private int collegeId;
    private String collegeName;

    // Constructor
    public CollegeBasicDTO(int collegeId, String collegeName) {
        this.collegeId = collegeId;
        this.collegeName = collegeName;
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
}