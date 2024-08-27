package com.example.BookHealthServiceOnline.dao;

import com.example.BookHealthServiceOnline.domain.Doctor;

import java.util.List;

public interface DoctorDao {
    Doctor saveDoctor(Doctor doctor);
    Doctor findById(Long id);
    List<Doctor> findAll();
    void deleteDoctor(Long id);
}


