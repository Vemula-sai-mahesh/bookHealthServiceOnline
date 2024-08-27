package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.UserCategoryDao;
import com.example.BookHealthServiceOnline.domain.UserCategory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserCategoryDaoImpl implements UserCategoryDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserCategoryDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public UserCategory save(UserCategory userCategory) {
        if (userCategory.getId() == null) {
            return create(userCategory);
        } else {
            return update(userCategory);
        }
    }

    private UserCategory create(UserCategory userCategory) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String sqlInsert = String.format("INSERT INTO %s.user_category (category_name, status) VALUES (:categoryName, :status)", tenantSchema);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(userCategory);

        int rowsAffected = namedParameterJdbcTemplate.update(sqlInsert, params, keyHolder, new String[]{"id"});
        if (rowsAffected > 0) {
            userCategory.setId(keyHolder.getKey().longValue());
        }

        return userCategory;
    }

    @Override
    @Transactional
    public UserCategory update(UserCategory updatedUserCategory) {
        String tenantSchema = AppTenantContext.getCurrentTenant();

        // Start building the SQL update query
        StringBuilder sqlUpdate = new StringBuilder(String.format("UPDATE %s.user_category SET ", tenantSchema));

        // Use a Map to store parameters for the SQL update
        MapSqlParameterSource params = new MapSqlParameterSource();

        // Check each field and append to the SQL query if the field is not null
        if (updatedUserCategory.getCategoryName() != null) {
            sqlUpdate.append("category_name = :categoryName, ");
            params.addValue("categoryName", updatedUserCategory.getCategoryName());
        }

        // Remove the trailing comma and space, and add the WHERE clause
        sqlUpdate.setLength(sqlUpdate.length() - 2); // Remove last comma and space
        sqlUpdate.append(" WHERE id = :id");
        params.addValue("id", updatedUserCategory.getId());

        // Execute the update
        namedParameterJdbcTemplate.update(sqlUpdate.toString(), params);

        return updatedUserCategory;
    }


    @Override
    public Optional<UserCategory> findById(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String sqlSelect = String.format("SELECT * FROM %s.user_category WHERE id = :id", tenantSchema);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return namedParameterJdbcTemplate.query(sqlSelect, params, new UserCategoryRowMapper()).stream().findFirst();
    }

    @Override
    public List<UserCategory> findAll() {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String sqlSelectAll = String.format("SELECT * FROM %s.user_category", tenantSchema);

        return namedParameterJdbcTemplate.query(sqlSelectAll, new UserCategoryRowMapper());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String sqlDelete = String.format("DELETE FROM %s.user_category WHERE id = :id", tenantSchema);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        namedParameterJdbcTemplate.update(sqlDelete, params);
    }

    private static class UserCategoryRowMapper implements RowMapper<UserCategory> {
        @Override
        public UserCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserCategory userCategory = new UserCategory();
            userCategory.setId(rs.getLong("id"));
            userCategory.setCategoryName(rs.getString("category_name"));
            userCategory.setStatus(rs.getBoolean("status"));
            return userCategory;
        }
    }
}
