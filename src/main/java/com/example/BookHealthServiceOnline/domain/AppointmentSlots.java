package com.example.BookHealthServiceOnline.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointment_slots")
public class AppointmentSlots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appoint_start_time", nullable = false)
    private LocalTime appointStartTime;

    @Column(name = "appoint_end_time", nullable = false)
    private LocalTime appointEndTime;

    @Column(name = "appoint_interval_time", nullable = false)
    private Integer appointIntervalTime;

    @Column(name = "appointment_charge", nullable = false, precision = 10, scale = 2)
    private BigDecimal appointmentCharge;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointStartTime() {
        return appointStartTime;
    }

    public void setAppointStartTime(LocalTime appointStartTime) {
        this.appointStartTime = appointStartTime;
    }

    public LocalTime getAppointEndTime() {
        return appointEndTime;
    }

    public void setAppointEndTime(LocalTime appointEndTime) {
        this.appointEndTime = appointEndTime;
    }

    public Integer getAppointIntervalTime() {
        return appointIntervalTime;
    }

    public void setAppointIntervalTime(Integer appointIntervalTime) {
        this.appointIntervalTime = appointIntervalTime;
    }

    public BigDecimal getAppointmentCharge() {
        return appointmentCharge;
    }

    public void setAppointmentCharge(BigDecimal appointmentCharge) {
        this.appointmentCharge = appointmentCharge;
    }
}
