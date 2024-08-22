package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.config.DatabaseInitializer;
import com.example.BookHealthServiceOnline.domain.Hospital;
import org.springframework.stereotype.Service;

@Service
public class TenantService {


    private final DatabaseInitializer databaseInitializer;

    public TenantService(DatabaseInitializer databaseInitializer) {
        this.databaseInitializer = databaseInitializer;
    }

    public String  createTenantSchemaAndTables(Hospital hospital) {
        String tenantId = hospital.getTenantId();
        String result=databaseInitializer.createSchemaAndTables(tenantId);
        return result;
    }
}
