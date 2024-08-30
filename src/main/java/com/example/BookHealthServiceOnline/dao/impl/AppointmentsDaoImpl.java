package com.example.BookHealthServiceOnline.dao.impl;

import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.AppointmentsDao;
import com.example.BookHealthServiceOnline.domain.Appointments;
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
public class AppointmentsDaoImpl implements AppointmentsDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AppointmentsDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_APPOINTMENTS_TEMPLATE =
            "INSERT INTO %s.appointments (patient_id, doctor_id, appointment_slot_id, status, consultation_type, notes) " +
                    "VALUES (:patientId, :doctorId, :appointmentSlotId, :status, :consultationType, :notes)";

    private static final String SELECT_APPOINTMENTS_BY_ID_TEMPLATE =
            "SELECT * FROM %s.appointments WHERE id = :id";

    private static final String SELECT_ALL_APPOINTMENTS_TEMPLATE =
            "SELECT * FROM %s.appointments";

    private static final String DELETE_APPOINTMENTS_BY_ID_TEMPLATE =
            "DELETE FROM %s.appointments WHERE id = :id";

    @Override
    @Transactional
    public Appointments save(Appointments appointments) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String insertSql = String.format(INSERT_APPOINTMENTS_TEMPLATE, tenantSchema);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("patientId", appointments.getPatientId());
        params.addValue("doctorId", appointments.getDoctorId());
        params.addValue("appointmentSlotId", appointments.getAppointmentSlotId());
        params.addValue("status", appointments.getStatus().name());
        params.addValue("consultationType", appointments.getConsultationType().name());
        params.addValue("notes", appointments.getNotes());

        int rowsAffected = jdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});
        if (rowsAffected > 0) {
            Number key = keyHolder.getKey();
            if (key != null) {
                appointments.setId(key.longValue());
                return appointments;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Appointments update(Appointments updatedAppointments) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String updateSql = String.format("UPDATE %s.appointments SET patient_id = :patientId, doctor_id = :doctorId, appointment_slot_id = :appointmentSlotId, status = :status, consultation_type = :consultationType, notes = :notes WHERE id = :id", tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("patientId", updatedAppointments.getPatientId());
        params.addValue("doctorId", updatedAppointments.getDoctorId());
        params.addValue("appointmentSlotId", updatedAppointments.getAppointmentSlotId());
        params.addValue("status", updatedAppointments.getStatus().name());
        params.addValue("consultationType", updatedAppointments.getConsultationType().name());
        params.addValue("notes", updatedAppointments.getNotes());
        params.addValue("id", updatedAppointments.getId());

        jdbcTemplate.update(updateSql, params);
        return updatedAppointments;
    }

    @Override
    public Appointments findById(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectSql = String.format(SELECT_APPOINTMENTS_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Optional<Appointments> appointments = jdbcTemplate.query(selectSql, params, new AppointmentsRowMapper()).stream().findFirst();
        return appointments.orElse(null);
    }

    @Override
    public List<Appointments> findAll() {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String selectAllSql = String.format(SELECT_ALL_APPOINTMENTS_TEMPLATE, tenantSchema);
        return jdbcTemplate.query(selectAllSql, new AppointmentsRowMapper());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String tenantSchema = AppTenantContext.getCurrentTenant();
        String deleteSql = String.format(DELETE_APPOINTMENTS_BY_ID_TEMPLATE, tenantSchema);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(deleteSql, params);
    }

    private static class AppointmentsRowMapper implements RowMapper<Appointments> {
        @Override
        public Appointments mapRow(ResultSet rs, int rowNum) throws SQLException {
            Appointments appointments = new Appointments();
            appointments.setId(rs.getLong("id"));
            appointments.setPatientId(rs.getLong("patient_id"));
            appointments.setDoctorId(rs.getLong("doctor_id"));
            appointments.setAppointmentSlotId(rs.getLong("appointment_slot_id"));
            appointments.setStatus(Appointments.AppointmentStatus.valueOf(rs.getString("status")));
            appointments.setConsultationType(Appointments.ConsultationType.valueOf(rs.getString("consultation_type")));
            appointments.setNotes(rs.getString("notes"));
            return appointments;
        }
    }
}
