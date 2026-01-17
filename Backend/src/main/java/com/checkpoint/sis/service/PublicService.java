package com.checkpoint.sis.service;

import com.checkpoint.sis.dto.CollegeBasicDTO;
import com.checkpoint.sis.dto.DepartmentDTO;
import java.util.List;

public interface PublicService {

    /**
     * Retrieves a list of all colleges that are currently APPROVED.
     * 
     * @return A list of CollegeBasicDTO objects.
     */
    List<CollegeBasicDTO> getApprovedColleges();

    /**
     * Retrieves a list of all departments.
     * 
     * @return A list of DepartmentDTO objects.
     */
    List<DepartmentDTO> getAllDepartments();

}