package com.checkpoint.sis.controller;

import com.checkpoint.sis.dto.CollegeListingDTO;
import com.checkpoint.sis.dto.CollegeRequestDTO;
import com.checkpoint.sis.dto.DashboardStatsDTO;
import com.checkpoint.sis.enums.Status;
import com.checkpoint.sis.model.User;
import com.checkpoint.sis.repository.UserRepository;
// AdminService import is no longer needed
import com.checkpoint.sis.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/superadmin") // Endpoints for Super Admins ONLY
public class SuperAdminController {

    @Autowired
    private SuperAdminService superadminService; // This is the correct service instance

    @Autowired
    private UserRepository userRepository; // Needed to find the User entity for the Super Admin

    //Gives Dashboard Statistics
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        DashboardStatsDTO stats = superadminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
    
    /**
     * Approves a college admin's registration request.
     * Corresponds to: PATCH /api/college-admin/requests/:id/approve
     */
    @PutMapping("/requests/{requestId}/approve")
    public ResponseEntity<String> approveCollege(
            @PathVariable int requestId,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Fetch user entity from email
        User superAdmin = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Super Admin user not found"));

        // --- THIS IS THE FIX ---
        // Changed adminService to superadminService
        return superadminService.approveCollege(requestId, superAdmin);
    }

    /**
     * Rejects a college admin's registration request.
     * Corresponds to: PATCH /api/college-admin/requests/:id/reject
     */
    @PutMapping("/requests/{requestId}/reject")
    public ResponseEntity<String> rejectCollege(
            @PathVariable int requestId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User superAdmin = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Super Admin user not found"));

        // --- THIS IS THE FIX ---
        // Now calling the method we just built in the service
        return superadminService.rejectCollege(requestId, superAdmin);
    }

    /**
     * Retrieves all college admin registration requests.
     * This method was already correct.
     */
    @GetMapping("/requests")
    public ResponseEntity<List<CollegeRequestDTO>> getCollegeRequests(
            @RequestParam(required = false) Status status) {

        List<CollegeRequestDTO> requests = superadminService.getCollegeRequests(status);
        return ResponseEntity.ok(requests);
    }

    @DeleteMapping("/college/{collegeId}")
    public ResponseEntity<String> deleteCollege(@PathVariable int collegeId) {
        return superadminService.deleteCollege(collegeId);
    }
    
    @DeleteMapping("/college-requests/{requestId}")
    public ResponseEntity<String> deleteCollegeRequest(@PathVariable int requestId) {
        return superadminService.deleteCollegeRequest(requestId);
    }



    @GetMapping("/colleges")
    public ResponseEntity<List<CollegeListingDTO>> getAllColleges() {
        // We will create this 'getAllColleges' method in the SuperAdminService
        List<CollegeListingDTO> colleges = superadminService.getAllColleges();
        return ResponseEntity.ok(colleges);
    }

}