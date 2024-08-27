package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.HospitalDao;
import com.example.BookHealthServiceOnline.domain.Hospital;
//import com.example.BookHealthServiceOnline.Security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class HospitalDaoImpl implements HospitalDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HospitalDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Hospital save(Hospital hospital) {
        String sqlInsert = "INSERT INTO hospital (hospital_name, hospital_address, contact_number, tenant_id, url, domain_url, " +
                "created_by, created_date, last_modified_by, last_modified_date) " +
                "VALUES (:hospitalName, :hospitalAddress, :contactNumber, :tenantId, :url, :domainUrl, " +
                ":createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate)";

        String sqlUpdate = "UPDATE hospital SET hospital_name = :hospitalName, hospital_address = :hospitalAddress, " +
                "contact_number = :contactNumber, tenant_id = :tenantId, url = :url, domain_url = :domainUrl, " +
                "last_modified_by = :lastModifiedBy, last_modified_date = :lastModifiedDate " +
                "WHERE id = :id";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(hospital);

//        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();

        if (hospital.getId() == null) {
            // Set audit fields for the new record
//            currentUserLogin.ifPresent(hospital::setCreatedBy);
            hospital.setCreatedBy("admin");
            hospital.setCreatedDate(Timestamp.from(Instant.now()));
//            currentUserLogin.ifPresent(hospital::setLastModifiedBy);
            hospital.setLastModifiedBy("admin");
            hospital.setLastModifiedDate(Timestamp.from(Instant.now()));
            params = new BeanPropertySqlParameterSource(hospital);

            int rowsAffected = namedParameterJdbcTemplate.update(sqlInsert, params, keyHolder, new String[]{"id"});
            if (rowsAffected > 0) {
                hospital.setId(keyHolder.getKey().longValue());
                return hospital;
            }
        }
       return null;
    }
    @Override
    public Hospital update(Hospital updatedHospital) {
        String tenantSchema = AppTenantContext.getCurrentTenant();

        // Start building the SQL update query
        StringBuilder sqlUpdate = new StringBuilder(String.format("UPDATE %s.hospital SET ", tenantSchema));

        // Use a Map to store parameters for the SQL update
        MapSqlParameterSource params = new MapSqlParameterSource();

        // Check each field and append to the SQL query if the field is not null
        if (updatedHospital.getHospitalName() != null) {
            sqlUpdate.append("hospital_name = :hospitalName, ");
            params.addValue("hospitalName", updatedHospital.getHospitalName());
        }

        if (updatedHospital.getHospitalAddress() != null) {
            sqlUpdate.append("hospital_address = :hospitalAddress, ");
            params.addValue("hospitalAddress", updatedHospital.getHospitalAddress());
        }

        if (updatedHospital.getContactNumber() != null) {
            sqlUpdate.append("contact_number = :contactNumber, ");
            params.addValue("contactNumber", updatedHospital.getContactNumber());
        }
        if (updatedHospital.getUrl() != null) {
            sqlUpdate.append("url = :url, ");
            params.addValue("url", updatedHospital.getUrl());
        }

        if (updatedHospital.getDomainUrl() != null) {
            sqlUpdate.append("domain_url = :domainUrl, ");
            params.addValue("domainUrl", updatedHospital.getDomainUrl());
        }

        // Set the audit fields
        sqlUpdate.append("last_modified_by = :lastModifiedBy, ");
        sqlUpdate.append("last_modified_date = :lastModifiedDate, ");
        params.addValue("lastModifiedBy", "admin"); // Replace with SecurityUtils.getCurrentUserLogin() if needed
        params.addValue("lastModifiedDate", Timestamp.from(Instant.now()));

        // Remove the trailing comma and space, and add the WHERE clause
        sqlUpdate.setLength(sqlUpdate.length() - 2); // Remove last comma and space
        sqlUpdate.append(" WHERE id = :id");
        params.addValue("id", updatedHospital.getId());

        // Execute the update
        namedParameterJdbcTemplate.update(sqlUpdate.toString(), params);

        return updatedHospital;
    }

    @Override
    public Hospital findById(Long id) {
        String sql = "SELECT * FROM hospital WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new HospitalRowMapper(), id);
    }

    @Override
    public List<Hospital> findAll() {
        String sql = "SELECT * FROM hospital";
        return jdbcTemplate.query(sql, new HospitalRowMapper());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM hospital WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Hospital findByTenantId(String tenantId) {
        String sql = "SELECT * FROM hospital WHERE tenant_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{tenantId}, new HospitalRowMapper());
    }

    private static class HospitalRowMapper implements RowMapper<Hospital> {
        @Override
        public Hospital mapRow(ResultSet rs, int rowNum) throws SQLException {
            Hospital hospital = new Hospital();
            hospital.setId(rs.getLong("id"));
            hospital.setHospitalName(rs.getString("hospital_name"));
            hospital.setHospitalAddress(rs.getString("hospital_address"));
            hospital.setContactNumber(rs.getString("contact_number"));
            hospital.setTenantId(rs.getString("tenant_id"));
            hospital.setUrl(rs.getString("url"));
            hospital.setDomainUrl(rs.getString("domain_url"));
            hospital.setCreatedBy(rs.getString("created_by"));
            hospital.setCreatedDate(Timestamp.from(rs.getTimestamp("created_date").toInstant()));
            hospital.setLastModifiedBy(rs.getString("last_modified_by"));
            hospital.setLastModifiedDate(Timestamp.from(rs.getTimestamp("last_modified_date").toInstant()));
            return hospital;
        }
    }
}
