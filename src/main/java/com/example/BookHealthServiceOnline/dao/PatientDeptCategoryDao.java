package com.example.BookHealthServiceOnline.dao;

import com.example.BookHealthServiceOnline.domain.PatientDeptCategory;

import java.util.List;

public interface PatientDeptCategoryDao {
    PatientDeptCategory save(PatientDeptCategory patientDeptCategory);
    PatientDeptCategory update(PatientDeptCategory patientDeptCategory);
    PatientDeptCategory findById(Long id);
    List<PatientDeptCategory> findAll();
    void delete(Long id);
}
