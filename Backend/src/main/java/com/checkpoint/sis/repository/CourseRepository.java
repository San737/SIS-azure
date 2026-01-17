package com.checkpoint.sis.repository;

import com.checkpoint.sis.model.College;
import com.checkpoint.sis.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.checkpoint.sis.model.Course;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByDepartment(Department department);

    // NEW: Used by Admin Dashboard - Counts all courses for a specific college
    long countByCollege(College college);

    // NEW: Used by Admin Dashboard - Finds all courses for a specific college
    List<Course> findByCollege(College college);

    @Modifying
    @Transactional
    @Query("DELETE FROM Course c WHERE c.college.collegeId = :collegeId")
    void deleteCoursesByCollegeId(int collegeId);
}
