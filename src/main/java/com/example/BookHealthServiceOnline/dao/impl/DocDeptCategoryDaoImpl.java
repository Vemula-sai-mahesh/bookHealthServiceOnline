package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.DocDeptCategoryDao;
import com.example.BookHealthServiceOnline.domain.DocDeptCategory;
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
public class DocDeptCategoryDaoImpl implements DocDeptCategoryDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DocDeptCategoryDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_DOC_DEPT_CATEGORY_TEMPLATE =
            "INSERT INTO %s.doc_dept_category (doctor_id, department_id, available_from, available_to, available_time_interval, charges_per_time_interval) " +
                    "VALUES (:doctorId, :departmentId, :availableFrom, :availableTo, :availableTimeInterval, :chargesPerTimeInterval)";

    private static final String SELECT_DOC_DEPT_CATEGORY_BY_ID_TEMPLATE =
            "SELECT * FROM %s.doc_dept_category WHERE id = :id";

    private static final String SELECT_ALL_DOC_DEPT_CATEGORIES_TEMPLATE =
            "SELECT * FROM %s.doc_dept_category";

    private static final String DELETE_DOC_DEPT_CATEGORY_BY_ID_TEMPLATE =
            "DELETE FROM %s.doc_dept_category WHERE id = :id";

    @Override
    @Transactional
    public DocDeptCategory save(DocDeptCategory docDeptCategory) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String insertSql = String.format(INSERT_DOC_DEPT_CATEGORY_TEMPLATE, tenantSchema);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("doctorId", docDeptCategory.getDoctorId());
        params.addValue("departmentId", docDeptCategory.getDepartmentId());
        params.addValue("availableFrom", docDeptCategory.getAvailableFrom());
        params.addValue("availableTo", docDeptCategory.getAvailableTo());
        params.addValue("availableTimeInterval", docDeptCategory.getAvailableTimeInterval());
        params.addValue("chargesPerTimeInterval", docDeptCategory.getChargesPerTimeInterval());

        int rowsAffected = jdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});
        if (rowsAffected > 0) {
            Number key = keyHolder.getKey();
            if (key != null) {
                docDeptCategory.setId(key.longValue());
                return docDeptCategory;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public DocDeptCategory update(DocDeptCategory updatedDocDeptCategory) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String updateSql = String.format("UPDATE %s.doc_dept_category SET doctor_id = :doctorId, department_id = :departmentId, available_from = :availableFrom, available_to = :availableTo, available_time_interval = :availableTimeInterval, charges_per_time_interval = :chargesPerTimeInterval WHERE id = :id", tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("doctorId", updatedDocDeptCategory.getDoctorId());
        params.addValue("departmentId", updatedDocDeptCategory.getDepartmentId());
        params.addValue("availableFrom", updatedDocDeptCategory.getAvailableFrom());
        params.addValue("availableTo", updatedDocDeptCategory.getAvailableTo());
        params.addValue("availableTimeInterval", updatedDocDeptCategory.getAvailableTimeInterval());
        params.addValue("chargesPerTimeInterval", updatedDocDeptCategory.getChargesPerTimeInterval());
        params.addValue("id", updatedDocDeptCategory.getId());

        jdbcTemplate.update(updateSql, params);
        return updatedDocDeptCategory;
    }

    @Override
    public DocDeptCategory findById(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectSql = String.format(SELECT_DOC_DEPT_CATEGORY_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Optional<DocDeptCategory> docDeptCategory = jdbcTemplate.query(selectSql, params, new DocDeptCategoryRowMapper()).stream().findFirst();
        return docDeptCategory.orElse(null);
    }

    @Override
    public List<DocDeptCategory> findAll() {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectAllSql = String.format(SELECT_ALL_DOC_DEPT_CATEGORIES_TEMPLATE, tenantSchema);
        return jdbcTemplate.query(selectAllSql, new DocDeptCategoryRowMapper());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String deleteSql = String.format(DELETE_DOC_DEPT_CATEGORY_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(deleteSql, params);
    }

    private static class DocDeptCategoryRowMapper implements RowMapper<DocDeptCategory> {
        @Override
        public DocDeptCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
            DocDeptCategory docDeptCategory = new DocDeptCategory();
            docDeptCategory.setId(rs.getLong("id"));
            docDeptCategory.setDoctorId(rs.getLong("doctor_id"));
            docDeptCategory.setDepartmentId(rs.getLong("department_id"));
            docDeptCategory.setAvailableFrom(rs.getTime("available_from").toLocalTime());
            docDeptCategory.setAvailableTo(rs.getTime("available_to").toLocalTime());
            docDeptCategory.setAvailableTimeInterval(rs.getInt("available_time_interval"));
            docDeptCategory.setChargesPerTimeInterval(rs.getBigDecimal("charges_per_time_interval"));
            return docDeptCategory;
        }
    }
}
