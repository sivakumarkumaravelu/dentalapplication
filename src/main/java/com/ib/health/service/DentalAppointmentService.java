package com.ib.health.service;

import com.ib.health.bean.DentalAppointment;
import com.ib.health.exception.AppointmentNotFoundException;
import com.ib.health.exception.DataValidationException;
import com.ib.health.validator.DentalAppointmentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/*
Service class for Dental appointment
 */

@Component
public class DentalAppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(DentalAppointmentService.class);

    @Autowired
    DentalAppointmentValidator validator;
    protected ConcurrentHashMap<Integer, DentalAppointment> map = new ConcurrentHashMap<>();
    protected final AtomicInteger id = new AtomicInteger();


    public DentalAppointment getDentalAppointmentById(int id) {
        DentalAppointment appointment = map.get(id);
        if (appointment == null) {
            logger.info("DentalAppointment.getDentalAppointmentById(): DentalAppointmentById not found with id: " + id);
            throw new AppointmentNotFoundException(id);
        }
        return appointment;
    }

    public DentalAppointment createDentalAppointment(DentalAppointment appointment) {
        DentalAppointment newAppointment = null;
        if (validator.validateDateInterval(appointment.getStartTime(), appointment.getEndTime())) {
            if (validator.isValidDentistAppointment(appointment, map)) {
                //TODO Appointments are in memory so leaving as such. If DAO layer is created need to move there.
                int appointmentId = id.incrementAndGet();
                newAppointment = new DentalAppointment(appointmentId, appointment.getDentist_id(), appointment.getPatient_id(), appointment.getStartTime(), appointment.getEndTime());
                map.put(appointmentId, newAppointment);
            } else {
                logger.info("DentalAppointment.createDentalAppointment(): Dentist appointment not available");
                throw new DataValidationException("Dentist appointment not available, please try a different time");
            }
        } else {
            logger.info("DentalAppointment.createDentalAppointment(): Appointment time requested is invalid");
            throw new DataValidationException("Invalid appointment time requested");
        }
        return newAppointment;
    }

}
