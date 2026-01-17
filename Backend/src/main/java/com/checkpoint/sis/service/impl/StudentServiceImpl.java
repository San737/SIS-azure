package com.checkpoint.sis.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.checkpoint.sis.dto.*;
import com.checkpoint.sis.enums.EnrollmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- Added import

// Ensure correct Status enum is imported if needed elsewhere
import com.checkpoint.sis.enums.Role;
import com.checkpoint.sis.enums.Status;
import com.checkpoint.sis.model.College;
import com.checkpoint.sis.model.Course;
import com.checkpoint.sis.model.Department;
import com.checkpoint.sis.model.Enrollment;
import com.checkpoint.sis.model.Student;
import com.checkpoint.sis.model.User;
import com.checkpoint.sis.repository.CollegeRepository;
import com.checkpoint.sis.repository.CourseRepository;
import com.checkpoint.sis.repository.DepartmentRepository;
import com.checkpoint.sis.repository.EnrollmentRepository;
import com.checkpoint.sis.repository.StudentRepository;
import com.checkpoint.sis.repository.UserRepository;
import com.checkpoint.sis.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- Existing Methods ---
    
    private void validateApprovedStudent(Student student) {
        if (student.getApprovalStatus() != Status.APPROVED ||
            student.getUser().getStatus() != Status.APPROVED) {
            throw new RuntimeException("Student not approved yet");
        }
    }

    @Override
    @Transactional // Good practice for registration
    public ResponseEntity<String> registerStudent(String fullName, String email, String password, Integer collegeId,
            Integer deptId, String phone, String address) {

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists!");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.STUDENT);
        user.setStatus(Status.PENDING);

        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new RuntimeException("College not found"));
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        user.setCollege(college);
        userRepository.save(user);

        Student student = new Student();
        student.setStudent_id(UUID.randomUUID().toString().substring(0, 15));
        student.setUser(user);
        student.setCollege(college);
        student.setDepartment(department);
        student.setPhone(phone);
        student.setAddress(address);
        student.setApprovalStatus(Status.PENDING);
        studentRepository.save(student);

        return ResponseEntity.ok("Student registered successfully! Wait For Approval from your College Admin");
    }

    @Override
    public DashboardResponseDTO getStudentDashboard(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));
        Student student = user.getStudent();
        if (student == null) {
            throw new RuntimeException("Student profile not found for user: " + userEmail);
        }
        validateApprovedStudent(student);
        Department department = student.getDepartment();
        if (department == null) {
            throw new RuntimeException("Student is not associated with any department: " + userEmail);
        }
        List<Course> departmentCourses = courseRepository.findByDepartment(department);
        List<Enrollment> studentEnrollments = enrollmentRepository.findByStudent(student);

        Set<Integer> enrolledCourseIds = studentEnrollments.stream()
                .map(enrollment -> enrollment.getCourse().getCourseId())
                .collect(Collectors.toSet());

        List<CourseInfoDTO> enrolledCourses = departmentCourses.stream()
                .filter(course -> enrolledCourseIds.contains(course.getCourseId()))
                .map(course -> new CourseInfoDTO(course.getCourseId(), course.getCourseName(), course.getCredits()))
                .collect(Collectors.toList());

        List<CourseInfoDTO> availableCourses = departmentCourses.stream()
                .filter(course -> !enrolledCourseIds.contains(course.getCourseId()))
                .map(course -> new CourseInfoDTO(course.getCourseId(), course.getCourseName(), course.getCredits()))
                .collect(Collectors.toList());

        DashboardResponseDTO dashboardResponse = new DashboardResponseDTO();
        dashboardResponse.setEnrolledCourses(enrolledCourses);
        dashboardResponse.setAvailableCourses(availableCourses);

        return dashboardResponse;
    }

    @Override
    public List<CourseListingDTO> getCourseList(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));
        Student student = user.getStudent();
        if (student == null) {
            throw new RuntimeException("Student profile not found for user: " + userEmail);
        }
        validateApprovedStudent(student);
        Department department = student.getDepartment();
        if (department == null) {
            throw new RuntimeException("Student is not associated with any department: " + userEmail);
        }
        List<Course> departmentCourses = courseRepository.findByDepartment(department);
        List<Enrollment> studentEnrollments = enrollmentRepository.findByStudent(student);

        Set<Integer> enrolledCourseIds = studentEnrollments.stream()
                .map(enrollment -> enrollment.getCourse().getCourseId())
                .collect(Collectors.toSet());

        return departmentCourses.stream()
                .map(course -> {
                    String status = enrolledCourseIds.contains(course.getCourseId()) ? "ENROLLED" : "AVAILABLE";

                    // Calculate seats available
                    Integer seatLimit = course.getSeatLimit();
                    long approvedEnrollments = enrollmentRepository.countByCourseAndStatus(course,
                            EnrollmentStatus.APPROVED);
                    Integer seatsAvailable = (seatLimit != null) ? (int) (seatLimit - approvedEnrollments) : null;
                    Boolean isFull = (seatLimit != null) ? (approvedEnrollments >= seatLimit) : false;

                    // Get department name
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

    /**
     * NEW: Implementation for fetching student profile.
     */
    @Override
    public StudentProfileDTO getStudentProfile(String userEmail) {
        // 1. Find the user and associated student profile
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));
        Student student = user.getStudent();
        if (student == null) {
            // This could happen if registration wasn't fully complete or data issue
            throw new RuntimeException("Student profile not found for user: " + userEmail);
        }
        validateApprovedStudent(student);

        // 2. Get related entities (handle potential nulls gracefully)
        College college = student.getCollege();
        Department department = student.getDepartment();

        // 3. Assemble and return the DTO
        return new StudentProfileDTO(
                student.getStudent_id(), // Assuming method is getStudent_id()
                user.getFullName(),
                user.getEmail(),
                student.getPhone(),
                student.getAddress(),
                college != null ? college.getCollegeName() : "N/A", // Check for null college
                department != null ? department.getDeptName() : "N/A", // Check for null department
                user.getCreatedAt(), // Assuming User model has getCreatedAt()
                student.getApprovalStatus() // Assuming Student model has getApprovalStatus()
        );
    }

    /**
     * NEW: Implementation for updating student profile.
     */
    @Override
    @Transactional // Use Transactional for updates involving multiple saves
    public void updateStudentProfile(String userEmail, StudentProfileUpdateRequestDTO updateRequest) {
        // 1. Find the user and associated student profile
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));
        Student student = user.getStudent();
        if (student == null) {
            throw new RuntimeException("Student profile not found for user: " + userEmail);
        }

        // 2. Update the allowed fields (with checks to avoid nulling fields
        // unintentionally)
        // Update User object
        if (updateRequest.getFullName() != null) {
            // Add validation if needed (e.g., check for empty string)
            user.setFullName(updateRequest.getFullName());
        }

        // Update Student object
        if (updateRequest.getPhone() != null) {
            student.setPhone(updateRequest.getPhone());
        }
        if (updateRequest.getAddress() != null) {
            student.setAddress(updateRequest.getAddress());
        }

        // 3. Save changes to both entities
        userRepository.save(user);
        studentRepository.save(student);
    }

    /**
     * NEW: Implementation for enrolling in a course.
     */
    @Override
    @Transactional
    public ResponseEntity<String> enrollInCourse(String userEmail, int courseId) {
        // 1. Find the student
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));
        Student student = user.getStudent();
        if (student == null) {
            return ResponseEntity.badRequest().body("Student profile not found.");
        }
        validateApprovedStudent(student);
        // 2. Find the course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        // 3. Check if student's department matches course department
        if (student.getDepartment() == null || !student.getDepartment().equals(course.getDepartment())) {
            return ResponseEntity.badRequest().body("You can only enroll in courses from your own department.");
        }

        // 4. Check if already enrolled or requested
        boolean alreadyEnrolled = enrollmentRepository.existsByStudentAndCourse(student, course);
        if (alreadyEnrolled) {
            return ResponseEntity.badRequest().body("You have already enrolled in or requested this course.");
        }

        // 5. Check seat limits (if seat_limit is not null)
        if (course.getSeatLimit() != null) {
            long approvedCount = enrollmentRepository.countByCourseAndStatus(course, EnrollmentStatus.APPROVED);
            if (approvedCount >= course.getSeatLimit()) {
                return ResponseEntity.badRequest().body("This course is already full.");
            }
        }

        // 6. Create the new enrollment request
        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setStudent(student);
        newEnrollment.setCourse(course);
        newEnrollment.setStatus(EnrollmentStatus.APPROVED);
        newEnrollment.setApprovalDate(LocalDateTime.now()); // Set the approval date

        enrollmentRepository.save(newEnrollment);

        return ResponseEntity.ok("You have been successfully enrolled in the course");
    }

    /**
     * NEW: Implementation for getting a student's enrollment history.
     */
    @Override
    public List<EnrollmentDTO> getStudentEnrollments(String userEmail) {
        // 1. Find the logged-in user and their student profile
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));
        Student student = user.getStudent();
        if (student == null) {
            throw new RuntimeException("Student profile not found for user: " + userEmail);
        }
        validateApprovedStudent(student);
        // 2. Find all enrollments for this student (using the existing repo method)
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

        // 3. Convert the list of Enrollment entities into a list of EnrollmentDTOs
        return enrollments.stream()
                .map(enrollment -> new EnrollmentDTO(enrollment)) // Uses the DTO's constructor
                .collect(Collectors.toList());
    }
}
