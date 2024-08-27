package com.example.BookHealthServiceOnline.service.impl;



import com.example.BookHealthServiceOnline.dao.AdminUserDao;
import com.example.BookHealthServiceOnline.domain.AdminUser;
import com.example.BookHealthServiceOnline.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.BookHealthServiceOnline.Security.AuthoritiesConstants.ADMIN;


@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserDao adminUserDao;

    @Autowired
    public AdminUserServiceImpl(AdminUserDao adminUserDao) {
        this.adminUserDao = adminUserDao;
    }

    @Override
    public AdminUser save(AdminUser adminUser) {
        return adminUserDao.save(adminUser);
    }

    @Override
    public AdminUser update(AdminUser adminUser) {
        return adminUserDao.update(adminUser);
    }

    @Override
    public Optional<AdminUser> findById(Long id) {
        return adminUserDao.findById(id);
    }

    @Override
    public List<AdminUser> findAll() {
        return adminUserDao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        adminUserDao.deleteById(id);
    }

    @Override
    public Optional<AdminUser> findByUsername(String username) {
        return adminUserDao.findByUsername(username);
    }
}
