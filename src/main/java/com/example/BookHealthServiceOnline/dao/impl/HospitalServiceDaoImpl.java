package com.example.BookHealthServiceOnline.dao.impl;


import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.HospitalServiceDao;
import com.example.BookHealthServiceOnline.domain.HospitalService;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class HospitalServiceDaoImpl implements HospitalServiceDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public HospitalServiceDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_HOSPITAL_SERVICE_TEMPLATE =
            "INSERT INTO %s.hospital_services (service_name, description, department_id, price, created_by, created_date, last_modified_by, last_modified_date) " +
                    "VALUES (:serviceName, :description, :departmentId, :price, :createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate)";

    private static final String SELECT_HOSPITAL_SERVICE_BY_ID_TEMPLATE =
            "SELECT * FROM %s.hospital_services WHERE id = :id";

    private static final String SELECT_ALL_HOSPITAL_SERVICES_TEMPLATE =
            "SELECT * FROM %s.hospital_services";

    private static final String DELETE_HOSPITAL_SERVICE_BY_ID_TEMPLATE =
            "DELETE FROM %s.hospital_services WHERE id = :id";

    @Override
    @Transactional
    public HospitalService save(HospitalService hospitalService) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String insertSql = String.format(INSERT_HOSPITAL_SERVICE_TEMPLATE, tenantSchema);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        populateCommonParams(params, hospitalService);

        // Set audit fields for the new record
//        hospitalService.setCreatedBy("admin");
//        hospitalService.setCreatedDate(Timestamp.from(Instant.now()));
//        hospitalService.setLastModifiedBy("admin");
//        hospitalService.setLastModifiedDate(Timestamp.from(Instant.now()));
//
//        params.addValue("createdBy", hospitalService.getCreatedBy());
//        params.addValue("createdDate", hospitalService.getCreatedDate());
//        params.addValue("lastModifiedBy", hospitalService.getLastModifiedBy());
//        params.addValue("lastModifiedDate", hospitalService.getLastModifiedDate());

        // Insert new record and retrieve generated ID
        int rowsAffected = jdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});
        if (rowsAffected > 0) {
            Number key = keyHolder.getKey();
            if (key != null) {
                hospitalService.setId(key.longValue());
                return hospitalService;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public HospitalService update(HospitalService updatedHospitalService) {
        String tenantSchema = AppTenantContext.getCurrentTenant();

        // Start building the update SQL query
        StringBuilder updateSql = new StringBuilder(String.format("UPDATE %s.hospital_services SET ", tenantSchema));
        MapSqlParameterSource params = new MapSqlParameterSource();

        // Add fields that are not null to the update query
        if (updatedHospitalService.getServiceName() != null) {
            updateSql.append("service_name = :serviceName, ");
            params.addValue("serviceName", updatedHospitalService.getServiceName());
        }
        if (updatedHospitalService.getDescription() != null) {
            updateSql.append("description = :description, ");
            params.addValue("description", updatedHospitalService.getDescription());
        }
        if (updatedHospitalService.getDepartment() != null) {
            updateSql.append("department_id = :departmentId, ");
            params.addValue("departmentId", updatedHospitalService.getDepartment().getId());
        }
        if (updatedHospitalService.getPrice() != null) {
            updateSql.append("price = :price, ");
            params.addValue("price", updatedHospitalService.getPrice());
        }

        // Set the audit fields
//        updatedHospitalService.setLastModifiedBy("admin");
//        updatedHospitalService.setLastModifiedDate(Timestamp.from(Instant.now()));
//        updateSql.append("last_modified_by = :lastModifiedBy, last_modified_date = :lastModifiedDate ");
//
//        params.addValue("lastModifiedBy", updatedHospitalService.getLastModifiedBy());
//        params.addValue("lastModifiedDate", updatedHospitalService.getLastModifiedDate());

        // Add the WHERE clause
        updateSql.append("WHERE id = :id");
        params.addValue("id", updatedHospitalService.getId());

        // Execute the update
        jdbcTemplate.update(updateSql.toString(), params);

        return updatedHospitalService;
    }

    @Override
    public HospitalService findById(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectSql = String.format(SELECT_HOSPITAL_SERVICE_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Optional<HospitalService> hospitalService = jdbcTemplate.query(selectSql, params, new HospitalServiceRowMapper()).stream().findFirst();
        return hospitalService.orElse(null);
    }

    @Override
    public List<HospitalService> findAll() {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectAllSql = String.format(SELECT_ALL_HOSPITAL_SERVICES_TEMPLATE, tenantSchema);
        return jdbcTemplate.query(selectAllSql, new HospitalServiceRowMapper());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String deleteSql = String.format(DELETE_HOSPITAL_SERVICE_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(deleteSql, params);
    }

    private void populateCommonParams(MapSqlParameterSource params, HospitalService hospitalService) {
        params.addValue("serviceName", hospitalService.getServiceName());
        params.addValue("description", hospitalService.getDescription());
        params.addValue("departmentId", hospitalService.getDepartment() != null ? hospitalService.getDepartment().getId() : null);
        params.addValue("price", hospitalService.getPrice());
    }

    private static class HospitalServiceRowMapper implements RowMapper<HospitalService> {
        @Override
        public HospitalService mapRow(ResultSet rs, int rowNum) throws SQLException {
            HospitalService hospitalService = new HospitalService();
            hospitalService.setId(rs.getLong("id"));
            hospitalService.setServiceName(rs.getString("service_name"));
            hospitalService.setDescription(rs.getString("description"));

            // Assuming Department entity is populated separately
            // Example: hospitalService.setDepartment(new Department(rs.getLong("department_id")));

            hospitalService.setPrice(rs.getBigDecimal("price"));

            // Map auditing fields
//            hospitalService.setCreatedBy(rs.getString("created_by"));
//            hospitalService.setCreatedDate(Timestamp.from(rs.getTimestamp("created_date").toInstant()));
//            hospitalService.setLastModifiedBy(rs.getString("last_modified_by"));
//            hospitalService.setLastModifiedDate(Timestamp.from(rs.getTimestamp("last_modified_date").toInstant()));

            return hospitalService;
        }
    }
}

