package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.DoctorDao;
import com.example.BookHealthServiceOnline.dao.UserDao;
import com.example.BookHealthServiceOnline.domain.Doctor;
import com.example.BookHealthServiceOnline.domain.User;
import com.example.BookHealthServiceOnline.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Doctor save(Doctor doctor) {
        if (doctor.getUserId() != null && doctor.getId() == null) {
            return doctorDao.save(doctor);
        }
        return null;
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
    public Doctor update(Doctor doctor) {
        if (doctor.getId() != null && doctor.getUserId() != null) {
            User user = userDao.findById(doctor.getUserId());
            if (doctor.getEmail() != null) {
                user.setEmail(doctor.getEmail());
            }
            if (doctor.getContactNumber() != null) {
                user.setPhoneNumber(doctor.getContactNumber());
            }
            userDao.update(user);
            return doctorDao.update(doctor);
        }
        return null;
    }

    @Override
    public void delete(Long doctorId) {
        doctorDao.delete(doctorId);
    }
}
