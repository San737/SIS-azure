package com.checkpoint.sis.dto;

public class AdminCourseStatusDTO {
    private Integer courseId;
    private String courseName;
    private String departmentName;
    private Integer totalSeats; // seat_limit from courses table
    private Long enrolledCount; // Calculated count from enrollments table
    private Integer seatsAvailable; // Calculated (totalSeats - enrolledCount)
    private String status; // e.g., "Available", "Full"

    // Constructor to handle calculation
    public AdminCourseStatusDTO(Integer courseId, String courseName, String departmentName, Integer totalSeats, Long enrolledCount) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.departmentName = departmentName;
        this.totalSeats = 60; // Assume total seats is 60 for now
        this.enrolledCount = enrolledCount != null ? enrolledCount : 0L;

        // Calculate available seats, ensuring non-negative
        this.seatsAvailable = this.totalSeats - this.enrolledCount.intValue();

        // Determine status based on availability (assuming totalSeats > 0 means the course manages seats)
        if (this.seatsAvailable > 0) {
            this.status = "AVAILABLE";
        } else {
            this.status = "FULL";
            this.seatsAvailable = 0; // Ensure available seats doesn't go negative
        }
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Long getEnrolledCount() {
        return enrolledCount;
    }

    public void setEnrolledCount(Long enrolledCount) {
        this.enrolledCount = enrolledCount;
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

