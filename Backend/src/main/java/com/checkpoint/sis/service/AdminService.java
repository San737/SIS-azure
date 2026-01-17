package com.checkpoint.sis.service;

import com.checkpoint.sis.dto.AddCourseRequestDTO;
import com.checkpoint.sis.dto.AdminDashboardDTO;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.checkpoint.sis.dto.CollegeRegisterRequest;
import com.checkpoint.sis.dto.CourseListingDTO;
import com.checkpoint.sis.dto.PendingStudentDTO;
import com.checkpoint.sis.dto.PendingStudentDTO;
import com.checkpoint.sis.model.Course;
import com.checkpoint.sis.model.User;

public interface AdminService {

    // When a college admin tries to register a new college
    ResponseEntity<String> registerCollegeAdmin(CollegeRegisterRequest request);

    AdminDashboardDTO getDashboardData(String adminEmail);
    
    List<CourseListingDTO> getAllCoursesForCollege(String adminEmail);
    
    ResponseEntity<String> addCourse(String adminEmail, AddCourseRequestDTO dto);
    
    ResponseEntity<String> deleteCourse(String adminEmail, int courseId);

    List<PendingStudentDTO> getPendingStudents(String adminEmail);


}
