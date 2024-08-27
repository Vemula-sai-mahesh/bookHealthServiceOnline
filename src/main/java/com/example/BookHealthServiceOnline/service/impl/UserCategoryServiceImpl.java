package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.UserCategoryDao;
import com.example.BookHealthServiceOnline.domain.UserCategory;
import com.example.BookHealthServiceOnline.service.UserCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserCategoryServiceImpl implements UserCategoryService {

    private final UserCategoryDao userCategoryDao;

    public UserCategoryServiceImpl(UserCategoryDao userCategoryDao) {
        this.userCategoryDao = userCategoryDao;
    }

    @Override
    public UserCategory save(UserCategory userCategory) {
        return userCategoryDao.save(userCategory);
    }

    @Override
    public UserCategory update(UserCategory updatedUserCategory){
        return userCategoryDao.update(updatedUserCategory);
    }

    @Override
    public Optional<UserCategory> findById(Long id) {
        return userCategoryDao.findById(id);
    }

    @Override
    public List<UserCategory> findAll() {
        return userCategoryDao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userCategoryDao.deleteById(id);
    }
}
