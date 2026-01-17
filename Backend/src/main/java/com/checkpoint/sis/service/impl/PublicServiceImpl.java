package com.checkpoint.sis.service.impl;

import com.checkpoint.sis.dto.CollegeBasicDTO;
import com.checkpoint.sis.dto.DepartmentDTO;
import com.checkpoint.sis.enums.Status;
import com.checkpoint.sis.model.College;
import com.checkpoint.sis.model.Department;
import com.checkpoint.sis.repository.CollegeRepository;
import com.checkpoint.sis.repository.DepartmentRepository;
import com.checkpoint.sis.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicServiceImpl implements PublicService {

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<CollegeBasicDTO> getApprovedColleges() {
        // 1. Fetch all colleges from the database that have the status 'APPROVED'
        // We already built the findByStatus method in our CollegeRepository!
        List<College> approvedColleges = collegeRepository.findByStatus(Status.APPROVED);

        // 2. Convert (map) the list of College entities into a list of CollegeBasicDTOs
        return approvedColleges.stream()
                .map(college -> new CollegeBasicDTO(
                        college.getCollegeId(),
                        college.getCollegeName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        // Fetch all departments from the database
        List<Department> departments = departmentRepository.findAll();

        // Convert to DTOs to avoid circular reference issues
        return departments.stream()
                .map(dept -> new DepartmentDTO(
                        dept.getDeptId(),
                        dept.getDeptName()))
                .collect(Collectors.toList());
    }

}