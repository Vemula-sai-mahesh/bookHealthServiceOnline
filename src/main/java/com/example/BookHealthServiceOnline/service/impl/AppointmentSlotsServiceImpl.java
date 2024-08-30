package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.AppointmentSlotsDao;
import com.example.BookHealthServiceOnline.domain.AppointmentSlots;
import com.example.BookHealthServiceOnline.service.AppointmentSlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentSlotsServiceImpl implements AppointmentSlotsService {

    @Autowired
    private AppointmentSlotsDao appointmentSlotsDao;

    @Override
    public AppointmentSlots save(AppointmentSlots appointmentSlots) {
        return appointmentSlotsDao.save(appointmentSlots);
    }

    @Override
    public AppointmentSlots findById(Long id) {
        return appointmentSlotsDao.findById(id);
    }

    @Override
    public List<AppointmentSlots> findAll() {
        return appointmentSlotsDao.findAll();
    }

    @Override
    public AppointmentSlots update(AppointmentSlots appointmentSlots) {
        if (appointmentSlots.getId() != null) {
            return appointmentSlotsDao.update(appointmentSlots);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        appointmentSlotsDao.delete(id);
    }
}
