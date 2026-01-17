package com.checkpoint.sis.repository;

import com.checkpoint.sis.model.College;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.checkpoint.sis.model.Department;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    // NEW: Used by Admin Dashboard - Counts unique departments offering courses in a specific college
    long countDistinctByCourses_College(College college);
    Optional<Department> findByDeptNameIgnoreCase(String deptName);

}
