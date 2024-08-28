package com.example.BookHealthServiceOnline.domain;

import com.example.BookHealthServiceOnline.service.DepartmentService;
import jakarta.persistence.*;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "hospital_services")
public class HospitalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Transient
    @Autowired
    private DepartmentService departmentService;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDepartmentId(Long departmentId) {
        if (departmentId != null) {
            // Create a new Department instance
            Department department = new Department();
            department.setId(departmentId);
            this.department = department;
        } else {
            this.department = null;
        }
    }
}
