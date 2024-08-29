package com.example.BookHealthServiceOnline.service.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class HospitalServiceDTO {

    @NotBlank
    @Size(max = 100)
    private String serviceName;

    @Size(max = 500)
    private String description;

    // Getters and setters

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
