package com.ib.health.service;

import com.ib.health.bean.DentalAppointment;
import com.ib.health.exception.AppointmentNotFoundException;
import com.ib.health.exception.DataValidationException;
import com.ib.health.validator.DentalAppointmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DentalAppointmentService {

    @Autowired
    DentalAppointmentValidator validator;
    protected ConcurrentHashMap<Integer, DentalAppointment> map = new ConcurrentHashMap<>();
    protected final AtomicInteger id = new AtomicInteger();


    public DentalAppointment getDentalAppointmentById(int id) {
        DentalAppointment appointment = map.get(id);
        if (appointment == null) {
            throw new AppointmentNotFoundException(id);
        }
        return map.get(id);
    }

    public DentalAppointment createDentalAppointment(DentalAppointment appointment) {
        DentalAppointment newAppointment = null;
        if (validator.validateDateInterval(appointment.getStartTime(), appointment.getEndTime())) {
            if (validator.isValidDentistAppointment(appointment, map)) {
                int appointmentId = id.incrementAndGet();
                newAppointment = new DentalAppointment(appointmentId, appointment.getDentist_id(), appointment.getPatient_id(), appointment.getStartTime(), appointment.getEndTime());
                map.put(appointmentId, newAppointment);
            } else {
                throw new DataValidationException("Dentist appointment not available, please try a different time");
            }
        } else {
            throw new DataValidationException("Invalid appointment time requested");
        }
        return newAppointment;
    }

}
