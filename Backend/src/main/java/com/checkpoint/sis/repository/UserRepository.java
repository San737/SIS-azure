package com.checkpoint.sis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.checkpoint.sis.model.User;

import jakarta.transaction.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);


	@Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.college.collegeId = :collegeId")
    void deleteUsersByCollegeId(int collegeId);
	
}
