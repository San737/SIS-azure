package com.checkpoint.sis.dto;

public class PendingStudentDTO {

    private String studentId;
    private String fullName;
    private String email;
    private String departmentName;

    public PendingStudentDTO(String studentId, String fullName, String email, String departmentName) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.departmentName = departmentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}
