package com.checkpoint.sis.dto;

import com.checkpoint.sis.enums.Status; // Assuming your user/student status enum is Status

import java.time.LocalDateTime;

public class StudentProfileDTO {
    private String studentId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String collegeName;
    private String departmentName;
    private LocalDateTime registrationDate; // This is likely the user's created_at
    private Status approvalStatus; // Assuming this comes from the Student entity

    // Constructor (optional, but good practice)
    public StudentProfileDTO(String studentId, String fullName, String email, String phone, String address, String collegeName, String departmentName, LocalDateTime registrationDate, Status approvalStatus) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.collegeName = collegeName;
        this.departmentName = departmentName;
        this.registrationDate = registrationDate;
        this.approvalStatus = approvalStatus;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Status getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Status approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
