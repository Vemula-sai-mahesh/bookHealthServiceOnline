package com.example.BookHealthServiceOnline.controller;

import com.example.BookHealthServiceOnline.domain.UserCategory;
import com.example.BookHealthServiceOnline.service.UserCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-categories")
public class UserCategoryController {

    private final UserCategoryService userCategoryService;

    public UserCategoryController(UserCategoryService userCategoryService) {
        this.userCategoryService = userCategoryService;
    }

    @PostMapping
    public ResponseEntity<UserCategory> createUserCategory(  @RequestHeader("X-PrivateTenant") String tenant,@RequestBody UserCategory userCategory) {
        UserCategory savedUserCategory = userCategoryService.save(userCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserCategory);
    }
    @PutMapping
    public ResponseEntity<UserCategory> updateUserCategory(  @RequestHeader("X-PrivateTenant") String tenant,@RequestBody UserCategory userCategory) {
        UserCategory updatedUserCategory = userCategoryService.update(userCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedUserCategory);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserCategory> getUserCategory(  @RequestHeader("X-PrivateTenant") String tenant,@PathVariable Long id) {
        Optional<UserCategory> userCategory = userCategoryService.findById(id);
        return userCategory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserCategory>> getAllUserCategories(  @RequestHeader("X-PrivateTenant") String tenant) {
        List<UserCategory> userCategories = userCategoryService.findAll();
        return ResponseEntity.ok(userCategories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserCategory(  @RequestHeader("X-PrivateTenant") String tenant,@PathVariable Long id) {
        userCategoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
