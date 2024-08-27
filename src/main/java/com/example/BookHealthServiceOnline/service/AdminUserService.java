package com.example.BookHealthServiceOnline.service;


import com.example.BookHealthServiceOnline.domain.AdminUser;

import java.util.List;
import java.util.Optional;

public interface AdminUserService {

    AdminUser save(AdminUser adminUser);

    AdminUser update(AdminUser adminUser);

    Optional<AdminUser> findById(Long id);

    List<AdminUser> findAll();

    void deleteById(Long id);

    Optional<AdminUser> findByUsername(String username);
}
