package com.checkpoint.sis.dto;

public class CourseInfoDTO {
    private Integer courseId;
    private String courseName;
    private Integer credits;

    // Constructor, Getters, and Setters
    public CourseInfoDTO(Integer courseId, String courseName, Integer credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
    }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
}
