package com.checkpoint.sis.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List; // <-- Added import
import java.util.Map;
import java.util.stream.Collectors; // <-- Added import

import com.checkpoint.sis.dto.AddCourseRequestDTO;
import com.checkpoint.sis.dto.AdminCourseStatusDTO;
import com.checkpoint.sis.enums.EnrollmentStatus;
import com.checkpoint.sis.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // <-- Added import
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.checkpoint.sis.dto.AdminDashboardDTO; // <-- Added import
import com.checkpoint.sis.dto.CollegeRegisterRequest;
import com.checkpoint.sis.dto.CourseListingDTO;
import com.checkpoint.sis.dto.PendingStudentDTO;
import com.checkpoint.sis.dto.RecentEnrollmentDTO; // <-- Added import
import com.checkpoint.sis.enums.Role;
import com.checkpoint.sis.enums.Status;
import com.checkpoint.sis.repository.CollegeRepository;
import com.checkpoint.sis.repository.CollegeRequestRepository;
import com.checkpoint.sis.repository.CourseRepository; // <-- Added import
import com.checkpoint.sis.repository.DepartmentRepository; // <-- Added import
import com.checkpoint.sis.repository.EnrollmentRepository; // <-- Added import
import com.checkpoint.sis.repository.UserRepository;
import com.checkpoint.sis.repository.StudentRepository;
import com.checkpoint.sis.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
	// --- Existing + New Repositories ---
	@Autowired
	private CollegeRepository collegeRepository;
	@Autowired
	private CollegeRequestRepository collegeRequestRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CourseRepository courseRepository; // <-- Added
	@Autowired
	private DepartmentRepository departmentRepository; // <-- Added
	@Autowired
	private EnrollmentRepository enrollmentRepository; // <-- Added
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// --- Your Teammate's Existing Methods (Unchanged) ---
	@Override
	public ResponseEntity<String> registerCollegeAdmin(CollegeRegisterRequest request) {
		String normalizedName = request.getCollegeName().trim();

		if (collegeRepository.existsByCollegeNameIgnoreCase(normalizedName)) {
			return ResponseEntity.badRequest().body("College already exists. Please login instead.");
		}

		if (collegeRequestRepository.existsByCollegeNameIgnoreCaseAndStatus(normalizedName, Status.PENDING)) {
			return ResponseEntity.badRequest()
					.body("A registration request for this college is already pending approval.");
		}
		User user = new User();
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.ADMIN);
		user.setStatus(Status.PENDING);
		userRepository.save(user);

		CollegeRequest collegeRequest = new CollegeRequest();
		collegeRequest.setCollegeName(request.getCollegeName());
		collegeRequest.setAddress(request.getAddress());
		collegeRequest.setLogoUrl(request.getLogoUrl());
		collegeRequest.setRequestedBy(user);
		collegeRequest.setStatus(Status.PENDING);
		collegeRequestRepository.save(collegeRequest);

		return ResponseEntity.ok("College registration request submitted successfully. Awaiting approval.");
	}

	/**
	 * NEW: Implementation for the admin dashboard logic.
	 */
	@Override
	public AdminDashboardDTO getDashboardData(String adminEmail) {
		// 1. Find the admin's user profile and their associated college
		User adminUser = userRepository.findByEmail(adminEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Admin user not found"));
		College adminCollege = adminUser.getCollege();
		if (adminCollege == null) {
			// This should ideally not happen for an approved admin, but good to check
			throw new RuntimeException("Admin is not associated with any college. College might be pending approval.");
		}

		// 2. Fetch the statistics for that specific college
		long totalCourses = courseRepository.countByCollege(adminCollege);
		long totalDepartments = departmentRepository.countDistinctByCourses_College(adminCollege);
		// Assuming 'APPROVED' status means an active enrollment for stats
		long activeEnrollments = enrollmentRepository.countByStudent_CollegeAndStatus(adminCollege,
				EnrollmentStatus.APPROVED);

		// 3. Fetch the 5 most recent enrollments for that college (using requested
		// date)
		List<Enrollment> recentEnrollmentsList = enrollmentRepository
				.findTop5ByStudent_CollegeOrderByRequestedAtDesc(adminCollege);

		// 4. Convert the enrollment entities into DTOs
		List<RecentEnrollmentDTO> recentEnrollmentDTOs = recentEnrollmentsList.stream()
				.map(enrollment -> new RecentEnrollmentDTO(
						enrollment.getStudent().getUser().getFullName(), // Navigate to get student name
						enrollment.getCourse().getCourseName(),
						enrollment.getRequestedAt() // Use the request date for recency
				))
				.collect(Collectors.toList());

		// 1. Get all courses for the admin's college
		List<Course> collegeCourses = courseRepository.findByCollege(adminCollege);

		// 2. Efficiently get enrollment counts for all these courses
		// We get a Map where Key = courseId, Value = count of approved enrollments
		Map<Integer, Long> enrollmentCounts = enrollmentRepository.findApprovedEnrollmentCountsForCourses(
				collegeCourses.stream().map(Course::getCourseId).collect(Collectors.toList()));

		// 3. Create the detailed course status DTO list
		List<AdminCourseStatusDTO> courseStatusList = collegeCourses.stream()
				.map(course -> {
					// Get the enrollment count for this specific course from the map (default to 0
					// if not found)
					Long enrolledCount = enrollmentCounts.getOrDefault(course.getCourseId(), 0L);

					// Create the DTO (constructor handles calculations)
					return new AdminCourseStatusDTO(
							course.getCourseId(),
							course.getCourseName(),
							course.getDepartment() != null ? course.getDepartment().getDeptName() : "N/A", // Handle
																											// potential
																											// null
																											// department
							course.getSeatLimit(), // Assuming seatLimit is in Course entity
							enrolledCount);
				})
				.collect(Collectors.toList());

		AdminDashboardDTO dashboardData = new AdminDashboardDTO();
		dashboardData.setTotalCourses(totalCourses);
		dashboardData.setTotalDepartments(totalDepartments);
		dashboardData.setActiveEnrollments(activeEnrollments);
		dashboardData.setRecentEnrollments(recentEnrollmentDTOs);
		dashboardData.setCourseStatusList(courseStatusList); // Add the new list

		return dashboardData;
	}

	@Override
	public List<CourseListingDTO> getAllCoursesForCollege(String adminEmail) {
		User admin = userRepository.findByEmail(adminEmail)
				.orElseThrow(() -> new RuntimeException("Admin not found"));

		College college = admin.getCollege();
		if (college == null)
			throw new RuntimeException("Admin is not linked to any college.");

		// Get all courses for this college
		List<Course> courses = courseRepository.findByCollege(college);

		// Get enrolled count for each course (you can adjust as needed)
		Map<Integer, Long> enrollmentCounts = enrollmentRepository.findApprovedEnrollmentCountsForCourses(
				courses.stream().map(Course::getCourseId).collect(Collectors.toList()));

		return courses.stream()
				.map(course -> {
					Long enrolled = enrollmentCounts.getOrDefault(course.getCourseId(), 0L);
					Integer seatLimit = course.getSeatLimit();
					Integer seatsAvailable = (seatLimit != null) ? (int) (seatLimit - enrolled) : null;
					Boolean isFull = (seatLimit != null) ? (enrolled >= seatLimit) : false;
					String status = isFull ? "FULL" : "AVAILABLE";
					String deptName = (course.getDepartment() != null) ? course.getDepartment().getDeptName() : "N/A";

					return new CourseListingDTO(
							course.getCourseId(),
							course.getCourseName(),
							course.getDescription(),
							course.getCredits(),
							course.getStartDate(),
							course.getEndDate(),
							seatLimit,
							seatsAvailable,
							isFull,
							status,
							deptName);
				})
				.collect(Collectors.toList());
	}

	@Override
	public ResponseEntity<String> addCourse(String adminEmail, AddCourseRequestDTO dto) {
		User admin = userRepository.findByEmail(adminEmail)
				.orElseThrow(() -> new RuntimeException("Admin not found"));

		College college = admin.getCollege();
		if (college == null)
			return ResponseEntity.badRequest().body("Admin not linked to any college.");

		Course course = new Course();
		course.setCourseName(dto.getCourseName());
		course.setDescription(dto.getDescription());
		course.setCredits(dto.getCredits());
		course.setSeatLimit(dto.getSeatLimit());

		course.setCollege(college);
		course.setCreatedBy(admin);

		// Find department by deptId
		if (dto.getDeptId() != null) {
			Department dept = departmentRepository.findById(dto.getDeptId())
					.orElseThrow(() -> new RuntimeException("Department not found with ID: " + dto.getDeptId()));
			course.setDepartment(dept);
		}

		if (dto.getStartDate() != null)
			course.setStartDate(LocalDate.parse(dto.getStartDate()));
		if (dto.getEndDate() != null)
			course.setEndDate(LocalDate.parse(dto.getEndDate()));

		courseRepository.save(course);
		return ResponseEntity.ok("Course added successfully to " + college.getCollegeName());
	}

	@Override
	public ResponseEntity<String> deleteCourse(String adminEmail, int courseId) {
		User admin = userRepository.findByEmail(adminEmail)
				.orElseThrow(() -> new RuntimeException("Admin not found"));

		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new RuntimeException("Course not found"));

		if (!course.getCollege().equals(admin.getCollege())) {
			return ResponseEntity.status(403).body("You cannot delete courses from another college.");
		}

		courseRepository.delete(course);
		return ResponseEntity.ok("Course deleted successfully.");
	}
	
	
	@Override
	public List<PendingStudentDTO> getPendingStudents(String adminEmail) {

	    User admin = userRepository.findByEmail(adminEmail)
	            .orElseThrow(() -> new RuntimeException("Admin not found"));

	    Integer collegeId = admin.getCollege().getCollegeId();

	    List<Student> students =
	            studentRepository.findPendingStudentsByCollege(collegeId);

	    return students.stream()
	            .map(s -> new PendingStudentDTO(
	                    s.getStudent_id(),
	                    s.getUser().getFullName(),
	                    s.getUser().getEmail(),
	                    s.getDepartment().getDeptName()
	            ))
	            .toList();
	}


}
