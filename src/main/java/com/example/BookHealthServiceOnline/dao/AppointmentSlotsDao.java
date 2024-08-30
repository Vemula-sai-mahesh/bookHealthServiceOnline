package com.example.BookHealthServiceOnline.dao;

import com.example.BookHealthServiceOnline.domain.AppointmentSlots;

import java.util.List;

public interface AppointmentSlotsDao {
    AppointmentSlots save(AppointmentSlots appointmentSlots);
    AppointmentSlots update(AppointmentSlots appointmentSlots);
    AppointmentSlots findById(Long id);
    List<AppointmentSlots> findAll();
    void delete(Long id);
}
