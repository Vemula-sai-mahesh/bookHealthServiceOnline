package com.example.BookHealthServiceOnline.service;

import com.example.BookHealthServiceOnline.domain.Patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientService {

    Patient save(Patient patient);
    Patient update(Patient patient);
    Patient findById(Long id);
    List<Patient> findAll();
    void delete(Long id);
}
