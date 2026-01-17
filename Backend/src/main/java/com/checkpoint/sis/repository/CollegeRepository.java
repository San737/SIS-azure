package com.checkpoint.sis.repository;

import com.checkpoint.sis.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.checkpoint.sis.model.College;

import java.util.List;

@Repository
public interface CollegeRepository extends JpaRepository<College, Integer> {

	boolean existsByCollegeNameIgnoreCase(String normalizedName);
	// NEW: Used by Admin Registration - Checks if college name exists (case-insensitive)
	List<College> findByStatus(Status status);



}
