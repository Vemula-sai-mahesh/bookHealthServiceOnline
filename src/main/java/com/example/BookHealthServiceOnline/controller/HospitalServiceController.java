package com.example.BookHealthServiceOnline.controller;

import com.example.BookHealthServiceOnline.domain.HospitalService;
import com.example.BookHealthServiceOnline.service.Dto.HospitalServiceDTO;
import com.example.BookHealthServiceOnline.service.HospitalServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospital-services")
public class HospitalServiceController {

    @Autowired
    private HospitalServiceService hospitalServiceService;

    @PostMapping
    public ResponseEntity<String> createHospitalService(
            @RequestHeader("X-PrivateTenant") String tenant,
            @Valid @RequestBody HospitalServiceDTO hospitalServiceDTO) {

        // Convert HospitalServiceDTO to HospitalService entity
        HospitalService hospitalService = new HospitalService();
        hospitalService.setServiceName(hospitalServiceDTO.getServiceName());
        hospitalService.setDescription(hospitalServiceDTO.getDescription());
        hospitalService.setDepartmentId(hospitalServiceDTO.getDepartmentId());
        hospitalService.setPrice(hospitalServiceDTO.getPrice());

        // Save the HospitalService entity to the database
        hospitalServiceService.save(hospitalService);

        // Return a response indicating success
        return new ResponseEntity<>("Hospital Service created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalService> getHospitalServiceById(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id) {
        HospitalService hospitalService = hospitalServiceService.findById(id);
        return (hospitalService != null) ? ResponseEntity.ok(hospitalService) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<HospitalService>> getAllHospitalServices(
            @RequestHeader("X-PrivateTenant") String tenant) {
        List<HospitalService> hospitalServices = hospitalServiceService.findAll();
        return ResponseEntity.ok(hospitalServices);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateHospitalService(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id,
            @Valid @RequestBody HospitalServiceDTO hospitalServiceDTO) {

        // Fetch existing hospital service
        HospitalService hospitalService = hospitalServiceService.findById(id);

        if (hospitalService == null) {
            return new ResponseEntity<>("Hospital Service not found", HttpStatus.NOT_FOUND);
        }

        hospitalService.setServiceName(hospitalServiceDTO.getServiceName());
        hospitalService.setDescription(hospitalServiceDTO.getDescription());
        hospitalService.setDepartmentId(hospitalServiceDTO.getDepartmentId());
        hospitalService.setPrice(hospitalServiceDTO.getPrice());

        // Save the updated hospital service
        hospitalServiceService.update(hospitalService);

        return new ResponseEntity<>("Hospital Service updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHospitalService(
            @RequestHeader("X-PrivateTenant") String tenant,
            @PathVariable Long id) {
        HospitalService hospitalService = hospitalServiceService.findById(id);

        if (hospitalService == null) {
            return new ResponseEntity<>("Hospital Service not found", HttpStatus.NOT_FOUND);
        }

        hospitalServiceService.delete(id);
        return new ResponseEntity<>("Hospital Service deleted successfully", HttpStatus.NO_CONTENT);
    }
}

