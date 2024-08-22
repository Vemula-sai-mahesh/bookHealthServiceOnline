package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.dao.HospitalDao;
import com.example.BookHealthServiceOnline.domain.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        String sqlInsert = "INSERT INTO hospital (hospital_name, hospital_address, contact_number, tenant_id, url) " +
                "VALUES (:hospitalName, :hospitalAddress, :contactNumber, :tenantId, :url)";
        String sqlUpdate = "UPDATE hospital SET hospital_name = :hospitalName, hospital_address = :hospitalAddress, " +
                "contact_number = :contactNumber, tenant_id = :tenantId, url = :url WHERE id = :id";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(hospital);

        if (hospital.getId() == null) {
            int rowsAffected = namedParameterJdbcTemplate.update(sqlInsert, params, keyHolder, new String[]{"id"});
            if (rowsAffected > 0) {
                hospital.setId(keyHolder.getKey().longValue());
            }
        } else {
            namedParameterJdbcTemplate.update(sqlUpdate, params);
        }
        return hospital;
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
        // Define the SQL query to find a hospital by tenantId
        String sql = "SELECT * FROM hospitals WHERE tenant_id = ?";

        // Execute the query using JdbcTemplate and map the result to a Hospital object
        return jdbcTemplate.queryForObject(sql, new Object[]{tenantId}, (rs, rowNum) -> {
            Hospital hospital = new Hospital();
            hospital.setId(rs.getLong("id"));
            hospital.setHospitalName(rs.getString("hospital_name"));
            hospital.setHospitalAddress(rs.getString("hospital_address"));
            hospital.setContactNumber(rs.getString("contact_number"));
            hospital.setTenantId(rs.getString("tenant_id"));
            hospital.setUrl(rs.getString("url"));
            return hospital;
        });
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
            return hospital;
        }
    }
}
