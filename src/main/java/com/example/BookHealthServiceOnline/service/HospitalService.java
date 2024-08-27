package com.example.BookHealthServiceOnline.service;

import com.example.BookHealthServiceOnline.domain.Hospital;

import java.util.List;

public interface HospitalService {
    Hospital save(Hospital hospital);
    Hospital update(Hospital hospital);
    Hospital findById(Long id);
    Hospital findByTenantId(String  tenantId);
    List<Hospital> findAll();
    void deleteById(Long id);
}
