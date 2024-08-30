package com.example.BookHealthServiceOnline.service;

import com.example.BookHealthServiceOnline.domain.AppointmentSlots;

import java.util.List;

public interface AppointmentSlotsService {

    AppointmentSlots save(AppointmentSlots appointmentSlots);
    AppointmentSlots update(AppointmentSlots appointmentSlots);
    AppointmentSlots findById(Long id);
    List<AppointmentSlots> findAll();
    void delete(Long id);
}
