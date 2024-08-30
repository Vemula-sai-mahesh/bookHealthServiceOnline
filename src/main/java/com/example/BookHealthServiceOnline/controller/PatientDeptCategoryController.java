package com.example.BookHealthServiceOnline.controller;

import com.example.BookHealthServiceOnline.domain.PatientDeptCategory;
import com.example.BookHealthServiceOnline.service.Dto.PatientDeptCategoryDTO;
import com.example.BookHealthServiceOnline.service.PatientDeptCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient-dept-categories")
public class PatientDeptCategoryController {

    @Autowired
    private PatientDeptCategoryService patientDeptCategoryService;

    @PostMapping
    public ResponseEntity<String> createPatientDeptCategory(
            @RequestHeader("X-PrivateTenant") String tenant,
            @Valid @RequestBody PatientDeptCategoryDTO patientDeptCategoryDTO) {

        PatientDeptCategory patientDeptCategory = new PatientDeptCategory();
        patientDeptCategory.setPatientId(patientDeptCategoryDTO.getPatientId());
        patientDeptCategory.setDepartmentId(patientDeptCategoryDTO.getDepartmentId());

        patientDeptCategoryService.save(patientDeptCategory);

        return new ResponseEntity<>("PatientDeptCategory created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDeptCategory> getPatientDeptCategoryById(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id) {
        PatientDeptCategory patientDeptCategory = patientDeptCategoryService.findById(id);
        return (patientDeptCategory != null) ? ResponseEntity.ok(patientDeptCategory) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<PatientDeptCategory>> getAllPatientDeptCategories(
            @RequestHeader("X-PrivateTenant") String tenant) {
        List<PatientDeptCategory> patientDeptCategories = patientDeptCategoryService.findAll();
        return ResponseEntity.ok(patientDeptCategories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePatientDeptCategory(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id,
            @Valid @RequestBody PatientDeptCategoryDTO patientDeptCategoryDTO) {

        PatientDeptCategory patientDeptCategory = patientDeptCategoryService.findById(id);

        if (patientDeptCategory == null) {
            return new ResponseEntity<>("PatientDeptCategory not found", HttpStatus.NOT_FOUND);
        }

        patientDeptCategory.setPatientId(patientDeptCategoryDTO.getPatientId());
        patientDeptCategory.setDepartmentId(patientDeptCategoryDTO.getDepartmentId());

        patientDeptCategoryService.update(patientDeptCategory);

        return new ResponseEntity<>("PatientDeptCategory updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatientDeptCategory(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id) {
        PatientDeptCategory patientDeptCategory = patientDeptCategoryService.findById(id);

        if (patientDeptCategory == null) {
            return new ResponseEntity<>("PatientDeptCategory not found", HttpStatus.NOT_FOUND);
        }

        patientDeptCategoryService.delete(id);
        return new ResponseEntity<>("PatientDeptCategory deleted successfully", HttpStatus.NO_CONTENT);
    }
}
