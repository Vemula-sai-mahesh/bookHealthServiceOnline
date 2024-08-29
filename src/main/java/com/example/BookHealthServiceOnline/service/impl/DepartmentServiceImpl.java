package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.DepartmentDao;
import com.example.BookHealthServiceOnline.domain.Department;
import com.example.BookHealthServiceOnline.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public Department save(Department department) {
        return departmentDao.save(department);
    }

    @Override
    public Department findById(Long id) {
        return departmentDao.findById(id);
    }

    @Override
    public List<Department> findAll() {
        return departmentDao.findAll();
    }

    @Override
    public Department update(Department department) {
        if (department.getId() != null) {
            return departmentDao.update(department);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        departmentDao.delete(id);
    }
}
