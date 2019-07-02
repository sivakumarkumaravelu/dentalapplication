package com.ib.health.service;

import com.ib.health.bean.DentalAppointment;
import com.ib.health.exception.AppointmentNotFoundException;
import com.ib.health.exception.DataValidationException;
import com.ib.health.validator.DentalAppointmentValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DentalAppointmentServiceTests {

    @TestConfiguration
    static class DentalAppointmentServiceTestContextConfiguration {
        @Bean
        public DentalAppointmentService dentalAppointmentService() {
            DentalAppointmentService dentalAppointmentService = new DentalAppointmentService();
            DentalAppointment appointment = new DentalAppointment(1, 1, 2, new Long("1564779965"), new Long("1564783565"));
            ConcurrentHashMap<Integer, DentalAppointment> map = new ConcurrentHashMap<>();
            map.put(1, appointment);

            dentalAppointmentService.map = map;
            return dentalAppointmentService;
        }

        @Bean
        public DentalAppointmentValidator dentalAppointmentValidator() {
            return new DentalAppointmentValidator();
        }
    }

    @Autowired
    DentalAppointmentService service;

    @Test
    public void get_dental_appointment_Id_OK() {
        DentalAppointment appointment = new DentalAppointment(1, 1, 2, new Long("1564779965"), new Long("1564783565"));
        assertEquals(appointment, service.getDentalAppointmentById(1));

    }
    @Test(expected = AppointmentNotFoundException.class)
    public void get_dental_appointment_Id_ERROR() {
        service.getDentalAppointmentById(2);
    }

    @Test
    public void create_dental_appointment_Id_OK() {
        DentalAppointment appointment = new DentalAppointment(1, 3, 4, new Long("1588524724000"), new Long("1588528324000"));
        assertEquals(appointment, service.createDentalAppointment(appointment));
    }

    @Test(expected = DataValidationException.class)
    public void create_dental_appointment_Id_INVALIDTIME() {
        DentalAppointment appointment = new DentalAppointment(1, 3, 4, new Long("1564779965"), new Long("1564779965"));
        assertEquals(appointment, service.createDentalAppointment(appointment));
    }

    @Test(expected = DataValidationException.class)
    public void create_dental_appointment_Id_INVALIDAPPOINTMENT() {
        DentalAppointment appointment = new DentalAppointment(1, 1, 2, new Long("1564779965"), new Long("1564783565"));
        assertEquals(appointment, service.createDentalAppointment(appointment));
    }

}
