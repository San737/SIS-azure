package com.checkpoint.sis.dto;

import com.checkpoint.sis.enums.Status;
import java.time.LocalDateTime;

public class CollegeRequestDTO {

    private int requestId;
    private String collegeName;
    private String address;
    private String requestedByEmail;
    private String requestedByName;
    private LocalDateTime requestDate;
    private Status status;

    // We can add more fields if the Super Admin needs them, like logoUrl

    // Constructor
    public CollegeRequestDTO(int requestId, String collegeName, String address, String requestedByEmail, String requestedByName, LocalDateTime requestDate, Status status) {
        this.requestId = requestId;
        this.collegeName = collegeName;
        this.address = address;
        this.requestedByEmail = requestedByEmail;
        this.requestedByName = requestedByName;
        this.requestDate = requestDate;
        this.status = status;
    }

    // Getters and Setters
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
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

    public String getRequestedByEmail() {
        return requestedByEmail;
    }

    public void setRequestedByEmail(String requestedByEmail) {
        this.requestedByEmail = requestedByEmail;
    }

    public String getRequestedByName() {
        return requestedByName;
    }

    public void setRequestedByName(String requestedByName) {
        this.requestedByName = requestedByName;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


}