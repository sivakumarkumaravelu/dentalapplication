package com.ib.health.validator;


import com.ib.health.bean.DentalAppointment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DentalAppointmentValidatorTest {

    @TestConfiguration
    static class DentalAppointmentValidatorTestContextConfiguration {
        @Bean
        public DentalAppointmentValidator dentalAppointmentValidator() {
            return new DentalAppointmentValidator();
        }
    }

    @Autowired
    DentalAppointmentValidator validator;

    @Test
    public void valid_dentist_appointment_NEW() {
        DentalAppointment appointment = new DentalAppointment(1, 1, 2, new Long("1564779965"), new Long("1564783565"));
        ConcurrentHashMap<Integer, DentalAppointment> map = new ConcurrentHashMap<>();
        map.put(1, appointment);
        assertEquals(true, validator.isValidDentistAppointment(appointment, null));

    }

    @Test
    public void valid_dentist_appointment_EXISTING() {
        DentalAppointment appointment1 = new DentalAppointment(1, 1, 2, new Long("1564779965"), new Long("1564783565"));
        DentalAppointment appointment2 = new DentalAppointment(1, 1, 2, new Long("1564779965"), new Long("1564783565"));
        ConcurrentHashMap<Integer, DentalAppointment> map = new ConcurrentHashMap<>();
        map.put(1, appointment1);
        assertEquals(false, validator.isValidDentistAppointment(appointment2, map));

    }

    @Test
    public void conflicting_appointment_OK() {
        DentalAppointment appointment1 = new DentalAppointment(1, 1, 2, new Long("1564779965"), new Long("1564783565"));
        DentalAppointment appointment2 = new DentalAppointment(1, 1, 2, new Long("1564779965"), new Long("1564783565"));
        assertEquals(true, validator.isConflictingAppointment(appointment1, appointment2));

    }

    @Test
    public void conflicting_appointment_ERROR() {
        DentalAppointment appointment1 = new DentalAppointment(1, 1, 2, new Long("1564783565"), new Long("1564787165"));
        //Date overlaps with previous appointment
        DentalAppointment appointment2 = new DentalAppointment(1, 1, 2, new Long("1564784765"), new Long("1564788365"));
        assertEquals(false, validator.isConflictingAppointment(appointment1, appointment2));

    }

    @Test
    public void validate_date_interval_OK() {
        assertEquals(true, validator.validateDateInterval(new Long("1564847524000"), new Long("1564851124000")));
    }

    @Test
    public void validate_date_interval_ERROR() {
        assertEquals(false, validator.validateDateInterval(new Long("1564851124000"), new Long("1564847524000")));
    }

    @Test
    public void validate_future_time_OK() {
        assertEquals(true, validator.isValidFutureTimeStamp(new Timestamp(new Long("1564851124000"))));
    }

    @Test
    public void validate_future_time_ERROR() {
        assertEquals(false, validator.isValidFutureTimeStamp(new Timestamp(new Long("1556902324000"))));
    }

    @Test
    public void validate_time_interval_OK() {
        assertEquals(true, validator.isValidTimeDuration(new Timestamp(new Long("1588524724000")),new Timestamp(new Long("1588528324000")),30));
    }

    @Test
    public void validate_time_interval_ERROR() {
        assertEquals(false, validator.isValidTimeDuration(new Timestamp(new Long("1588528324000")),new Timestamp(new Long("1588524724000")),30));
    }

}
