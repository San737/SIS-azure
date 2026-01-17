package com.checkpoint.sis.dto;

public class DashboardStatsDTO {

    private long totalColleges;
    private long pendingRequests;
    private long approvedColleges;
    private long rejectedRequests;

    public DashboardStatsDTO(long totalColleges, long pendingRequests, long approvedColleges, long rejectedRequests) {
        this.totalColleges = totalColleges;
        this.pendingRequests = pendingRequests;
        this.approvedColleges = approvedColleges;
        this.rejectedRequests = rejectedRequests;
    }

    public long getTotalColleges() {
        return totalColleges;
    }

    public void setTotalColleges(long totalColleges) {
        this.totalColleges = totalColleges;
    }

    public long getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(long pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public long getApprovedColleges() {
        return approvedColleges;
    }

    public void setApprovedColleges(long approvedColleges) {
        this.approvedColleges = approvedColleges;
    }

    public long getRejectedRequests() {
        return rejectedRequests;
    }

    public void setRejectedRequests(long rejectedRequests) {
        this.rejectedRequests = rejectedRequests;
    }
}
