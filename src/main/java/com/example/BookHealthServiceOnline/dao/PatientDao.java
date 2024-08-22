package com.example.BookHealthServiceOnline.dao;

import com.example.BookHealthServiceOnline.domain.Patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientDao {
     Patient savePatient(Patient patient);
     Patient findById(Long patientId);
     List<Patient> findAll();
     void deletePatient(Long patientId);

}
