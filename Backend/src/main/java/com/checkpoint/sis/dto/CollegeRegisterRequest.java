package com.checkpoint.sis.dto;

public class CollegeRegisterRequest {
    
    private String fullName;        // College Admin full name
    private String email;           // Admin email
    private String password;        // Admin password
    private String collegeName;     // Name of the college
    private String address;         // College address
    private String logoUrl;         // Optional: College logo URL (if provided)

    // ✅ Getters and Setters
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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

    public String getLogoUrl() {
        return logoUrl;
    }
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
