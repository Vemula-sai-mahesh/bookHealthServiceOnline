package com.example.BookHealthServiceOnline.service.Dto;

public class AppointmentsDTO {
    private Long patientId;
    private Long doctorId;
    private Long appointmentSlotId;
    private String status; // or AppointmentStatus if using the enum directly
    private String consultationType; // or ConsultationType if using the enum directly
    private String notes;

    // Getters and Setters
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
