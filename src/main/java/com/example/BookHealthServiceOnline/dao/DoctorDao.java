package com.example.BookHealthServiceOnline.dao;


import com.example.BookHealthServiceOnline.domain.Doctor;

import java.util.List;

public interface DoctorDao {
    Doctor save(Doctor doctor);
    Doctor update(Doctor doctor);
    Doctor findById(Long doctorId);
    List<Doctor> findAll();
    void delete(Long doctorId);
}
