package com.example.BookHealthServiceOnline.service;
import com.example.BookHealthServiceOnline.domain.User;

import java.util.List;

public interface UserService {
    User save(User user);
    User update(User user);
    User findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
}
