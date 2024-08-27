package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.PatientDao;
import com.example.BookHealthServiceOnline.domain.Patient;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientDaoImpl implements PatientDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PatientDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_PATIENT_TEMPLATE =
            "INSERT INTO %s.patient (user_id, first_name, last_name, date_of_birth, gender, contact_number, email, address, emergency_contact, " +
                    "created_by, created_date, last_modified_by, last_modified_date) " +
                    "VALUES (:userId, :firstName, :lastName, :dateOfBirth, :gender, :contactNumber, :email, :address, :emergencyContact, " +
                    ":createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate)";


    private static final String SELECT_PATIENT_BY_ID_TEMPLATE =
            "SELECT * FROM %s.patient WHERE id = :id";

    private static final String SELECT_ALL_PATIENTS_TEMPLATE =
            "SELECT * FROM %s.patient";

    private static final String DELETE_PATIENT_BY_ID_TEMPLATE =
            "DELETE FROM %s.patient WHERE id = :id";

    @Override
    @Transactional
    public Patient save(Patient patient) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String insertSql = String.format(INSERT_PATIENT_TEMPLATE, tenantSchema);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        populateCommonParams(params, patient);

        // Set audit fields for the new record
        patient.setCreatedBy("admin");
        patient.setCreatedDate(Timestamp.from(Instant.now()));
        patient.setLastModifiedBy("admin");
        patient.setLastModifiedDate(Timestamp.from(Instant.now()));

        params.addValue("createdBy", patient.getCreatedBy());
        params.addValue("createdDate", patient.getCreatedDate());
        params.addValue("lastModifiedBy", patient.getLastModifiedBy());
        params.addValue("lastModifiedDate", patient.getLastModifiedDate());

        // Insert new record and retrieve generated ID
        int rowsAffected = jdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});
        if (rowsAffected > 0) {
            Number key = keyHolder.getKey();
            if (key != null) {
                patient.setId(key.longValue());
                return patient;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Patient update(Patient updatedPatient) {
        String tenantSchema = AppTenantContext.getCurrentTenant();

        // Start building the update SQL query
        StringBuilder updateSql = new StringBuilder(String.format("UPDATE %s.patient SET ", tenantSchema));
        MapSqlParameterSource params = new MapSqlParameterSource();

        // Add fields that are not null to the update query
        if (updatedPatient.getFirstName() != null) {
            updateSql.append("first_name = :firstName, ");
            params.addValue("firstName", updatedPatient.getFirstName());
        }
        if (updatedPatient.getLastName() != null) {
            updateSql.append("last_name = :lastName, ");
            params.addValue("lastName", updatedPatient.getLastName());
        }
        if (updatedPatient.getDateOfBirth() != null) {
            updateSql.append("date_of_birth = :dateOfBirth, ");
            params.addValue("dateOfBirth", updatedPatient.getDateOfBirth());
        }
        if (updatedPatient.getGender() != null) {
            updateSql.append("gender = :gender, ");
            params.addValue("gender", updatedPatient.getGender().name());
        }
        if (updatedPatient.getContactNumber() != null) {
            updateSql.append("contact_number = :contactNumber, ");
            params.addValue("contactNumber", updatedPatient.getContactNumber());
        }
        if (updatedPatient.getEmail() != null) {
            updateSql.append("email = :email, ");
            params.addValue("email", updatedPatient.getEmail());
        }
        if (updatedPatient.getAddress() != null) {
            updateSql.append("address = :address, ");
            params.addValue("address", updatedPatient.getAddress());
        }
        if (updatedPatient.getEmergencyContact() != null) {
            updateSql.append("emergency_contact = :emergencyContact, ");
            params.addValue("emergencyContact", updatedPatient.getEmergencyContact());
        }

        // Set the audit fields
        updatedPatient.setLastModifiedBy("admin");
        updatedPatient.setLastModifiedDate(Timestamp.from(Instant.now()));
        updateSql.append("last_modified_by = :lastModifiedBy, last_modified_date = :lastModifiedDate ");

        params.addValue("lastModifiedBy", updatedPatient.getLastModifiedBy());
        params.addValue("lastModifiedDate", updatedPatient.getLastModifiedDate());

        // Add the WHERE clause
        updateSql.append("WHERE id = :id");
        params.addValue("id", updatedPatient.getId());

        // Execute the update
        jdbcTemplate.update(updateSql.toString(), params);

        return updatedPatient;
    }


    @Override
    public Patient findById(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectSql = String.format(SELECT_PATIENT_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Optional<Patient> patient = jdbcTemplate.query(selectSql, params, new PatientRowMapper()).stream().findFirst();
        return patient.orElse(null);
    }

    @Override
    public List<Patient> findAll() {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectAllSql = String.format(SELECT_ALL_PATIENTS_TEMPLATE, tenantSchema);
        return jdbcTemplate.query(selectAllSql, new PatientRowMapper());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String deleteSql = String.format(DELETE_PATIENT_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(deleteSql, params);
    }

    private void populateCommonParams(MapSqlParameterSource params, Patient patient) {
        params.addValue("userId", patient.getUserId());
        params.addValue("firstName", patient.getFirstName());
        params.addValue("lastName", patient.getLastName());
        params.addValue("dateOfBirth", patient.getDateOfBirth());
        params.addValue("gender", patient.getGender());
        params.addValue("contactNumber", patient.getContactNumber());
        params.addValue("email", patient.getEmail());
        params.addValue("address", patient.getAddress());
        params.addValue("emergencyContact", patient.getEmergencyContact());
    }

    private static class PatientRowMapper implements RowMapper<Patient> {
        @Override
        public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
            Patient patient = new Patient();
            patient.setId(rs.getLong("id"));
            patient.setUserId(rs.getLong("user_id"));
            patient.setFirstName(rs.getString("first_name"));
            patient.setLastName(rs.getString("last_name"));
            LocalDate dateOfBirth=null;
            if(rs.getString("date_of_birth") != null) {
                try{
                    dateOfBirth= rs.getDate("date_of_birth").toLocalDate();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            patient.setDateOfBirth(dateOfBirth);
            patient.setGender(Patient.Gender.valueOf(rs.getString("gender")));
            patient.setContactNumber(rs.getString("contact_number"));
            patient.setEmail(rs.getString("email"));
            patient.setAddress(rs.getString("address"));
            patient.setEmergencyContact(rs.getString("emergency_contact"));

            // Map auditing fields
            patient.setCreatedBy(rs.getString("created_by"));
            patient.setCreatedDate(Timestamp.from(rs.getTimestamp("created_date").toInstant()));
            patient.setLastModifiedBy(rs.getString("last_modified_by"));
            patient.setLastModifiedDate(Timestamp.from(rs.getTimestamp("last_modified_date").toInstant()));

            return patient;
        }
    }
}
