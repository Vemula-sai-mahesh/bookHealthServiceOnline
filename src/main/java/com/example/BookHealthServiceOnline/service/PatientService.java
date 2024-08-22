package com.example.BookHealthServiceOnline.service;

import com.example.BookHealthServiceOnline.domain.Patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientService {

    void savePatient(Patient patient);
    Patient findById(Long patientId);
    List<Patient> findAll();
    void updatePatient(Patient patient);
    void deletePatient(Long patientId);
}
