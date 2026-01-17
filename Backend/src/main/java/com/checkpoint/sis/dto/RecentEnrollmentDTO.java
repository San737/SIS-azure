package com.checkpoint.sis.dto;

import java.time.LocalDateTime;

public class RecentEnrollmentDTO {
    private String studentName;
    private String courseName;
    private LocalDateTime enrollmentDate;

    // Constructor
    public RecentEnrollmentDTO(String studentName, String courseName, LocalDateTime enrollmentDate) {
        this.studentName = studentName;
        this.courseName = courseName;
        this.enrollmentDate = enrollmentDate;
    }

    // Getters and Setters
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}

