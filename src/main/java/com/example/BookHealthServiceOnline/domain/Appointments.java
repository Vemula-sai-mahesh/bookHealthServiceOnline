package com.example.BookHealthServiceOnline.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "appointments")
public class Appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "appointment_slot_id", nullable = false)
    private Long appointmentSlotId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "consultation_type", nullable = false)
    private ConsultationType consultationType;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getAppointmentSlotId() {
        return appointmentSlotId;
    }

    public void setAppointmentSlotId(Long appointmentSlotId) {
        this.appointmentSlotId = appointmentSlotId;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public ConsultationType getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(ConsultationType consultationType) {
        this.consultationType = consultationType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public enum AppointmentStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    public enum ConsultationType {
        IN_PERSON, ONLINE
    }
}
