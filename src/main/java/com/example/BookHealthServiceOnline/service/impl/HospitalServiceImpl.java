package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.HospitalDao;
import com.example.BookHealthServiceOnline.domain.Hospital;
import com.example.BookHealthServiceOnline.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalServiceImpl implements HospitalService {

    private HospitalDao hospitalDao;

    private TenantService tenantService;

    @Autowired
    public HospitalServiceImpl(HospitalDao hospitalDao,TenantService tenantService) {
        this.hospitalDao = hospitalDao;
        this.tenantService = tenantService;
    }


    @Override
    public Hospital save(Hospital hospital) {

        if(hospital.getId()==null){
            String result=tenantService.createTenantSchemaAndTables(hospital);
            if(result.equals("error")){
                return null;
            }
            hospital=hospitalDao.save(hospital);
            return hospital;
        }
       return null;
    }

    @Override
    public Hospital update(Hospital hospital) {
        if(hospital.getId()!=null){
            hospital=hospitalDao.update(hospital);
            return hospital;
        }
        return null;
    }

    @Override
    public Hospital findById(Long id) {
        return hospitalDao.findById(id);
    }

    @Override
    public Hospital findByTenantId(String tenantId) {
        return hospitalDao.findByTenantId(tenantId);
    }

    @Override
    public List<Hospital> findAll() {
        return hospitalDao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        hospitalDao.deleteById(id);
    }
}
