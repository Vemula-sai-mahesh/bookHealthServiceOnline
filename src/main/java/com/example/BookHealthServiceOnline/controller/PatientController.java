package com.example.BookHealthServiceOnline.controller;

import com.example.BookHealthServiceOnline.domain.Patient;

import com.example.BookHealthServiceOnline.service.Dto.PatientDTO;
import com.example.BookHealthServiceOnline.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<String> createPatient(
            @RequestHeader("X-PrivateTenant") String tenant,
            @Valid @RequestBody PatientDTO patientDTO) {

        // Convert PatientDTO to Patient entity
        Patient patient = new Patient();
        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setGender(Patient.Gender.valueOf(patientDTO.getGender())); // Convert String to Gender enum
        patient.setContactNumber(patientDTO.getContactNumber());
        patient.setEmail(patientDTO.getEmail());
        patient.setAddress(patientDTO.getAddress());
        patient.setEmergencyContact(patientDTO.getEmergencyContact());

        // Save the Patient entity to the database
        patientService.save(patient);

        // Return a response indicating success
        return new ResponseEntity<>("Patient created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(  @RequestHeader("X-PrivateTenant") String tenant,@PathVariable Long id) {
        Patient patient = patientService.findById(id);
        return (patient != null) ? ResponseEntity.ok(patient) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients( @RequestHeader("X-PrivateTenant") String tenant) {
        List<Patient> patients = patientService.findAll();
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePatient(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id,
            @Valid @RequestBody PatientDTO patientDTO) {

        // Fetch existing patient
        Patient patient = patientService.findById(id);

        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setGender(Patient.Gender.valueOf(patientDTO.getGender())); // Convert String to Gender enum
        patient.setContactNumber(patientDTO.getContactNumber());
        patient.setEmail(patientDTO.getEmail());
        patient.setAddress(patientDTO.getAddress());
        patient.setEmergencyContact(patientDTO.getEmergencyContact());

        // Save the updated patient
        patientService.update(patient);

        return new ResponseEntity<>("Patient updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(  @RequestHeader("X-PrivateTenant") String tenant,@PathVariable Long id) {
        Patient patient = patientService.findById(id);

        if (patient==null) {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }

        patientService.delete(id);
        return new ResponseEntity<>("Patient deleted successfully", HttpStatus.NO_CONTENT);
    }
}