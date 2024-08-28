package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.DepartmentDao;
import com.example.BookHealthServiceOnline.domain.Department;
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
public class DepartmentDaoImpl implements DepartmentDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DepartmentDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_DEPARTMENT_TEMPLATE =
            "INSERT INTO %s.department (department_name, description, created_by, created_date, last_modified_by, last_modified_date) " +
                    "VALUES (:departmentName, :description, :createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate)";

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
        populateCommonParams(params, department);

        // Set audit fields for the new record
//        department.setCreatedBy("admin");
//        department.setCreatedDate(Timestamp.from(Instant.now()));
//        department.setLastModifiedBy("admin");
//        department.setLastModifiedDate(Timestamp.from(Instant.now()));
//
//        params.addValue("createdBy", department.getCreatedBy());
//        params.addValue("createdDate", department.getCreatedDate());
//        params.addValue("lastModifiedBy", department.getLastModifiedBy());
//        params.addValue("lastModifiedDate", department.getLastModifiedDate());

        // Insert new record and retrieve generated ID
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

        // Start building the update SQL query
        StringBuilder updateSql = new StringBuilder(String.format("UPDATE %s.department SET ", tenantSchema));
        MapSqlParameterSource params = new MapSqlParameterSource();

        // Add fields that are not null to the update query
        if (updatedDepartment.getDepartmentName() != null) {
            updateSql.append("department_name = :departmentName, ");
            params.addValue("departmentName", updatedDepartment.getDepartmentName());
        }
        if (updatedDepartment.getDescription() != null) {
            updateSql.append("description = :description, ");
            params.addValue("description", updatedDepartment.getDescription());
        }

        // Set the audit fields
//        updatedDepartment.setLastModifiedBy("admin");
//        updatedDepartment.setLastModifiedDate(Timestamp.from(Instant.now()));
//        updateSql.append("last_modified_by = :lastModifiedBy, last_modified_date = :lastModifiedDate ");
//
//        params.addValue("lastModifiedBy", updatedDepartment.getLastModifiedBy());
//        params.addValue("lastModifiedDate", updatedDepartment.getLastModifiedDate());

        // Add the WHERE clause
        updateSql.append("WHERE id = :id");
        params.addValue("id", updatedDepartment.getId());

        // Execute the update
        jdbcTemplate.update(updateSql.toString(), params);

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

    private void populateCommonParams(MapSqlParameterSource params, Department department) {
        params.addValue("departmentName", department.getDepartmentName());
        params.addValue("description", department.getDescription());
    }

    private static class DepartmentRowMapper implements RowMapper<Department> {
        @Override
        public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
            Department department = new Department();
            department.setId(rs.getLong("id"));
            department.setDepartmentName(rs.getString("department_name"));
            department.setDescription(rs.getString("description"));

            // Map auditing fields
//            department.setCreatedBy(rs.getString("created_by"));
//            department.setCreatedDate(Timestamp.from(rs.getTimestamp("created_date").toInstant()));
//            department.setLastModifiedBy(rs.getString("last_modified_by"));
//            department.setLastModifiedDate(Timestamp.from(rs.getTimestamp("last_modified_date").toInstant()));

            return department;
        }
    }
}
