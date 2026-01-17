package com.checkpoint.sis.service;

import com.checkpoint.sis.dto.CollegeListingDTO;
import com.checkpoint.sis.dto.CollegeRequestDTO;
import com.checkpoint.sis.dto.DashboardStatsDTO;
import com.checkpoint.sis.enums.Status;
import com.checkpoint.sis.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SuperAdminService {
    ResponseEntity<String> approveCollege(int requestId, User superAdmin);

    /**
     * Retrieves all college admin registration requests, optionally filtered by status.
     */
    List<CollegeRequestDTO> getCollegeRequests(Status status);
    ResponseEntity<String> rejectCollege(int requestId, User superAdmin);
    ResponseEntity<String> deleteCollege(int collegeId);
    List<CollegeListingDTO> getAllColleges();
    DashboardStatsDTO getDashboardStats();

	ResponseEntity<String> deleteCollegeRequest(int requestId);

}

