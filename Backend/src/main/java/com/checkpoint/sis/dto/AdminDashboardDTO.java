package com.checkpoint.sis.dto;

import java.util.List;

public class AdminDashboardDTO {
    private long totalCourses;
    private long totalDepartments;
    private long activeEnrollments;
    private List<RecentEnrollmentDTO> recentEnrollments;

    private List<AdminCourseStatusDTO> courseStatusList;

    // Getters and Setters
    public long getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public long getTotalDepartments() {
        return totalDepartments;
    }

    public void setTotalDepartments(long totalDepartments) {
        this.totalDepartments = totalDepartments;
    }

    public long getActiveEnrollments() {
        return activeEnrollments;
    }

    public void setActiveEnrollments(long activeEnrollments) {
        this.activeEnrollments = activeEnrollments;
    }

    public List<RecentEnrollmentDTO> getRecentEnrollments() {
        return recentEnrollments;
    }

    public void setRecentEnrollments(List<RecentEnrollmentDTO> recentEnrollments) {
        this.recentEnrollments = recentEnrollments;
    }

        public List<AdminCourseStatusDTO> getCourseStatusList() {
            return courseStatusList;
        }

        public void setCourseStatusList(List<AdminCourseStatusDTO> courseStatusList) {
            this.courseStatusList = courseStatusList;
    }
}

