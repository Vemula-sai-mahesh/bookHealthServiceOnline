package com.example.BookHealthServiceOnline.service.Dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalTime;

public class DocDeptCategoryDTO {

    @NotNull
    private Long doctorId;

    @NotNull
    private Long departmentId;

    private LocalTime availableFrom;

    private LocalTime availableTo;

    private Integer availableTimeInterval;

    @NotNull
    private BigDecimal chargesPerTimeInterval; // Changed from Integer to BigDecimal

    // Getters and setters

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public LocalTime getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalTime availableFrom) {
        this.availableFrom = availableFrom;
    }

    public LocalTime getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(LocalTime availableTo) {
        this.availableTo = availableTo;
    }

    public Integer getAvailableTimeInterval() {
        return availableTimeInterval;
    }

    public void setAvailableTimeInterval(Integer availableTimeInterval) {
        this.availableTimeInterval = availableTimeInterval;
    }

    public BigDecimal getChargesPerTimeInterval() {
        return chargesPerTimeInterval;
    }

    public void setChargesPerTimeInterval(BigDecimal chargesPerTimeInterval) { // Updated to accept BigDecimal
        this.chargesPerTimeInterval = chargesPerTimeInterval;
    }
}
