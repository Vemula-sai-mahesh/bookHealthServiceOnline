package com.example.BookHealthServiceOnline.controller;

import com.example.BookHealthServiceOnline.domain.AppointmentSlots;
import com.example.BookHealthServiceOnline.service.Dto.AppointmentSlotsDTO;
import com.example.BookHealthServiceOnline.service.AppointmentSlotsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment-slots")
public class AppointmentSlotsController {

    @Autowired
    private AppointmentSlotsService appointmentSlotsService;

    @PostMapping
    public ResponseEntity<String> createAppointmentSlot(
            @RequestHeader("X-PrivateTenant") String tenant,
            @Valid @RequestBody AppointmentSlotsDTO appointmentSlotsDTO) {

        AppointmentSlots appointmentSlots = new AppointmentSlots();
        appointmentSlots.setAppointmentDate(appointmentSlotsDTO.getAppointmentDate());
        appointmentSlots.setAppointStartTime(appointmentSlotsDTO.getAppointStartTime());
        appointmentSlots.setAppointEndTime(appointmentSlotsDTO.getAppointEndTime());
        appointmentSlots.setAppointIntervalTime(appointmentSlotsDTO.getAppointIntervalTime());
        appointmentSlots.setAppointmentCharge(appointmentSlotsDTO.getAppointmentCharge());

        appointmentSlotsService.save(appointmentSlots);

        return new ResponseEntity<>("AppointmentSlot created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentSlots> getAppointmentSlotById(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id) {
        AppointmentSlots appointmentSlots = appointmentSlotsService.findById(id);
        return (appointmentSlots != null) ? ResponseEntity.ok(appointmentSlots) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentSlots>> getAllAppointmentSlots(
            @RequestHeader("X-PrivateTenant") String tenant) {
        List<AppointmentSlots> appointmentSlotsList = appointmentSlotsService.findAll();
        return ResponseEntity.ok(appointmentSlotsList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAppointmentSlot(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id,
            @Valid @RequestBody AppointmentSlotsDTO appointmentSlotsDTO) {

        AppointmentSlots appointmentSlots = appointmentSlotsService.findById(id);

        if (appointmentSlots == null) {
            return new ResponseEntity<>("AppointmentSlot not found", HttpStatus.NOT_FOUND);
        }

        appointmentSlots.setAppointmentDate(appointmentSlotsDTO.getAppointmentDate());
        appointmentSlots.setAppointStartTime(appointmentSlotsDTO.getAppointStartTime());
        appointmentSlots.setAppointEndTime(appointmentSlotsDTO.getAppointEndTime());
        appointmentSlots.setAppointIntervalTime(appointmentSlotsDTO.getAppointIntervalTime());
        appointmentSlots.setAppointmentCharge(appointmentSlotsDTO.getAppointmentCharge());

        appointmentSlotsService.update(appointmentSlots);

        return new ResponseEntity<>("AppointmentSlot updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointmentSlot(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id) {
        AppointmentSlots appointmentSlots = appointmentSlotsService.findById(id);

        if (appointmentSlots == null) {
            return new ResponseEntity<>("AppointmentSlot not found", HttpStatus.NOT_FOUND);
        }

        appointmentSlotsService.delete(id);
        return new ResponseEntity<>("AppointmentSlot deleted successfully", HttpStatus.NO_CONTENT);
    }
}
