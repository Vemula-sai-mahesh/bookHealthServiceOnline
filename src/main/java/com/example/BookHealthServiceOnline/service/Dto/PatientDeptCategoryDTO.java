package com.example.BookHealthServiceOnline.service.Dto;

import jakarta.validation.constraints.NotNull;

public class PatientDeptCategoryDTO {

    @NotNull
    private Long patientId;

    @NotNull
    private Long departmentId;

    // Getters and setters

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
