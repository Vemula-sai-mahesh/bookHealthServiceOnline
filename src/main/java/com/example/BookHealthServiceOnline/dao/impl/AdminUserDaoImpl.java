package com.example.BookHealthServiceOnline.dao.impl;


import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.AdminUserDao;
import com.example.BookHealthServiceOnline.domain.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class AdminUserDaoImpl implements AdminUserDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AdminUserDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AdminUser save(AdminUser adminUser) {
        String sqlInsert = "INSERT INTO admin_user (username, password, email, phone_number, user_role, " +
                "created_by, created_date, last_modified_by, last_modified_date) " +
                "VALUES (:username, :password, :email, :phoneNumber, :userRole, " +
                ":createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(adminUser);

        if (adminUser.getId() == null) {
            adminUser.setCreatedBy("admin");
            adminUser.setCreatedDate(Timestamp.from(Instant.now()));
            adminUser.setLastModifiedBy("admin");
            adminUser.setLastModifiedDate(Timestamp.from(Instant.now()));

            params = new BeanPropertySqlParameterSource(adminUser);
            int rowsAffected = namedParameterJdbcTemplate.update(sqlInsert, params, keyHolder, new String[]{"id"});
            if (rowsAffected > 0) {
                adminUser.setId(keyHolder.getKey().longValue());
                return adminUser;
            }
        }
        return null;
    }

    @Override
    public AdminUser update(AdminUser updatedAdminUser) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        StringBuilder sqlUpdate = new StringBuilder(String.format("UPDATE %s.admin_user SET ", tenantSchema));

        MapSqlParameterSource params = new MapSqlParameterSource();

        if (updatedAdminUser.getUsername() != null) {
            sqlUpdate.append("username = :username, ");
            params.addValue("username", updatedAdminUser.getUsername());
        }

        if (updatedAdminUser.getPassword() != null) {
            sqlUpdate.append("password = :password, ");
            params.addValue("password", updatedAdminUser.getPassword());
        }

        if (updatedAdminUser.getEmail() != null) {
            sqlUpdate.append("email = :email, ");
            params.addValue("email", updatedAdminUser.getEmail());
        }

        if (updatedAdminUser.getPhoneNumber() != null) {
            sqlUpdate.append("phone_number = :phoneNumber, ");
            params.addValue("phoneNumber", updatedAdminUser.getPhoneNumber());
        }

        if (updatedAdminUser.getUserRole() != null) {
            sqlUpdate.append("user_role = :userRole, ");
            params.addValue("userRole", updatedAdminUser.getUserRole());
        }

        sqlUpdate.append("last_modified_by = :lastModifiedBy, ");
        sqlUpdate.append("last_modified_date = :lastModifiedDate, ");
        params.addValue("lastModifiedBy", "admin"); // Replace with SecurityUtils.getCurrentUserLogin() if needed
        params.addValue("lastModifiedDate", Timestamp.from(Instant.now()));

        sqlUpdate.setLength(sqlUpdate.length() - 2); // Remove last comma and space
        sqlUpdate.append(" WHERE id = :id");
        params.addValue("id", updatedAdminUser.getId());

        namedParameterJdbcTemplate.update(sqlUpdate.toString(), params);

        return updatedAdminUser;
    }

    @Override
    public Optional<AdminUser> findById(Long id) {
        String sql = "SELECT * FROM admin_user WHERE id = ?";
        return jdbcTemplate.query(sql, new AdminUserRowMapper(), id).stream().findFirst();
    }

    @Override
    public List<AdminUser> findAll() {
        String sql = "SELECT * FROM admin_user";
        return jdbcTemplate.query(sql, new AdminUserRowMapper());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM admin_user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<AdminUser> findByUsername(String username) {
        String sql = "SELECT * FROM admin_user WHERE username = ?";
        return jdbcTemplate.query(sql, new Object[]{username}, new AdminUserRowMapper()).stream().findFirst();
    }

    private static class AdminUserRowMapper implements RowMapper<AdminUser> {
        @Override
        public AdminUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            AdminUser adminUser = new AdminUser();
            adminUser.setId(rs.getLong("id"));
            adminUser.setUsername(rs.getString("username"));
            adminUser.setPassword(rs.getString("password"));
            adminUser.setEmail(rs.getString("email"));
            adminUser.setPhoneNumber(rs.getString("phone_number"));
            adminUser.setUserRole(rs.getString("user_role"));
            adminUser.setCreatedBy(rs.getString("created_by"));
            adminUser.setCreatedDate(Timestamp.from(rs.getTimestamp("created_date").toInstant()));
            adminUser.setLastModifiedBy(rs.getString("last_modified_by"));
            adminUser.setLastModifiedDate(Timestamp.from(rs.getTimestamp("last_modified_date").toInstant()));
            return adminUser;
        }
    }
}
