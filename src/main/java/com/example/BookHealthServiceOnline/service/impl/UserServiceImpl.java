package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.DoctorDao;
import com.example.BookHealthServiceOnline.dao.PatientDao;
import com.example.BookHealthServiceOnline.dao.UserDao;
import com.example.BookHealthServiceOnline.dao.UserCategoryDao;
import com.example.BookHealthServiceOnline.dao.impl.PatientDaoImpl;
import com.example.BookHealthServiceOnline.domain.Doctor;
import com.example.BookHealthServiceOnline.domain.Patient;
import com.example.BookHealthServiceOnline.domain.User;
import com.example.BookHealthServiceOnline.domain.UserCategory;
import com.example.BookHealthServiceOnline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserCategoryDao userCategoryDao;
    private final PatientDao patientDao;
    private final DoctorDao doctorDao;

    private static final String PATIENT = "PATIENT";
    private static final String DOCTOR = "DOCTOR";

    @Autowired
    public UserServiceImpl(UserDao userDao, UserCategoryDao userCategoryDao, PatientDao patientDao, DoctorDao doctorDao) {
        this.userDao = userDao;
        this.userCategoryDao = userCategoryDao;
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
    }

    @Override
    public User save(User user) {
        UserCategory userCategory = userCategoryDao.findById(user.getUserCategoryId()).orElse(null);
        if (userCategory!=null) {
            if(user.getId()==null){
                user =userDao.save(user);
                if(userCategory.getCategoryName().equals(PATIENT)) {
                    Patient patient = new Patient();
                    patient.setContactNumber(user.getPhoneNumber());
                    patient.setEmail(user.getEmail());
                    patient.setUserId(user.getId());
                    patient.setGender(Patient.Gender.valueOf(user.getGender().name()));
                    patient.setEmergencyContact(user.getPhoneNumber());
                    patientDao.save(patient);
                }else if (userCategory.getCategoryName().equals(DOCTOR)) {
                    Doctor doctor = new Doctor();
                    doctor.setFirstName("DefaultFirstName"); // or extract from user details if available
                    doctor.setLastName("DefaultLastName");   // or extract from user details if available
                    doctor.setSpecialty("DefaultSpecialty"); // or set to a valid value
                    doctor.setQualification("DefaultQualification"); // or set to a valid value
                    doctor.setExperienceYears(0);
                    doctor.setContactNumber(user.getPhoneNumber());
                    doctor.setEmail(user.getEmail());
                    doctor.setUserId(user.getId());
                    doctorDao.save(doctor);
                }

                return user;
            }
        }
        return null;
    }

    @Override
    public User update(User user) {
        Optional<UserCategory> userCategory = userCategoryDao.findById(user.getUserCategoryId());
        if (userCategory.isPresent()) {
            return userDao.update(user);
        }
        return null;
    }


    @Override
    public User findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }
}
