package com.example.BookHealthServiceOnline.dao;

import com.example.BookHealthServiceOnline.domain.Department;

import java.util.List;

public interface DepartmentDao {
    Department save(Department department);
    Department update(Department department);
    Department findById(Long departmentId);
    List<Department> findAll();
    void delete(Long departmentId);
}
