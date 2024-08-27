package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.dao.PatientDao;

import com.example.BookHealthServiceOnline.domain.Patient;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientDaoImpl implements PatientDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PatientDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_PATIENT =
            "INSERT INTO patient (first_name, last_name, date_of_birth, gender, contact_number, email, address, emergency_contact) " +
                    "VALUES (:firstName, :lastName, :dateOfBirth, :gender, :contactNumber, :email, :address, :emergencyContact)";

    private static final String UPDATE_PATIENT =
            "UPDATE patient SET first_name = :firstName, last_name = :lastName, date_of_birth = :dateOfBirth, gender = :gender, " +
                    "contact_number = :contactNumber, email = :email, address = :address, emergency_contact = :emergencyContact WHERE patient_id = :patientId";

    private static final String SELECT_PATIENT_BY_ID =
            "SELECT * FROM patient WHERE patient_id = :patientId";

    private static final String SELECT_ALL_PATIENTS =
            "SELECT * FROM patient";

    private static final String DELETE_PATIENT_BY_ID =
            "DELETE FROM patient WHERE patient_id = :patientId";

    @Transactional
    @Override
    public Patient savePatient(Patient patient) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("firstName", patient.getFirstName());
        params.addValue("lastName", patient.getLastName());
        params.addValue("dateOfBirth", patient.getDateOfBirth());
        params.addValue("gender", patient.getGender().name());
        params.addValue("contactNumber", patient.getContactNumber());
        params.addValue("email", patient.getEmail());
        params.addValue("address", patient.getAddress());
        params.addValue("emergencyContact", patient.getEmergencyContact());

        if (patient.getId() == null) {
            // Insert new record and retrieve generated ID
            int rowsAffected = jdbcTemplate.update(INSERT_PATIENT, params, keyHolder, new String[]{"id"});
            if (rowsAffected > 0) {
                Number key = keyHolder.getKey();
                if (key != null) {
                    patient.setId(key.longValue());
                    return patient;
                }
            }
        } else {
            // Update existing record
            params.addValue("patientId", patient.getId());
            jdbcTemplate.update(UPDATE_PATIENT, params);
            return patient;
        }
        return null;
    }

    @Override
    public Patient findById(Long patientId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("patientId", patientId);
        Optional<Patient> patient=jdbcTemplate.query(SELECT_PATIENT_BY_ID, params, new PatientRowMapper()).stream().findFirst();
        return patient.orElse(null);
    }

    @Override
    public List<Patient> findAll() {
        return jdbcTemplate.query(SELECT_ALL_PATIENTS, new PatientRowMapper());
    }

    @Transactional
    @Override
    public void deletePatient(Long patientId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("patientId", patientId);
        jdbcTemplate.update(DELETE_PATIENT_BY_ID, params);
    }

    private static class PatientRowMapper implements RowMapper<Patient> {
        @Override
        public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
            Patient patient = new Patient();
            patient.setId(rs.getLong("patient_id"));
            patient.setFirstName(rs.getString("first_name"));
            patient.setLastName(rs.getString("last_name"));
            patient.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
            patient.setGender(Patient.Gender.valueOf(rs.getString("gender")));
            patient.setContactNumber(rs.getString("contact_number"));
            patient.setEmail(rs.getString("email"));
            patient.setAddress(rs.getString("address"));
            patient.setEmergencyContact(rs.getString("emergency_contact"));
            return patient;
        }
    }
}
