package com.example.BookHealthServiceOnline.service.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartmentDTO {

    @NotBlank
    @Size(max = 100)
    private String departmentName;

    @Size(max = 500)
    private String description;

    private Long serviceId; // Service ID can be null if not applicable

    // Getters and setters

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}
