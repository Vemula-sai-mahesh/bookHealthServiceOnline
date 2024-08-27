package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.PatientDao;
import com.example.BookHealthServiceOnline.dao.UserDao;
import com.example.BookHealthServiceOnline.domain.Patient;
import com.example.BookHealthServiceOnline.domain.User;
import com.example.BookHealthServiceOnline.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.BookHealthServiceOnline.Security.AuthoritiesConstants.PATIENT;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private UserDao userDao;


    @Override
    public Patient save(Patient patient) {
        if(patient.getUserId()!=null && patient.getId() == null) {
                return patientDao.save(patient);
        }

       return null;
    }

    @Override
    public Patient findById(Long id) {
        return patientDao.findById(id);
    }

    @Override
    public List<Patient> findAll() {
        return patientDao.findAll();
    }

    @Override
    public Patient update(Patient patient) {
        if (patient.getId() != null && patient.getUserId()!=null) {
            User user = userDao.findById(patient.getUserId());
            if(patient.getEmail()!=null){
                user.setEmail(patient.getEmail());
            }
           if(patient.getEmergencyContact()!=null){
               user.setPhoneNumber(patient.getEmergencyContact());
           }
            userDao.update(user);
            return patientDao.update(patient);
        }
        return null;
    }

    @Override
    public void delete(Long patientId) {
        patientDao.delete(patientId);
    }
}
