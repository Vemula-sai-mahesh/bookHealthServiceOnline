package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.UserDao;
import com.example.BookHealthServiceOnline.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UserDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String sqlInsert = String.format("INSERT INTO %s.users (username, password, user_category_id, gender, email, phone_number, created_by, created_date, last_modified_by, last_modified_date) " +
                "VALUES (:username, :password, :userCategoryId, :gender, :email, :phoneNumber, :createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate)", tenantSchema);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Convert the Gender enum to a string
        Map<String, Object> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        params.put("userCategoryId", user.getUserCategoryId());
        params.put("gender", user.getGender().name()); // Convert enum to string
        params.put("email", user.getEmail());
        params.put("phoneNumber", user.getPhoneNumber());
        params.put("createdBy", user.getCreatedBy() != null ? user.getCreatedBy() : "admin");
        params.put("createdDate", user.getCreatedDate() != null ? user.getCreatedDate() : Timestamp.from(Instant.now()));
        params.put("lastModifiedBy", user.getLastModifiedBy() != null ? user.getLastModifiedBy() : "admin");
        params.put("lastModifiedDate", user.getLastModifiedDate() != null ? user.getLastModifiedDate() : Timestamp.from(Instant.now()));

        if (user.getId() == null) {
            try {
                namedParameterJdbcTemplate.update(sqlInsert, new MapSqlParameterSource(params), keyHolder, new String[]{"id"});
            } catch (EmptyResultDataAccessException e) {
                e.printStackTrace();
                return null;
            }

            if (keyHolder.getKey() != null) {
                user.setId(keyHolder.getKey().longValue());
            }
            return user;
        } else {
            return null;
        }
    }


    @Override
    @Transactional
    public User update(User updatedUser) {
        String tenantSchema = AppTenantContext.getCurrentTenant();

        // Build the SQL update query dynamically based on non-null fields
        StringBuilder sqlUpdate = new StringBuilder(String.format("UPDATE %s.users SET ", tenantSchema));
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (updatedUser.getUsername() != null) {
            sqlUpdate.append("username = :username, ");
            params.addValue("username", updatedUser.getUsername());
        }

        if (updatedUser.getEmail() != null) {
            sqlUpdate.append("email = :email, ");
            params.addValue("email", updatedUser.getEmail());
        }
        if(updatedUser.getGender()!=null){
            sqlUpdate.append("gender = :gender, ");
            params.addValue("gender", updatedUser.getGender().name());
        }
        if (updatedUser.getPhoneNumber() != null) {
            sqlUpdate.append("phone_number = :phoneNumber, ");
            params.addValue("phoneNumber", updatedUser.getPhoneNumber());
        }

        // Add the audit fields
        sqlUpdate.append("last_modified_by = :lastModifiedBy, last_modified_date = :lastModifiedDate ");
        params.addValue("lastModifiedBy", "admin");
        params.addValue("lastModifiedDate", Timestamp.from(Instant.now()));

        // Add the WHERE clause
        sqlUpdate.append("WHERE id = :id");
        params.addValue("id", updatedUser.getId());

        // Convert StringBuilder to String
        String finalSqlUpdate = sqlUpdate.toString();

        try {
            namedParameterJdbcTemplate.update(finalSqlUpdate, params);
            return updatedUser;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public User findById(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String sql = String.format("SELECT * FROM %s.users WHERE id = :id", tenantSchema);
        return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", id), new UserRowMapper());
    }

    @Override
    public List<User> findAll() {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String sql = String.format("SELECT * FROM %s.users", tenantSchema);
        return namedParameterJdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String sql = String.format("DELETE FROM %s.users WHERE id = :id", tenantSchema);
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
    }

    @Override
    @Transactional
    public Optional<User> findByUsername(String username) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String sql = String.format("SELECT * FROM %s.users WHERE username = :username", tenantSchema);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", username);

        try {
            User user = namedParameterJdbcTemplate.queryForObject(sql, params, new UserRowMapper());
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static class UserRowMapper implements org.springframework.jdbc.core.RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setUserCategoryId(rs.getLong("user_category_id"));
            user.setGender(User.Gender.valueOf(rs.getString("gender")));
            user.setEmail(rs.getString("email"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setCreatedBy(rs.getString("created_by"));
            user.setCreatedDate(Timestamp.from(rs.getTimestamp("created_date").toInstant()));
            user.setLastModifiedBy(rs.getString("last_modified_by"));
            user.setLastModifiedDate(Timestamp.from(rs.getTimestamp("last_modified_date").toInstant()));
            return user;
        }
    }
}
