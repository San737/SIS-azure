package com.checkpoint.sis.dto;

import java.time.LocalDate;

public class CourseListingDTO {
    private Integer courseId;
    private String courseName;
    private String description;
    private Integer credits;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer seatLimit;
    private Integer seatsAvailable;
    private Boolean isFull;
    private String enrollmentStatus; // Will be "ENROLLED" or "AVAILABLE"
    private String departmentName;

    // Constructor
    public CourseListingDTO(Integer courseId, String courseName, String description, Integer credits,
            LocalDate startDate, LocalDate endDate, Integer seatLimit, Integer seatsAvailable,
            Boolean isFull, String enrollmentStatus, String departmentName) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.startDate = startDate;
        this.endDate = endDate;
        this.seatLimit = seatLimit;
        this.seatsAvailable = seatsAvailable;
        this.isFull = isFull;
        this.enrollmentStatus = enrollmentStatus;
        this.departmentName = departmentName;
    }

    // Getters and Setters
    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getSeatLimit() {
        return seatLimit;
    }

    public void setSeatLimit(Integer seatLimit) {
        this.seatLimit = seatLimit;
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public Boolean getIsFull() {
        return isFull;
    }

    public void setIsFull(Boolean isFull) {
        this.isFull = isFull;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
