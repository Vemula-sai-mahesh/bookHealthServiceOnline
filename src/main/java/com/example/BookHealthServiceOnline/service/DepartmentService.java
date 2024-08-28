package com.example.BookHealthServiceOnline.service;

import com.example.BookHealthServiceOnline.domain.Department;

import java.util.List;

public interface DepartmentService {

    Department save(Department department);
    Department update(Department department);
    Department findById(Long id);
    List<Department> findAll();
    void delete(Long id);
}
