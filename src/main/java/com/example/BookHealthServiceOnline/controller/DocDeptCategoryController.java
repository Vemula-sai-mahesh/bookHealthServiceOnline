package com.example.BookHealthServiceOnline.controller;

import com.example.BookHealthServiceOnline.domain.DocDeptCategory;
import com.example.BookHealthServiceOnline.service.Dto.DocDeptCategoryDTO;
import com.example.BookHealthServiceOnline.service.DocDeptCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doc-dept-categories")
public class DocDeptCategoryController {

    @Autowired
    private DocDeptCategoryService docDeptCategoryService;

    @PostMapping
    public ResponseEntity<String> createDocDeptCategory(
            @RequestHeader("X-PrivateTenant") String tenant,
            @Valid @RequestBody DocDeptCategoryDTO docDeptCategoryDTO) {

        DocDeptCategory docDeptCategory = new DocDeptCategory();
        docDeptCategory.setDoctorId(docDeptCategoryDTO.getDoctorId());
        docDeptCategory.setDepartmentId(docDeptCategoryDTO.getDepartmentId());
        docDeptCategory.setAvailableFrom(docDeptCategoryDTO.getAvailableFrom());
        docDeptCategory.setAvailableTo(docDeptCategoryDTO.getAvailableTo());
        docDeptCategory.setAvailableTimeInterval(docDeptCategoryDTO.getAvailableTimeInterval());
        docDeptCategory.setChargesPerTimeInterval(docDeptCategoryDTO.getChargesPerTimeInterval());

        docDeptCategoryService.save(docDeptCategory);

        return new ResponseEntity<>("DocDeptCategory created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocDeptCategory> getDocDeptCategoryById(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id) {
        DocDeptCategory docDeptCategory = docDeptCategoryService.findById(id);
        return (docDeptCategory != null) ? ResponseEntity.ok(docDeptCategory) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<DocDeptCategory>> getAllDocDeptCategories(
            @RequestHeader("X-PrivateTenant") String tenant) {
        List<DocDeptCategory> docDeptCategories = docDeptCategoryService.findAll();
        return ResponseEntity.ok(docDeptCategories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDocDeptCategory(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id,
            @Valid @RequestBody DocDeptCategoryDTO docDeptCategoryDTO) {

        DocDeptCategory docDeptCategory = docDeptCategoryService.findById(id);

        if (docDeptCategory == null) {
            return new ResponseEntity<>("DocDeptCategory not found", HttpStatus.NOT_FOUND);
        }

        docDeptCategory.setDoctorId(docDeptCategoryDTO.getDoctorId());
        docDeptCategory.setDepartmentId(docDeptCategoryDTO.getDepartmentId());
        docDeptCategory.setAvailableFrom(docDeptCategoryDTO.getAvailableFrom());
        docDeptCategory.setAvailableTo(docDeptCategoryDTO.getAvailableTo());
        docDeptCategory.setAvailableTimeInterval(docDeptCategoryDTO.getAvailableTimeInterval());
        docDeptCategory.setChargesPerTimeInterval(docDeptCategoryDTO.getChargesPerTimeInterval());

        docDeptCategoryService.update(docDeptCategory);

        return new ResponseEntity<>("DocDeptCategory updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocDeptCategory(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id) {
        DocDeptCategory docDeptCategory = docDeptCategoryService.findById(id);

        if (docDeptCategory == null) {
            return new ResponseEntity<>("DocDeptCategory not found", HttpStatus.NOT_FOUND);
        }

        docDeptCategoryService.delete(id);
        return new ResponseEntity<>("DocDeptCategory deleted successfully", HttpStatus.NO_CONTENT);
    }
}
