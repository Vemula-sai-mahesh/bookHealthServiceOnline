package com.example.BookHealthServiceOnline.service;

import com.example.BookHealthServiceOnline.domain.Appointments;

import java.util.List;

public interface AppointmentsService {

    Appointments save(Appointments appointments);
    Appointments update(Appointments appointments);
    Appointments findById(Long id);
    List<Appointments> findAll();
    void delete(Long id);
}
