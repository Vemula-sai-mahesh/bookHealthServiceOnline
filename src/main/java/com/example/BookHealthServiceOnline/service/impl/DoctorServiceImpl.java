package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.DoctorDao;
import com.example.BookHealthServiceOnline.domain.Doctor;
import com.example.BookHealthServiceOnline.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorDao doctorDao;

    @Override
    public void saveDoctor(Doctor doctor) {
        doctorDao.saveDoctor(doctor);
    }

    @Override
    public Doctor findById(Long id) {
        return doctorDao.findById(id);
    }

    @Override
    public List<Doctor> findAll() {
        return doctorDao.findAll();
    }

    @Override
    public void updateDoctor(Doctor doctor) {
        doctorDao.saveDoctor(doctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorDao.deleteDoctor(id);
    }
}
