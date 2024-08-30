package com.example.BookHealthServiceOnline.service;

import com.example.BookHealthServiceOnline.domain.PatientDeptCategory;

import java.util.List;

public interface PatientDeptCategoryService {

    PatientDeptCategory save(PatientDeptCategory patientDeptCategory);
    PatientDeptCategory update(PatientDeptCategory patientDeptCategory);
    PatientDeptCategory findById(Long id);
    List<PatientDeptCategory> findAll();
    void delete(Long id);
}
