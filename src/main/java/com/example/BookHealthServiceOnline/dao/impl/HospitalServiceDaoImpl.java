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
            "INSERT INTO %s.hospital_service (service_name, description) " +
                    "VALUES (:serviceName, :description)";

    private static final String SELECT_HOSPITAL_SERVICE_BY_ID_TEMPLATE =
            "SELECT * FROM %s.hospital_service WHERE id = :id";

    private static final String SELECT_ALL_HOSPITAL_SERVICES_TEMPLATE =
            "SELECT * FROM %s.hospital_service";

    private static final String DELETE_HOSPITAL_SERVICE_BY_ID_TEMPLATE =
            "DELETE FROM %s.hospital_service WHERE id = :id";

    @Override
    @Transactional
    public HospitalService save(HospitalService hospitalService) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String insertSql = String.format(INSERT_HOSPITAL_SERVICE_TEMPLATE, tenantSchema);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("serviceName", hospitalService.getServiceName());
        params.addValue("description", hospitalService.getDescription());

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
        String updateSql = String.format("UPDATE %s.hospital_service SET service_name = :serviceName, description = :description WHERE id = :id", tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("serviceName", updatedHospitalService.getServiceName());
        params.addValue("description", updatedHospitalService.getDescription());
        params.addValue("id", updatedHospitalService.getId());

        jdbcTemplate.update(updateSql, params);
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

    private static class HospitalServiceRowMapper implements RowMapper<HospitalService> {
        @Override
        public HospitalService mapRow(ResultSet rs, int rowNum) throws SQLException {
            HospitalService hospitalService = new HospitalService();
            hospitalService.setId(rs.getLong("id"));
            hospitalService.setServiceName(rs.getString("service_name"));
            hospitalService.setDescription(rs.getString("description"));
            return hospitalService;
        }
    }
}
