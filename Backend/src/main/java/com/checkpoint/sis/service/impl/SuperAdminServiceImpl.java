package com.checkpoint.sis.service.impl;

import com.checkpoint.sis.dto.CollegeListingDTO;
import com.checkpoint.sis.dto.CollegeRequestDTO;
import com.checkpoint.sis.dto.DashboardStatsDTO;
import com.checkpoint.sis.enums.Role;
import com.checkpoint.sis.enums.Status;
import com.checkpoint.sis.model.College;
import com.checkpoint.sis.model.CollegeRequest;
import com.checkpoint.sis.model.User;
import com.checkpoint.sis.repository.CollegeRepository;
import com.checkpoint.sis.repository.CollegeRequestRepository;
import com.checkpoint.sis.repository.StudentRepository;
import com.checkpoint.sis.repository.UserRepository;
import com.checkpoint.sis.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    // Repositories needed for Super Admin tasks
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private CollegeRequestRepository collegeRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private com.checkpoint.sis.repository.CourseRepository courseRepository;
    @Autowired
    private com.checkpoint.sis.repository.EnrollmentRepository enrollmentRepository;

    @Override
    public ResponseEntity<String> approveCollege(int requestId, User superAdmin) {
        CollegeRequest collegeRequest = collegeRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("College request not found"));

        if (collegeRequest.getStatus() != Status.PENDING) {
            return ResponseEntity.badRequest().body("This request has already been processed.");
        }

        College college = new College();
        college.setCollegeName(collegeRequest.getCollegeName());
        college.setAddress(collegeRequest.getAddress());
        college.setStatus(Status.APPROVED);
        college.setLogoUrl(collegeRequest.getLogoUrl());
        collegeRepository.save(college);

        User adminUser = collegeRequest.getRequestedBy();
        adminUser.setStatus(Status.APPROVED);
        adminUser.setRole(Role.ADMIN);
        adminUser.setCollege(college);
        userRepository.save(adminUser);

        collegeRequest.setStatus(Status.APPROVED);
        collegeRequest.setApprovedBy(superAdmin);
        collegeRequest.setApprovalDate(LocalDateTime.now());
        collegeRequestRepository.save(collegeRequest);

        return ResponseEntity.ok("College approved and admin account activated.");
    }

    @Override
    public List<CollegeRequestDTO> getCollegeRequests(Status status) {
        List<CollegeRequest> requests;

        if (status != null) {
            // If a status is provided, filter by that status
            requests = collegeRequestRepository.findByStatus(status);
        } else {
            // If no status is provided, get all requests
            requests = collegeRequestRepository.findAll();
        }

        // Convert the list of entities to a list of DTOs
        return requests.stream()
                .map(request -> new CollegeRequestDTO(
                        request.getRequestId(),
                        request.getCollegeName(),
                        request.getAddress(), // <-- THE FIX IS HERE
                        request.getRequestedBy().getEmail(),
                        request.getRequestedBy().getFullName(),
                        request.getRequestDate(),
                        request.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<String> rejectCollege(int requestId, User superAdmin) {
        // Step 1: Find the request
        CollegeRequest collegeRequest = collegeRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("College request not found"));

        // Step 2: Check if it's already processed
        if (collegeRequest.getStatus() != Status.PENDING) {
            return ResponseEntity.badRequest().body("This request has already been processed.");
        }

        // Step 3: Update the associated User's status to REJECTED
        User adminUser = collegeRequest.getRequestedBy();
        adminUser.setStatus(Status.REJECTED);
        userRepository.save(adminUser);

        // Step 4: Update the CollegeRequest itself
        collegeRequest.setStatus(Status.REJECTED);
        collegeRequest.setApprovedBy(superAdmin); // The Super Admin is the one who processed it
        collegeRequest.setApprovalDate(LocalDateTime.now()); // Set the processing date
        collegeRequestRepository.save(collegeRequest);

        return ResponseEntity.ok("College request rejected successfully.");
    }

    @Override
    public List<CollegeListingDTO> getAllColleges() {
        // Step 1: Find all colleges with an APPROVED status
        List<College> colleges = collegeRepository.findByStatus(Status.APPROVED);

        // Step 2: Convert the list of entities to a list of DTOs
        return colleges.stream()
                .map(college -> new CollegeListingDTO(
                        college.getCollegeId(),
                        college.getCollegeName(),
                        college.getAddress(),
                        college.getLogoUrl(),
                        college.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public DashboardStatsDTO getDashboardStats() {
        long totalColleges = collegeRepository.count();
        long pendingRequests = collegeRequestRepository.countByStatus(Status.PENDING);
        long approvedColleges = collegeRequestRepository.countByStatus(Status.APPROVED);
        long rejectedRequests = collegeRequestRepository.countByStatus(Status.REJECTED);

        return new DashboardStatsDTO(
                totalColleges,
                pendingRequests,
                approvedColleges,
                rejectedRequests);
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteCollege(int collegeId) {

        College college = collegeRepository.findById(collegeId)
                .orElse(null);

        if (college == null) {
            return ResponseEntity.badRequest().body("College not found!");
        }

        // Cascade delete in order: enrollments -> courses -> students -> users ->
        // college requests -> college
        // Step 1: delete all enrollments for students in this college
        enrollmentRepository.deleteEnrollmentsByCollegeId(collegeId);

        // Step 2: delete all courses offered by this college
        courseRepository.deleteCoursesByCollegeId(collegeId);

        // Step 3: delete all students from this college
        studentRepository.deleteStudentsByCollegeId(collegeId);

        // 4. College Requests (Child of User)
        // FIX: Use the new ID-based delete method.
        // This deletes any request made by a user who belongs to this college.
        collegeRequestRepository.deleteRequestsByCollegeId(collegeId);

        /// 5. Users (Child of College)
        userRepository.deleteUsersByCollegeId(collegeId);

        // Step 6: finally delete the college itself
        collegeRepository.delete(college);

        return ResponseEntity.ok("College and all associated data deleted successfully.");
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteCollegeRequest(int requestId) {

        CollegeRequest request = collegeRequestRepository.findById(requestId)
                .orElse(null);

        if (request == null) {
            return ResponseEntity.badRequest().body("College request not found!");
        }

        collegeRequestRepository.delete(request);

        return ResponseEntity.ok("College request deleted successfully.");
    }

}
