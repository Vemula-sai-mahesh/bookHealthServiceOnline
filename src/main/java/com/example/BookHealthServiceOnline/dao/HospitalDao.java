package com.example.BookHealthServiceOnline.dao;

import com.example.BookHealthServiceOnline.domain.Hospital;

import java.util.List;

public interface HospitalDao {
    Hospital save(Hospital hospital);
    Hospital findById(Long id);
    List<Hospital> findAll();
    void deleteById(Long id);

    Hospital findByTenantId(String tenantId);
}
