package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.AppointmentsDao;
import com.example.BookHealthServiceOnline.domain.Appointments;
import com.example.BookHealthServiceOnline.service.AppointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentsServiceImpl implements AppointmentsService {

    @Autowired
    private AppointmentsDao appointmentsDao;

    @Override
    public Appointments save(Appointments appointments) {
        return appointmentsDao.save(appointments);
    }

    @Override
    public Appointments findById(Long id) {
        return appointmentsDao.findById(id);
    }

    @Override
    public List<Appointments> findAll() {
        return appointmentsDao.findAll();
    }

    @Override
    public Appointments update(Appointments appointments) {
        if (appointments.getId() != null) {
            return appointmentsDao.update(appointments);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        appointmentsDao.delete(id);
    }
}
