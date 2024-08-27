package com.example.BookHealthServiceOnline.dao;

import com.example.BookHealthServiceOnline.domain.Patient;

import java.util.List;


public interface PatientDao {
     Patient savePatient(Patient patient);
     Patient findById(Long id);
     List<Patient> findAll();
     void deletePatient(Long id);

}
