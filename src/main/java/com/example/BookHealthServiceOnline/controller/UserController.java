package com.example.BookHealthServiceOnline.controller;


import com.example.BookHealthServiceOnline.domain.User;
import com.example.BookHealthServiceOnline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/newuser")
    public ResponseEntity<User> createUser(  @RequestHeader("X-PrivateTenant") String tenant,@RequestBody User user) {
        User createdUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(  @RequestHeader("X-PrivateTenant") String tenant,@RequestBody User user) {
        User updatedUser = userService.update(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(  @RequestHeader("X-PrivateTenant") String tenant,@PathVariable Long id) {
        User user = userService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(  @RequestHeader("X-PrivateTenant") String tenant) {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(  @RequestHeader("X-PrivateTenant") String tenant,@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
