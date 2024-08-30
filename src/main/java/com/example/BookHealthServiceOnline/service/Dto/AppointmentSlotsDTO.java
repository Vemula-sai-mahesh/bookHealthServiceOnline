package com.example.BookHealthServiceOnline.service.Dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentSlotsDTO {

    @NotNull
    private LocalDate appointmentDate;

    @NotNull
    private LocalTime appointStartTime;

    @NotNull
    private LocalTime appointEndTime;

    @NotNull
    private Integer appointIntervalTime;

    @NotNull
    private BigDecimal appointmentCharge; // Changed from Integer to BigDecimal

    // Getters and setters

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

    public void setAppointmentCharge(BigDecimal appointmentCharge) { // Updated to accept BigDecimal
        this.appointmentCharge = appointmentCharge;
    }
}
