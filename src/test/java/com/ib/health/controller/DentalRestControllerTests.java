package com.ib.health.controller;

import com.ib.health.bean.DentalAppointment;
import com.ib.health.service.DentalAppointmentService;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DentalRestControllerTests {

    @MockBean
    private DentalAppointmentService mockService;

    @Autowired
    private TestRestTemplate restTemplate;


    @Before
    public void init() {
        DentalAppointment appointment = new DentalAppointment(1, 1, 2, new Long("1562576400000"), new Long("1562578260000"));
        when(mockService.getDentalAppointmentById(1)).thenReturn(appointment);
    }

    @Test
    public void find_dentistAppointmentId_OK() throws JSONException {
        String expected = "{\n" +
                "  \"startTime\": 1562576400000,\n" +
                "  \"endTime\": 1562578260000,\n" +
                "  \"dentist_id\": 1,\n" +
                "  \"patient_id\": 2\n" +
                "}";
        ResponseEntity<String> response = restTemplate.getForEntity("/dentalAppointments/1", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        JSONAssert.assertEquals(expected, response.getBody(), false);
        verify(mockService, times(1)).getDentalAppointmentById(1);
    }

    @Test
    public void save_dentalAppointment_OK() {
        DentalAppointment appointment = new DentalAppointment(1, 1, 2, new Long("1562576400000"), new Long("1562578260000"));
        when(mockService.createDentalAppointment(any(DentalAppointment.class))).thenReturn(appointment);
        ResponseEntity<String> response = restTemplate.postForEntity("/dentalAppointments", appointment, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(mockService, times(1)).createDentalAppointment(any(DentalAppointment.class));
    }
}
