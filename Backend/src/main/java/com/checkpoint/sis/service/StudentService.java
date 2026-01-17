package com.checkpoint.sis.service;

import com.checkpoint.sis.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentService {
	ResponseEntity<String> registerStudent(String fullName, String email, String password,Integer collegeId, Integer deptId, String phone, String address);

	DashboardResponseDTO getStudentDashboard(String userEmail);

	List<CourseListingDTO> getCourseList(String userEmail);
	// NEW: Method definition for fetching student profile
	StudentProfileDTO getStudentProfile(String userEmail);

	// NEW: Method definition for updating student profile
	void updateStudentProfile(String userEmail, StudentProfileUpdateRequestDTO updateRequest);

	ResponseEntity<String> enrollInCourse(String userEmail, int courseId);
	// NEW: Method definition for getting a student's enrollment history
	List<EnrollmentDTO> getStudentEnrollments(String userEmail);
}

