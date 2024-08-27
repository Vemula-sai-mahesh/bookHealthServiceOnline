package com.example.BookHealthServiceOnline.dao;

import com.example.BookHealthServiceOnline.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User save(User user);
    User update(User user);
    User findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
    Optional<User> findByUsername(String username);
}
