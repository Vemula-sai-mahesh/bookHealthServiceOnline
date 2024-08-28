package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.HospitalServiceDao;
import com.example.BookHealthServiceOnline.domain.HospitalService;
import com.example.BookHealthServiceOnline.service.HospitalServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalServiceServiceImpl implements HospitalServiceService {

    @Autowired
    private HospitalServiceDao hospitalServiceDao;

    @Override
    public HospitalService save(HospitalService hospitalService) {
        // Perform any validation if needed before saving
        return hospitalServiceDao.save(hospitalService);
    }

    @Override
    public HospitalService findById(Long id) {
        return hospitalServiceDao.findById(id);
    }

    @Override
    public List<HospitalService> findAll() {
        return hospitalServiceDao.findAll();
    }

    @Override
    public HospitalService update(HospitalService hospitalService) {
        if (hospitalService.getId() != null) {
            return hospitalServiceDao.update(hospitalService);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        hospitalServiceDao.delete(id);
    }
}
