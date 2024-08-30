package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.AppointmentSlotsDao;
import com.example.BookHealthServiceOnline.domain.AppointmentSlots;
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
public class AppointmentSlotsDaoImpl implements AppointmentSlotsDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AppointmentSlotsDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_APPOINTMENT_SLOTS_TEMPLATE =
            "INSERT INTO %s.appointment_slots (appointment_date, appoint_start_time, appoint_end_time, appoint_interval_time, appointment_charge) " +
                    "VALUES (:appointmentDate, :appointStartTime, :appointEndTime, :appointIntervalTime, :appointmentCharge)";

    private static final String SELECT_APPOINTMENT_SLOTS_BY_ID_TEMPLATE =
            "SELECT * FROM %s.appointment_slots WHERE id = :id";

    private static final String SELECT_ALL_APPOINTMENT_SLOTS_TEMPLATE =
            "SELECT * FROM %s.appointment_slots";

    private static final String DELETE_APPOINTMENT_SLOTS_BY_ID_TEMPLATE =
            "DELETE FROM %s.appointment_slots WHERE id = :id";

    @Override
    @Transactional
    public AppointmentSlots save(AppointmentSlots appointmentSlots) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String insertSql = String.format(INSERT_APPOINTMENT_SLOTS_TEMPLATE, tenantSchema);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("appointmentDate", appointmentSlots.getAppointmentDate());
        params.addValue("appointStartTime", appointmentSlots.getAppointStartTime());
        params.addValue("appointEndTime", appointmentSlots.getAppointEndTime());
        params.addValue("appointIntervalTime", appointmentSlots.getAppointIntervalTime());
        params.addValue("appointmentCharge", appointmentSlots.getAppointmentCharge());

        int rowsAffected = jdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});
        if (rowsAffected > 0) {
            Number key = keyHolder.getKey();
            if (key != null) {
                appointmentSlots.setId(key.longValue());
                return appointmentSlots;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public AppointmentSlots update(AppointmentSlots updatedAppointmentSlots) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String updateSql = String.format("UPDATE %s.appointment_slots SET appointment_date = :appointmentDate, appoint_start_time = :appointStartTime, appoint_end_time = :appointEndTime, appoint_interval_time = :appointIntervalTime, appointment_charge = :appointmentCharge WHERE id = :id", tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("appointmentDate", updatedAppointmentSlots.getAppointmentDate());
        params.addValue("appointStartTime", updatedAppointmentSlots.getAppointStartTime());
        params.addValue("appointEndTime", updatedAppointmentSlots.getAppointEndTime());
        params.addValue("appointIntervalTime", updatedAppointmentSlots.getAppointIntervalTime());
        params.addValue("appointmentCharge", updatedAppointmentSlots.getAppointmentCharge());
        params.addValue("id", updatedAppointmentSlots.getId());

        jdbcTemplate.update(updateSql, params);
        return updatedAppointmentSlots;
    }

    @Override
    public AppointmentSlots findById(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectSql = String.format(SELECT_APPOINTMENT_SLOTS_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Optional<AppointmentSlots> appointmentSlots = jdbcTemplate.query(selectSql, params, new AppointmentSlotsRowMapper()).stream().findFirst();
        return appointmentSlots.orElse(null);
    }

    @Override
    public List<AppointmentSlots> findAll() {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectAllSql = String.format(SELECT_ALL_APPOINTMENT_SLOTS_TEMPLATE, tenantSchema);
        return jdbcTemplate.query(selectAllSql, new AppointmentSlotsRowMapper());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String deleteSql = String.format(DELETE_APPOINTMENT_SLOTS_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(deleteSql, params);
    }

    private static class AppointmentSlotsRowMapper implements RowMapper<AppointmentSlots> {
        @Override
        public AppointmentSlots mapRow(ResultSet rs, int rowNum) throws SQLException {
            AppointmentSlots appointmentSlots = new AppointmentSlots();
            appointmentSlots.setId(rs.getLong("id"));
            appointmentSlots.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
            appointmentSlots.setAppointStartTime(rs.getTime("appoint_start_time").toLocalTime());
            appointmentSlots.setAppointEndTime(rs.getTime("appoint_end_time").toLocalTime());
            appointmentSlots.setAppointIntervalTime(rs.getInt("appoint_interval_time"));
            appointmentSlots.setAppointmentCharge(rs.getBigDecimal("appointment_charge"));
            return appointmentSlots;
        }
    }
}
