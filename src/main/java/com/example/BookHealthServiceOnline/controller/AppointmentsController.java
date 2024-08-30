package com.example.BookHealthServiceOnline.controller;

import com.example.BookHealthServiceOnline.domain.Appointments;
import com.example.BookHealthServiceOnline.service.Dto.AppointmentsDTO;
import com.example.BookHealthServiceOnline.service.AppointmentsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentsController {

    @Autowired
    private AppointmentsService appointmentsService;

//    @PostMapping
//    public ResponseEntity<String> createAppointment(
//            @RequestHeader("X-PrivateTenant") String tenant,
//            @Valid @RequestBody AppointmentsDTO appointmentsDTO) {
//
//        Appointments appointments = new Appointments();
//        appointments.setPatientId(appointmentsDTO.getPatientId());
//        appointments.setDoctorId(appointmentsDTO.getDoctorId());
////        appointments.setAppointmentDate(appointmentsDTO.getAppointmentDate());
//        appointments.setAppointmentSlotId(appointmentsDTO.getAppointmentSlotId());
//
//        appointmentsService.save(appointments);
//
//        return new ResponseEntity<>("Appointment created successfully", HttpStatus.CREATED);
//    }



    @PostMapping
    public ResponseEntity<String> createAppointment(
            @RequestHeader("X-PrivateTenant") String tenant,
            @Valid @RequestBody AppointmentsDTO appointmentsDTO) {

        Appointments appointments = new Appointments();
        appointments.setPatientId(appointmentsDTO.getPatientId());
        appointments.setDoctorId(appointmentsDTO.getDoctorId());
        appointments.setAppointmentSlotId(appointmentsDTO.getAppointmentSlotId());

        // Convert status and consultationType from String to Enum
        try {
            appointments.setStatus(Appointments.AppointmentStatus.valueOf(appointmentsDTO.getStatus()));
            appointments.setConsultationType(Appointments.ConsultationType.valueOf(appointmentsDTO.getConsultationType()));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid status or consultation type", HttpStatus.BAD_REQUEST);
        }

        appointments.setNotes(appointmentsDTO.getNotes());

        appointmentsService.save(appointments);

        return new ResponseEntity<>("Appointment created successfully", HttpStatus.CREATED);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Appointments> getAppointmentById(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id) {
        Appointments appointments = appointmentsService.findById(id);
        return (appointments != null) ? ResponseEntity.ok(appointments) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Appointments>> getAllAppointments(
            @RequestHeader("X-PrivateTenant") String tenant) {
        List<Appointments> appointmentsList = appointmentsService.findAll();
        return ResponseEntity.ok(appointmentsList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAppointment(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id,
            @Valid @RequestBody AppointmentsDTO appointmentsDTO) {

        Appointments appointments = appointmentsService.findById(id);

        if (appointments == null) {
            return new ResponseEntity<>("Appointment not found", HttpStatus.NOT_FOUND);
        }

        appointments.setPatientId(appointmentsDTO.getPatientId());
        appointments.setDoctorId(appointmentsDTO.getDoctorId());
//        appointments.setAppointmentDate(appointmentsDTO.getAppointmentDate());
        appointments.setAppointmentSlotId(appointmentsDTO.getAppointmentSlotId());

        appointmentsService.update(appointments);

        return new ResponseEntity<>("Appointment updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id) {
        Appointments appointments = appointmentsService.findById(id);

        if (appointments == null) {
            return new ResponseEntity<>("Appointment not found", HttpStatus.NOT_FOUND);
        }

        appointmentsService.delete(id);
        return new ResponseEntity<>("Appointment deleted successfully", HttpStatus.NO_CONTENT);
    }
}
