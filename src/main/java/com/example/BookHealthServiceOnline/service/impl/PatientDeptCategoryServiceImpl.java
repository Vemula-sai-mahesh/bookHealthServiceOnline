package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.PatientDeptCategoryDao;
import com.example.BookHealthServiceOnline.domain.PatientDeptCategory;
import com.example.BookHealthServiceOnline.service.PatientDeptCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientDeptCategoryServiceImpl implements PatientDeptCategoryService {

    @Autowired
    private PatientDeptCategoryDao patientDeptCategoryDao;

    @Override
    public PatientDeptCategory save(PatientDeptCategory patientDeptCategory) {
        return patientDeptCategoryDao.save(patientDeptCategory);
    }

    @Override
    public PatientDeptCategory findById(Long id) {
        return patientDeptCategoryDao.findById(id);
    }

    @Override
    public List<PatientDeptCategory> findAll() {
        return patientDeptCategoryDao.findAll();
    }

    @Override
    public PatientDeptCategory update(PatientDeptCategory patientDeptCategory) {
        if (patientDeptCategory.getId() != null) {
            return patientDeptCategoryDao.update(patientDeptCategory);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        patientDeptCategoryDao.delete(id);
    }
}
