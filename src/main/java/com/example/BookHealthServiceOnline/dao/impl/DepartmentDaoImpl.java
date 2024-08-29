package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.DepartmentDao;
import com.example.BookHealthServiceOnline.domain.Department;
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
import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentDaoImpl implements DepartmentDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DepartmentDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_DEPARTMENT_TEMPLATE =
            "INSERT INTO %s.department (department_name, description, service_id) " +
                    "VALUES (:departmentName, :description, :serviceId)";

    private static final String SELECT_DEPARTMENT_BY_ID_TEMPLATE =
            "SELECT * FROM %s.department WHERE id = :id";

    private static final String SELECT_ALL_DEPARTMENTS_TEMPLATE =
            "SELECT * FROM %s.department";

    private static final String DELETE_DEPARTMENT_BY_ID_TEMPLATE =
            "DELETE FROM %s.department WHERE id = :id";

    @Override
    @Transactional
    public Department save(Department department) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String insertSql = String.format(INSERT_DEPARTMENT_TEMPLATE, tenantSchema);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("departmentName", department.getDepartmentName());
        params.addValue("description", department.getDescription());
        params.addValue("serviceId", department.getServiceId());

        int rowsAffected = jdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});
        if (rowsAffected > 0) {
            Number key = keyHolder.getKey();
            if (key != null) {
                department.setId(key.longValue());
                return department;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Department update(Department updatedDepartment) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String updateSql = String.format("UPDATE %s.department SET department_name = :departmentName, description = :description, service_id = :serviceId WHERE id = :id", tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("departmentName", updatedDepartment.getDepartmentName());
        params.addValue("description", updatedDepartment.getDescription());
        params.addValue("serviceId", updatedDepartment.getServiceId());
        params.addValue("id", updatedDepartment.getId());

        jdbcTemplate.update(updateSql, params);
        return updatedDepartment;
    }

    @Override
    public Department findById(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectSql = String.format(SELECT_DEPARTMENT_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Optional<Department> department = jdbcTemplate.query(selectSql, params, new DepartmentRowMapper()).stream().findFirst();
        return department.orElse(null);
    }

    @Override
    public List<Department> findAll() {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectAllSql = String.format(SELECT_ALL_DEPARTMENTS_TEMPLATE, tenantSchema);
        return jdbcTemplate.query(selectAllSql, new DepartmentRowMapper());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String deleteSql = String.format(DELETE_DEPARTMENT_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(deleteSql, params);
    }

    private static class DepartmentRowMapper implements RowMapper<Department> {
        @Override
        public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
            Department department = new Department();
            department.setId(rs.getLong("id"));
            department.setDepartmentName(rs.getString("department_name"));
            department.setDescription(rs.getString("description"));
            // Assuming you have a method to fetch HospitalService by ID, or modify this part accordingly
            Long serviceId = rs.getLong("service_id");
            if (serviceId != null && serviceId > 0) {

                department.setServiceId(serviceId);
            }
            return department;
        }
    }
}
