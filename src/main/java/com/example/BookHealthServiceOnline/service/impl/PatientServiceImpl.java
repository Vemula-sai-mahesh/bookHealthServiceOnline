package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.PatientDao;
import com.example.BookHealthServiceOnline.domain.Patient;
import com.example.BookHealthServiceOnline.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientDao patientDao;

    @Override
    public void savePatient(Patient patient) {
        patientDao.savePatient(patient);
    }

    @Override
    public Patient findById(Long patientId) {
        return patientDao.findById(patientId);
    }

    @Override
    public List<Patient> findAll() {
        return patientDao.findAll();
    }

    @Override
    public void updatePatient(Patient patient) {
        patientDao.savePatient(patient);
    }

    @Override
    public void deletePatient(Long patientId) {
        patientDao.deletePatient(patientId);
    }
}
