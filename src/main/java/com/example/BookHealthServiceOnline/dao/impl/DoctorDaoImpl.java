package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.DoctorDao;
import com.example.BookHealthServiceOnline.domain.Doctor;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DoctorDaoImpl implements DoctorDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DoctorDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_DOCTOR_TEMPLATE =
            "INSERT INTO %s.doctors (first_name, last_name, specialty, qualification, experience_years, contact_number, email, user_id, created_by, created_date, last_modified_by, last_modified_date) " +
                    "VALUES (:firstName, :lastName, :specialty, :qualification, :experienceYears, :contactNumber, :email, :userId, :createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate)";

    private static final String SELECT_DOCTOR_BY_ID_TEMPLATE =
            "SELECT * FROM %s.doctors WHERE id = :id";

    private static final String SELECT_ALL_DOCTORS_TEMPLATE =
            "SELECT * FROM %s.doctors";

    private static final String DELETE_DOCTOR_BY_ID_TEMPLATE =
            "DELETE FROM %s.doctors WHERE id = :id";

//    @Override
//    @Transactional
//    public Doctor save(Doctor doctor) {
//        String tenantSchema = AppTenantContext.getCurrentTenant();
//        String insertSql = String.format(INSERT_DOCTOR_TEMPLATE, tenantSchema);
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        populateCommonParams(params, doctor);
//
//        // Set audit fields for the new record
//        doctor.setCreatedBy("admin");
//        doctor.setCreatedDate(Timestamp.from(Instant.now()));
//        doctor.setLastModifiedBy("admin");
//        doctor.setLastModifiedDate(Timestamp.from(Instant.now()));
//
//        params.addValue("createdBy", doctor.getCreatedBy());
//        params.addValue("createdDate", doctor.getCreatedDate());
//        params.addValue("lastModifiedBy", doctor.getLastModifiedBy());
//        params.addValue("lastModifiedDate", doctor.getLastModifiedDate());
//
//        // Insert new record and retrieve generated ID
//        int rowsAffected = jdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});
//        if (rowsAffected > 0) {
//            Number key = keyHolder.getKey();
//            if (key != null) {
//                doctor.setId(key.longValue());
//                return doctor;
//            }
//        }
//        return null;
//    }
@Override
@Transactional
public Doctor save(Doctor doctor) {
    String tenantSchema = AppTenantContext.getCurrentTenant();
    String insertSql = String.format("INSERT INTO %s.doctors (first_name, last_name, specialty, qualification, experience_years, contact_number, email, user_id, created_by, created_date, last_modified_by, last_modified_date) " +
            "VALUES (:firstName, :lastName, :specialty, :qualification, :experienceYears, :contactNumber, :email, :userId, :createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate)", tenantSchema);

    KeyHolder keyHolder = new GeneratedKeyHolder();
    MapSqlParameterSource params = new MapSqlParameterSource();

    // Populate parameters from the Doctor object
    params.addValue("firstName", doctor.getFirstName());
    params.addValue("lastName", doctor.getLastName());
    params.addValue("specialty", doctor.getSpecialty());
    params.addValue("qualification", doctor.getQualification());
    params.addValue("experienceYears", doctor.getExperienceYears());
    params.addValue("contactNumber", doctor.getContactNumber());
    params.addValue("email", doctor.getEmail());
    params.addValue("userId", doctor.getUserId());

    // Set audit fields
    doctor.setCreatedBy("admin");
    doctor.setCreatedDate(Timestamp.from(Instant.now()));
    doctor.setLastModifiedBy("admin");
    doctor.setLastModifiedDate(Timestamp.from(Instant.now()));

    params.addValue("createdBy", doctor.getCreatedBy());
    params.addValue("createdDate", doctor.getCreatedDate());
    params.addValue("lastModifiedBy", doctor.getLastModifiedBy());
    params.addValue("lastModifiedDate", doctor.getLastModifiedDate());

    // Execute the insert operation
    int rowsAffected = jdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});
    if (rowsAffected > 0) {
        Number key = keyHolder.getKey();
        if (key != null) {
            doctor.setId(key.longValue());
            return doctor;
        }
    }
    return null;
}



    @Override
    @Transactional
    public Doctor update(Doctor updatedDoctor) {
        String tenantSchema = AppTenantContext.getCurrentTenant();

        // Start building the update SQL query
        StringBuilder updateSql = new StringBuilder(String.format("UPDATE %s.doctors SET ", tenantSchema));
        MapSqlParameterSource params = new MapSqlParameterSource();

        // Add fields that are not null to the update query
        if (updatedDoctor.getFirstName() != null) {
            updateSql.append("first_name = :firstName, ");
            params.addValue("firstName", updatedDoctor.getFirstName());
        }
        if (updatedDoctor.getLastName() != null) {
            updateSql.append("last_name = :lastName, ");
            params.addValue("lastName", updatedDoctor.getLastName());
        }
        if (updatedDoctor.getSpecialty() != null) {
            updateSql.append("specialty = :specialty, ");
            params.addValue("specialty", updatedDoctor.getSpecialty());
        }
        if (updatedDoctor.getQualification() != null) {
            updateSql.append("qualification = :qualification, ");
            params.addValue("qualification", updatedDoctor.getQualification());
        }
        if (updatedDoctor.getExperienceYears() != null) {
            updateSql.append("experience_years = :experienceYears, ");
            params.addValue("experienceYears", updatedDoctor.getExperienceYears());
        }
        if (updatedDoctor.getContactNumber() != null) {
            updateSql.append("contact_number = :contactNumber, ");
            params.addValue("contactNumber", updatedDoctor.getContactNumber());
        }
        if (updatedDoctor.getEmail() != null) {
            updateSql.append("email = :email, ");
            params.addValue("email", updatedDoctor.getEmail());
        }

        // Set the audit fields
        updatedDoctor.setLastModifiedBy("admin");
        updatedDoctor.setLastModifiedDate(Timestamp.from(Instant.now()));
        updateSql.append("last_modified_by = :lastModifiedBy, last_modified_date = :lastModifiedDate ");

        params.addValue("lastModifiedBy", updatedDoctor.getLastModifiedBy());
        params.addValue("lastModifiedDate", updatedDoctor.getLastModifiedDate());

        // Add the WHERE clause
        updateSql.append("WHERE id = :id");
        params.addValue("id", updatedDoctor.getId());

        // Execute the update
        jdbcTemplate.update(updateSql.toString(), params);

        return updatedDoctor;
    }

    @Override
    public Doctor findById(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectSql = String.format(SELECT_DOCTOR_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Optional<Doctor> doctor = jdbcTemplate.query(selectSql, params, new DoctorRowMapper()).stream().findFirst();
        return doctor.orElse(null);
    }

    @Override
    public List<Doctor> findAll() {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectAllSql = String.format(SELECT_ALL_DOCTORS_TEMPLATE, tenantSchema);
        return jdbcTemplate.query(selectAllSql, new DoctorRowMapper());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String deleteSql = String.format(DELETE_DOCTOR_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(deleteSql, params);
    }

    private void populateCommonParams(MapSqlParameterSource params, Doctor doctor) {
        params.addValue("firstName", doctor.getFirstName());
        params.addValue("lastName", doctor.getLastName());
        params.addValue("specialty", doctor.getSpecialty());
        params.addValue("qualification", doctor.getQualification());
        params.addValue("experienceYears", doctor.getExperienceYears());
        params.addValue("contactNumber", doctor.getContactNumber());
        params.addValue("email", doctor.getEmail());
        params.addValue("userId", doctor.getUserId());
    }

    private static class DoctorRowMapper implements RowMapper<Doctor> {
        @Override
        public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Doctor doctor = new Doctor();
            doctor.setId(rs.getLong("id"));
            doctor.setFirstName(rs.getString("first_name"));
            doctor.setLastName(rs.getString("last_name"));
            doctor.setUserId(rs.getLong("user_id"));
            doctor.setSpecialty(rs.getString("specialty"));
            doctor.setQualification(rs.getString("qualification"));
            doctor.setExperienceYears(rs.getInt("experience_years"));
            doctor.setContactNumber(rs.getString("contact_number"));
            doctor.setEmail(rs.getString("email"));

            // Map auditing fields
            doctor.setCreatedBy(rs.getString("created_by"));
            doctor.setCreatedDate(Timestamp.from(rs.getTimestamp("created_date").toInstant()));
            doctor.setLastModifiedBy(rs.getString("last_modified_by"));
            doctor.setLastModifiedDate(Timestamp.from(rs.getTimestamp("last_modified_date").toInstant()));

            return doctor;
        }
    }
}
