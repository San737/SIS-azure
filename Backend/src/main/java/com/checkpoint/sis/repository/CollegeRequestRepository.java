package com.checkpoint.sis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.checkpoint.sis.enums.Status;
import com.checkpoint.sis.model.CollegeRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CollegeRequestRepository extends JpaRepository<CollegeRequest, Integer> {

	boolean existsByCollegeNameIgnoreCaseAndStatus(String normalizedName, Status pending);

	// NEW: For Super Admin to find requests by their status
	List<CollegeRequest> findByStatus(Status status);

	long countByStatus(Status status);

	// Find college request by college name (to delete associated request when
	// college is deleted)
	List<CollegeRequest> findByCollegeNameIgnoreCase(String collegeName);
	@Modifying
	@Transactional
	@Query("DELETE FROM CollegeRequest cr WHERE cr.requestedBy.college.collegeId = :collegeId")
	void deleteRequestsByCollegeId(int collegeId);

}
