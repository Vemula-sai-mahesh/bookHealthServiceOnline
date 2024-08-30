package com.example.BookHealthServiceOnline.dao;

import com.example.BookHealthServiceOnline.domain.Appointments;

import java.util.List;

public interface AppointmentsDao {
    Appointments save(Appointments appointments);
    Appointments update(Appointments appointments);
    Appointments findById(Long id);
    List<Appointments> findAll();
    void delete(Long id);
}
