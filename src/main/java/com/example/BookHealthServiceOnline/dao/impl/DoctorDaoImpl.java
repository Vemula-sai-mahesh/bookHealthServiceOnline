package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.dao.DoctorDao;
import com.example.BookHealthServiceOnline.domain.Doctor;
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
public class DoctorDaoImpl implements DoctorDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DoctorDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_DOCTOR =
            "INSERT INTO doctors (first_name, last_name, specialty, qualification, experience_years, contact_number, email) " +
                    "VALUES (:firstName, :lastName, :specialty, :qualification, :experienceYears, :contactNumber, :email)";

    private static final String UPDATE_DOCTOR =
            "UPDATE doctors SET first_name = :firstName, last_name = :lastName, specialty = :specialty, qualification = :qualification, " +
                    "experience_years = :experienceYears, contact_number = :contactNumber, email = :email WHERE id = :id";

    private static final String SELECT_DOCTOR_BY_ID =
            "SELECT * FROM doctors WHERE id = :id";

    private static final String SELECT_ALL_DOCTORS =
            "SELECT * FROM doctors";

    private static final String DELETE_DOCTOR_BY_ID =
            "DELETE FROM doctors WHERE id = :id";

    @Transactional
    @Override
    public Doctor saveDoctor(Doctor doctor) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("firstName", doctor.getFirstName());
        params.addValue("lastName", doctor.getLastName());
        params.addValue("specialty", doctor.getSpecialty());
        params.addValue("qualification", doctor.getQualification());
        params.addValue("experienceYears", doctor.getExperienceYears());
        params.addValue("contactNumber", doctor.getContactNumber());
        params.addValue("email", doctor.getEmail());

        if (doctor.getId() == null) {
            // Insert new record and retrieve generated ID
            int rowsAffected = jdbcTemplate.update(INSERT_DOCTOR, params, keyHolder, new String[]{"id"});
            if (rowsAffected > 0) {
                Number key = keyHolder.getKey();
                if (key != null) {
                    doctor.setId(key.longValue());
                    return doctor;
                }
            }
        } else {
            // Update existing record
            params.addValue("id", doctor.getId());
            jdbcTemplate.update(UPDATE_DOCTOR, params);
            return doctor;
        }
        return null;
    }

    @Override
    public Doctor findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Optional<Doctor> doctor = jdbcTemplate.query(SELECT_DOCTOR_BY_ID, params, new DoctorRowMapper()).stream().findFirst();
        return doctor.orElse(null);
    }

    @Override
    public List<Doctor> findAll() {
        return jdbcTemplate.query(SELECT_ALL_DOCTORS, new DoctorRowMapper());
    }

    @Transactional
    @Override
    public void deleteDoctor(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(DELETE_DOCTOR_BY_ID, params);
    }

    private static class DoctorRowMapper implements RowMapper<Doctor> {
        @Override
        public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Doctor doctor = new Doctor();
            doctor.setId(rs.getLong("id"));
            doctor.setFirstName(rs.getString("first_name"));
            doctor.setLastName(rs.getString("last_name"));
            doctor.setSpecialty(rs.getString("specialty"));
            doctor.setQualification(rs.getString("qualification"));
            doctor.setExperienceYears(rs.getInt("experience_years"));
            doctor.setContactNumber(rs.getString("contact_number"));
            doctor.setEmail(rs.getString("email"));
            return doctor;
        }
    }
}
