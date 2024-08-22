package com.example.BookHealthServiceOnline.controller;


import com.example.BookHealthServiceOnline.domain.Hospital;
import com.example.BookHealthServiceOnline.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    @Autowired
    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PostMapping
    public ResponseEntity<Hospital> createHospital(@RequestBody Hospital hospital) {
        Hospital savedHospital = hospitalService.save(hospital);
        return new ResponseEntity<>(savedHospital, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hospital> getHospitalById(@PathVariable Long id) {
        Hospital hospital = hospitalService.findById(id);
        if (hospital != null) {
            return new ResponseEntity<>(hospital, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Hospital>> getAllHospitals() {
        List<Hospital> hospitals = hospitalService.findAll();
        return new ResponseEntity<>(hospitals, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        hospitalService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hospital> updateHospital(@PathVariable Long id, @RequestBody Hospital hospital) {
        Hospital existingHospital = hospitalService.findById(id);
        if (existingHospital != null) {
            hospital.setId(id);
            Hospital updatedHospital = hospitalService.save(hospital);
            return new ResponseEntity<>(updatedHospital, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
