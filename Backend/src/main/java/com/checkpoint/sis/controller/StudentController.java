package com.checkpoint.sis.controller;

import com.checkpoint.sis.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.checkpoint.sis.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {
	@Autowired
    private StudentService studentService;
	
	@PostMapping("/register")
	public ResponseEntity<String> registerStudent(@RequestBody StudentRegisterRequest request) {
	    return studentService.registerStudent(
	        request.getFullName(),
	        request.getEmail(),
	        request.getPassword(),
	        request.getCollegeId(),
	        request.getDeptId(),
	        request.getPhone(),
	        request.getAddress()
	    );
	}

	@GetMapping("/dashboard")
	public ResponseEntity<DashboardResponseDTO> getStudentDashboard(@AuthenticationPrincipal UserDetails userDetails) {
		// Get the email of the currently logged-in user from the JWT
		String userEmail = userDetails.getUsername();

		// Call the service layer to fetch the dashboard data
		DashboardResponseDTO dashboardResponse = studentService.getStudentDashboard(userEmail);

		// Return the data with a 200 OK status
		return ResponseEntity.ok(dashboardResponse);
	}

	/**
	 * NEW: Endpoint for a student to get the full list of available courses.
	 * URL: GET /api/v1/student/courses
	 */
	@GetMapping("/courses")
	public ResponseEntity<List<CourseListingDTO>> getCourseList(@AuthenticationPrincipal UserDetails userDetails) {
		// Get the email of the currently logged-in user
		String userEmail = userDetails.getUsername();

		// Call the service layer to get the list of courses with their enrollment status
		List<CourseListingDTO> courses = studentService.getCourseList(userEmail);

		// Return the list with a 200 OK status
		return ResponseEntity.ok(courses);
	}

	@GetMapping("/profile")
	public ResponseEntity<StudentProfileDTO> getStudentProfile(@AuthenticationPrincipal UserDetails userDetails) {
		String userEmail = userDetails.getUsername();
		StudentProfileDTO profile = studentService.getStudentProfile(userEmail);
		return ResponseEntity.ok(profile);
	}

	/**
	 * NEW: Endpoint for a student to update their profile (name, phone, address).
	 * URL: PUT /api/v1/student/profile
	 */
	@PutMapping("/profile")
	public ResponseEntity<String> updateStudentProfile(
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody StudentProfileUpdateRequestDTO updateRequest) {
		String userEmail = userDetails.getUsername();
		studentService.updateStudentProfile(userEmail, updateRequest);
		return ResponseEntity.ok("Profile updated successfully.");
	}

	@PostMapping("/enroll")
	public ResponseEntity<String> enrollInCourse(
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody EnrollmentRequestDTO request) {

		String userEmail = userDetails.getUsername();
		return studentService.enrollInCourse(userEmail, request.getCourseId());
	}

	/**
	 * NEW: Endpoint for a student to get their full enrollment history.
	 * URL: GET /api/v1/student/enrollments
	 */
	@GetMapping("/enrollments")
	public ResponseEntity<List<EnrollmentDTO>> getStudentEnrollments(@AuthenticationPrincipal UserDetails userDetails) {
		String userEmail = userDetails.getUsername();
		List<EnrollmentDTO> enrollments = studentService.getStudentEnrollments(userEmail);
		return ResponseEntity.ok(enrollments);
	}
}
	

