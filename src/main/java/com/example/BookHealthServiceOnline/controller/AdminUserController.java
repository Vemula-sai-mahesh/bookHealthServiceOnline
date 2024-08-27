package com.example.BookHealthServiceOnline.controller;


import com.example.BookHealthServiceOnline.domain.AdminUser;
import com.example.BookHealthServiceOnline.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin-users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Autowired
    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping("/newuser")
    public ResponseEntity<AdminUser> createAdminUser(@RequestBody AdminUser adminUser) {
        AdminUser savedUser = adminUserService.save(adminUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUser> updateAdminUser(@PathVariable Long id, @RequestBody AdminUser adminUser) {
        if (!adminUserService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        adminUser.setId(id);
        AdminUser updatedUser = adminUserService.update(adminUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUser> getAdminUserById(@PathVariable Long id) {
        Optional<AdminUser> adminUser = adminUserService.findById(id);
        return adminUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AdminUser>> getAllAdminUsers() {
        List<AdminUser> adminUsers = adminUserService.findAll();
        return ResponseEntity.ok(adminUsers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminUser(@PathVariable Long id) {
        if (!adminUserService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        adminUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AdminUser> getAdminUserByUsername(@PathVariable String username) {
        Optional<AdminUser> adminUser = adminUserService.findByUsername(username);
        return adminUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
