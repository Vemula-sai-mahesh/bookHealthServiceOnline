package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.PatientDeptCategoryDao;
import com.example.BookHealthServiceOnline.domain.PatientDeptCategory;
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
public class PatientDeptCategoryDaoImpl implements PatientDeptCategoryDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PatientDeptCategoryDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_PATIENT_DEPT_CATEGORY_TEMPLATE =
            "INSERT INTO %s.patient_dept_category (patient_id, department_id) " +
                    "VALUES (:patientId, :departmentId)";

    private static final String SELECT_PATIENT_DEPT_CATEGORY_BY_ID_TEMPLATE =
            "SELECT * FROM %s.patient_dept_category WHERE id = :id";

    private static final String SELECT_ALL_PATIENT_DEPT_CATEGORIES_TEMPLATE =
            "SELECT * FROM %s.patient_dept_category";

    private static final String DELETE_PATIENT_DEPT_CATEGORY_BY_ID_TEMPLATE =
            "DELETE FROM %s.patient_dept_category WHERE id = :id";

    @Override
    @Transactional
    public PatientDeptCategory save(PatientDeptCategory patientDeptCategory) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String insertSql = String.format(INSERT_PATIENT_DEPT_CATEGORY_TEMPLATE, tenantSchema);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("patientId", patientDeptCategory.getPatientId());
        params.addValue("departmentId", patientDeptCategory.getDepartmentId());

        int rowsAffected = jdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});
        if (rowsAffected > 0) {
            Number key = keyHolder.getKey();
            if (key != null) {
                patientDeptCategory.setId(key.longValue());
                return patientDeptCategory;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public PatientDeptCategory update(PatientDeptCategory updatedPatientDeptCategory) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String updateSql = String.format("UPDATE %s.patient_dept_category SET patient_id = :patientId, department_id = :departmentId WHERE id = :id", tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("patientId", updatedPatientDeptCategory.getPatientId());
        params.addValue("departmentId", updatedPatientDeptCategory.getDepartmentId());
        params.addValue("id", updatedPatientDeptCategory.getId());

        jdbcTemplate.update(updateSql, params);
        return updatedPatientDeptCategory;
    }

    @Override
    public PatientDeptCategory findById(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectSql = String.format(SELECT_PATIENT_DEPT_CATEGORY_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Optional<PatientDeptCategory> patientDeptCategory = jdbcTemplate.query(selectSql, params, new PatientDeptCategoryRowMapper()).stream().findFirst();
        return patientDeptCategory.orElse(null);
    }

    @Override
    public List<PatientDeptCategory> findAll() {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectAllSql = String.format(SELECT_ALL_PATIENT_DEPT_CATEGORIES_TEMPLATE, tenantSchema);
        return jdbcTemplate.query(selectAllSql, new PatientDeptCategoryRowMapper());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String deleteSql = String.format(DELETE_PATIENT_DEPT_CATEGORY_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(deleteSql, params);
    }

    private static class PatientDeptCategoryRowMapper implements RowMapper<PatientDeptCategory> {
        @Override
        public PatientDeptCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
            PatientDeptCategory patientDeptCategory = new PatientDeptCategory();
            patientDeptCategory.setId(rs.getLong("id"));
            patientDeptCategory.setPatientId(rs.getLong("patient_id"));
            patientDeptCategory.setDepartmentId(rs.getLong("department_id"));
            return patientDeptCategory;
        }
    }
}
