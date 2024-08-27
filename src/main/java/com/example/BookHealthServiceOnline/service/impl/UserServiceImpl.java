package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.PatientDao;
import com.example.BookHealthServiceOnline.dao.UserDao;
import com.example.BookHealthServiceOnline.dao.UserCategoryDao;
import com.example.BookHealthServiceOnline.dao.impl.PatientDaoImpl;
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
    private static final String PATIENT = "PATIENT";
    @Autowired
    public UserServiceImpl(UserDao userDao, UserCategoryDao userCategoryDao, PatientDao patientDao) {
        this.userDao = userDao;
        this.userCategoryDao = userCategoryDao;
        this.patientDao = patientDao;
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
                    patient.setEmergencyContact(user.getPhoneNumber());
                    patientDao.save(patient);
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
