package com.example.BookHealthServiceOnline.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "doc_dept_category")
public class DocDeptCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "available_from", nullable = false)
    private LocalTime availableFrom;

    @Column(name = "available_to", nullable = false)
    private LocalTime availableTo;

    @Column(name = "available_time_interval", nullable = false)
    private Integer availableTimeInterval;

    @Column(name = "charges_per_time_interval", nullable = false, precision = 10, scale = 2)
    private BigDecimal chargesPerTimeInterval;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public void setChargesPerTimeInterval(BigDecimal chargesPerTimeInterval) {
        this.chargesPerTimeInterval = chargesPerTimeInterval;
    }
}
