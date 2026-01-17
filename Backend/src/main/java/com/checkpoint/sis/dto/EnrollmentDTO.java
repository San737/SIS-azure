package com.checkpoint.sis.dto;

import com.checkpoint.sis.enums.EnrollmentStatus;
import com.checkpoint.sis.model.Enrollment;

import java.time.LocalDateTime;

public class EnrollmentDTO {

    private int enrollmentId;
    private String courseName;
    private String departmentName;
    private Integer credits;
    private EnrollmentStatus status; // e.g., REQUESTED, APPROVED, REJECTED
    private LocalDateTime requestedAt;
    private LocalDateTime approvalDate;

    // We can create this DTO from an Enrollment entity
    public EnrollmentDTO(Enrollment enrollment) {
        this.enrollmentId = enrollment.getEnrollmentId();
        this.status = enrollment.getStatus();
        this.requestedAt = enrollment.getRequestedAt();
        this.approvalDate = enrollment.getApprovalDate();

        // Get course details from the relationship
        if (enrollment.getCourse() != null) {
            this.courseName = enrollment.getCourse().getCourseName();
            this.credits = enrollment.getCourse().getCredits();

            // Get department details from the course
            if (enrollment.getCourse().getDepartment() != null) {
                this.departmentName = enrollment.getCourse().getDepartment().getDeptName();
            } else {
                this.departmentName = "N/A";
            }
        } else {
            this.courseName = "N/A";
        }
    }

    // Getters
    public int getEnrollmentId() { return enrollmentId; }
    public String getCourseName() { return courseName; }
    public String getDepartmentName() { return departmentName; }
    public Integer getCredits() { return credits; }
    public EnrollmentStatus getStatus() { return status; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public LocalDateTime getApprovalDate() { return approvalDate; }
}