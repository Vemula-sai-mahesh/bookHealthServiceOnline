package com.example.BookHealthServiceOnline.service;

import com.example.BookHealthServiceOnline.domain.Doctor;

import java.util.List;

public interface DoctorService {

    Doctor save(Doctor doctor);
    Doctor update(Doctor doctor);
    Doctor findById(Long id);
    List<Doctor> findAll();
    void delete(Long id);
}
