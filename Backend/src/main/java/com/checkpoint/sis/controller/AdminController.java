package com.checkpoint.sis.controller;

import com.checkpoint.sis.dto.AddCourseRequestDTO;

import com.checkpoint.sis.dto.AdminDashboardDTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.checkpoint.sis.dto.CollegeRegisterRequest;
import com.checkpoint.sis.dto.CourseListingDTO;
import com.checkpoint.sis.dto.PendingStudentDTO;
import com.checkpoint.sis.model.Course;
import com.checkpoint.sis.model.User;
import com.checkpoint.sis.repository.StudentRepository;
import com.checkpoint.sis.repository.UserRepository;
import com.checkpoint.sis.service.AdminService;
import com.checkpoint.sis.enums.Status;
import com.checkpoint.sis.model.Student;



@RestController
@RequestMapping("/api/v1/college")
public class AdminController {
	@Autowired
	private AdminService adminService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    
	@PostMapping("/register")
    public ResponseEntity<String> registerCollegeAdmin(@RequestBody CollegeRegisterRequest request) {
        return adminService.registerCollegeAdmin(request);
    }


	@GetMapping("/dashboard")
	public ResponseEntity<AdminDashboardDTO> getAdminDashboard(@AuthenticationPrincipal UserDetails userDetails) {
		// Get the email of the currently logged-in admin
		String adminEmail = userDetails.getUsername();

		// Call the service layer to fetch the dashboard data for the admin's college
		// We will add this method to the AdminService next.
		AdminDashboardDTO dashboardData = adminService.getDashboardData(adminEmail);

		// Return the data with a 200 OK status
		return ResponseEntity.ok(dashboardData);
	}
	

	@GetMapping("/courses")
	public ResponseEntity<List<CourseListingDTO>> getCourses(@AuthenticationPrincipal UserDetails userDetails) {
	    return ResponseEntity.ok(adminService.getAllCoursesForCollege(userDetails.getUsername()));
	}

	@PostMapping("/addCourse")
	public ResponseEntity<String> addCourse(@AuthenticationPrincipal UserDetails userDetails,
	                                        @RequestBody AddCourseRequestDTO request) {
	    return adminService.addCourse(userDetails.getUsername(), request);
	}

	@DeleteMapping("/deleteCourse/{courseId}")
	public ResponseEntity<String> deleteCourse(@AuthenticationPrincipal UserDetails userDetails,
	                                           @PathVariable int courseId) {
	    return adminService.deleteCourse(userDetails.getUsername(), courseId);
	}
	
	@PutMapping("/approve-student/{studentId}")
	public ResponseEntity<String> approveStudent(
	        @PathVariable String studentId,
	        @AuthenticationPrincipal UserDetails userDetails) {

	    // 1️⃣ Get logged-in admin
	    User admin = userRepository.findByEmail(userDetails.getUsername())
	            .orElseThrow(() -> new RuntimeException("Admin not found"));

	    // 2️⃣ Get student
	    Student student = studentRepository.findByStudentId(studentId)
	            .orElseThrow(() -> new RuntimeException("Student not found"));

	    // 3️⃣ College isolation check
	    if (!admin.getCollege().getCollegeId()
	            .equals(student.getCollege().getCollegeId())) {
	        return ResponseEntity.status(403).body("Unauthorized action");
	    }

	    // 4️⃣ Approve student
	    student.setApprovalStatus(Status.APPROVED);
	    student.getUser().setStatus(Status.APPROVED);

	    studentRepository.save(student);
	    userRepository.save(student.getUser());

	    return ResponseEntity.ok("Student approved successfully");
	}
	
	@PutMapping("/reject-student/{studentId}")
	public ResponseEntity<String> rejectStudent(
	        @PathVariable String studentId,
	        @AuthenticationPrincipal UserDetails userDetails) {

	    User admin = userRepository.findByEmail(userDetails.getUsername())
	            .orElseThrow(() -> new RuntimeException("Admin not found"));

	    Student student = studentRepository.findByStudentId(studentId)
	            .orElseThrow(() -> new RuntimeException("Student not found"));

	    if (!admin.getCollege().getCollegeId()
	            .equals(student.getCollege().getCollegeId())) {
	        return ResponseEntity.status(403).body("Unauthorized action");
	    }

	    student.setApprovalStatus(Status.REJECTED);
	    student.getUser().setStatus(Status.REJECTED);

	    studentRepository.save(student);
	    userRepository.save(student.getUser());

	    return ResponseEntity.ok("Student rejected successfully");
	}

	@GetMapping("/students/pending")
	public ResponseEntity<List<PendingStudentDTO>> getPendingStudents(
	        @AuthenticationPrincipal UserDetails userDetails) {

	    return ResponseEntity.ok(
	            adminService.getPendingStudents(userDetails.getUsername())
	    );
	}



}
