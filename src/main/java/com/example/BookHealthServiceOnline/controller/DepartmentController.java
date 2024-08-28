package com.example.BookHealthServiceOnline.controller;

import com.example.BookHealthServiceOnline.domain.Department;
import com.example.BookHealthServiceOnline.service.Dto.DepartmentDTO;
import com.example.BookHealthServiceOnline.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<String> createDepartment(
            @RequestHeader("X-PrivateTenant") String tenant,
            @Valid @RequestBody DepartmentDTO departmentDTO) {

        // Convert DepartmentDTO to Department entity
        Department department = new Department();
        department.setDepartmentName(departmentDTO.getDepartmentName());
        department.setDescription(departmentDTO.getDescription());

        // Save the Department entity to the database
        departmentService.save(department);

        // Return a response indicating success
        return new ResponseEntity<>("Department created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id) {
        Department department = departmentService.findById(id);
        return (department != null) ? ResponseEntity.ok(department) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments(
            @RequestHeader("X-PrivateTenant") String tenant) {
        List<Department> departments = departmentService.findAll();
        return ResponseEntity.ok(departments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDepartment(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id,
            @Valid @RequestBody DepartmentDTO departmentDTO) {

        // Fetch existing department
        Department department = departmentService.findById(id);

        if (department == null) {
            return new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND);
        }

        department.setDepartmentName(departmentDTO.getDepartmentName());
        department.setDescription(departmentDTO.getDescription());

        // Save the updated department
        departmentService.update(department);

        return new ResponseEntity<>("Department updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id) {
        Department department = departmentService.findById(id);

        if (department == null) {
            return new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND);
        }

        departmentService.delete(id);
        return new ResponseEntity<>("Department deleted successfully", HttpStatus.NO_CONTENT);
    }
}
