package com.checkpoint.sis.repository;

import com.checkpoint.sis.enums.EnrollmentStatus;
import com.checkpoint.sis.enums.Status;
import com.checkpoint.sis.model.College;
import com.checkpoint.sis.model.Course;
import com.checkpoint.sis.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.checkpoint.sis.model.Enrollment;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    List<Enrollment> findByStudent(Student student);

    // NEW: Used by Admin Dashboard - Counts approved enrollments for a specific
    // college
    long countByStudent_CollegeAndStatus(College college, EnrollmentStatus status);

    // NEW: Used by Admin Dashboard - Gets the 5 most recent enrollments for a
    // specific college
    List<Enrollment> findTop5ByStudent_CollegeOrderByRequestedAtDesc(College college);

    // NEW: Used by Admin Dashboard - Efficiently gets counts of approved
    // enrollments for a list of course IDs
    @Query("SELECT e.course.courseId, COUNT(e) FROM Enrollment e WHERE e.course.courseId IN :courseIds AND e.status = :status GROUP BY e.course.courseId")
    List<Object[]> findApprovedEnrollmentCountsForCourseIds(@Param("courseIds") List<Integer> courseIds,
            @Param("status") EnrollmentStatus status);

    // Helper method to convert the query result into a Map
    default Map<Integer, Long> findApprovedEnrollmentCountsForCourses(List<Integer> courseIds) {
        if (courseIds == null || courseIds.isEmpty()) {
            return Collections.emptyMap(); // Return empty map if no courses to check
        }
        List<Object[]> results = findApprovedEnrollmentCountsForCourseIds(courseIds, EnrollmentStatus.APPROVED);
        return results.stream().collect(Collectors.toMap(
                result -> (Integer) result[0], // courseId
                result -> (Long) result[1] // count
        ));
    }

    /**
     * Checks if an enrollment record already exists for this student and course.
     */
    boolean existsByStudentAndCourse(Student student, Course course);

    /**
     * Counts enrollments for a specific course with a specific status (e.g., to
     * check seat limits).
     */
    long countByCourseAndStatus(Course course, EnrollmentStatus status);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query("DELETE FROM Enrollment e WHERE e.student.college.collegeId = :collegeId")
    void deleteEnrollmentsByCollegeId(@Param("collegeId") int collegeId);
}
