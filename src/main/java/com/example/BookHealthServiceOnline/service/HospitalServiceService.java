package com.example.BookHealthServiceOnline.service;

import com.example.BookHealthServiceOnline.domain.HospitalService;
import java.util.List;

public interface HospitalServiceService {

    HospitalService save(HospitalService hospitalService);
    HospitalService update(HospitalService hospitalService);
    HospitalService findById(Long id);
    List<HospitalService> findAll();
    void delete(Long id);
}
