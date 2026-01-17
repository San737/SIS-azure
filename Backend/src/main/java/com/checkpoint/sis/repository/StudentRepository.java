package com.checkpoint.sis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.checkpoint.sis.model.Student;
import com.checkpoint.sis.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {


	@Modifying
	@Transactional
	@Query("DELETE FROM Student s WHERE s.college.collegeId = :collegeId")
	void deleteStudentsByCollegeId(int collegeId);

	@Query("SELECT s FROM Student s WHERE s.student_id = :studentId")
	Optional<Student> findByStudentId(@Param("studentId") String studentId);

	@Query("""
		    SELECT s FROM Student s
		    WHERE s.college.collegeId = :collegeId
		      AND s.approvalStatus = com.checkpoint.sis.enums.Status.PENDING
		""")
		List<Student> findPendingStudentsByCollege(@Param("collegeId") Integer collegeId);





	

}
