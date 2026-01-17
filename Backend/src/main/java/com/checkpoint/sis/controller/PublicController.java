package com.checkpoint.sis.controller;

import com.checkpoint.sis.dto.CollegeBasicDTO;
import com.checkpoint.sis.dto.DepartmentDTO;
import com.checkpoint.sis.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    // We will create this service in the next step
    @Autowired
    private PublicService publicService;

    /**
     * Endpoint to get a list of all APPROVED colleges.
     * This is public and requires no authentication.
     * Used for login and registration dropdowns.
     *
     * URL: GET /api/v1/public/colleges
     */
    @GetMapping("/colleges")
    public ResponseEntity<List<CollegeBasicDTO>> getApprovedColleges() {
        List<CollegeBasicDTO> colleges = publicService.getApprovedColleges();
        return ResponseEntity.ok(colleges);
    }

    /**
     * Endpoint to get a list of all departments.
     * This is public and requires no authentication.
     * Used for course creation and registration dropdowns.
     *
     * URL: GET /api/v1/public/departments
     */
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departments = publicService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
}