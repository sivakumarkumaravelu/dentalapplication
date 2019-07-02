package com.ib.health.validator;


import com.ib.health.bean.DentalAppointment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DentalAppointmentValidatorTest {

    @MockBean
    DentalAppointmentValidator validator;


    @Test
    public void find_dentistAppointmentId_OK() {
        DentalAppointment appointment = new DentalAppointment(1, 1, 2, new Long("1562576400000"), new Long("1562578260000"));
        ConcurrentHashMap<Integer, DentalAppointment> map = new ConcurrentHashMap<>();
        map.put(1, appointment);
        assertEquals(true, validator.isValidDentistAppointment(appointment,null));

    }


}
