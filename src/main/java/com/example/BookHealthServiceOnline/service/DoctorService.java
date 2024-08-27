package com.example.BookHealthServiceOnline.service;

import com.example.BookHealthServiceOnline.domain.Doctor;

import java.util.List;

public interface DoctorService {

    void saveDoctor(Doctor doctor);
    Doctor findById(Long id);
    List<Doctor> findAll();
    void updateDoctor(Doctor doctor);
    void deleteDoctor(Long id);
}
