package com.example.BookHealthServiceOnline.controller;

import com.example.BookHealthServiceOnline.domain.Doctor;
import com.example.BookHealthServiceOnline.service.Dto.DoctorDTO;
import com.example.BookHealthServiceOnline.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping
    public ResponseEntity<String> createDoctor(
            @RequestHeader("X-PrivateTenant") String tenant,
            @Valid @RequestBody DoctorDTO doctorDTO) {

        // Convert DoctorDTO to Doctor entity
        Doctor doctor = new Doctor();
        doctor.setFirstName(doctorDTO.getFirstName());
        doctor.setLastName(doctorDTO.getLastName());
        doctor.setSpecialty(doctorDTO.getSpecialty());
        doctor.setQualification(doctorDTO.getQualification());
        doctor.setExperienceYears(doctorDTO.getExperienceYears());
        doctor.setContactNumber(doctorDTO.getContactNumber());
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setUserId(doctorDTO.getUserId()); // Assuming userId is part of the DTO

        // Save the Doctor entity to the database
        doctorService.save(doctor);

        // Return a response indicating success
        return new ResponseEntity<>("Doctor created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@RequestHeader("X-PrivateTenant") String tenant, @PathVariable Long id) {
        Doctor doctor = doctorService.findById(id);
        return (doctor != null) ? ResponseEntity.ok(doctor) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors(@RequestHeader("X-PrivateTenant") String tenant) {
        List<Doctor> doctors = doctorService.findAll();
        return ResponseEntity.ok(doctors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDoctor(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id,
            @Valid @RequestBody DoctorDTO doctorDTO) {

        // Fetch existing doctor
        Doctor doctor = doctorService.findById(id);

        if (doctor != null) {
            doctor.setFirstName(doctorDTO.getFirstName());
            doctor.setLastName(doctorDTO.getLastName());
            doctor.setSpecialty(doctorDTO.getSpecialty());
            doctor.setQualification(doctorDTO.getQualification());
            doctor.setExperienceYears(doctorDTO.getExperienceYears());
            doctor.setContactNumber(doctorDTO.getContactNumber());
            doctor.setEmail(doctorDTO.getEmail());
            doctor.setUserId(doctorDTO.getUserId()); // Assuming userId is part of the DTO

            // Save the updated doctor
            doctorService.update(doctor);

            return new ResponseEntity<>("Doctor updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@RequestHeader("X-PrivateTenant") String tenant, @PathVariable Long id) {
        Doctor doctor = doctorService.findById(id);

        if (doctor != null) {
            doctorService.delete(id);
            return new ResponseEntity<>("Doctor deleted successfully", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
        }
    }
}
