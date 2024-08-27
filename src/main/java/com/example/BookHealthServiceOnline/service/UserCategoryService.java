package com.example.BookHealthServiceOnline.service;

import com.example.BookHealthServiceOnline.domain.UserCategory;

import java.util.List;
import java.util.Optional;

public interface UserCategoryService {
    UserCategory save(UserCategory userCategory);
    UserCategory update(UserCategory updatedUserCategory);
    Optional<UserCategory> findById(Long id);
    List<UserCategory> findAll();
    void deleteById(Long id);
}
