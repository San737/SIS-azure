package com.checkpoint.sis.dto;

import java.util.List;

public class DashboardResponseDTO {
    private List<CourseInfoDTO> enrolledCourses;
    private List<CourseInfoDTO> availableCourses;

    // Getters and Setters
    public List<CourseInfoDTO> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<CourseInfoDTO> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public List<CourseInfoDTO> getAvailableCourses() {
        return availableCourses;
    }

    public void setAvailableCourses(List<CourseInfoDTO> availableCourses) {
        this.availableCourses = availableCourses;
    }
}