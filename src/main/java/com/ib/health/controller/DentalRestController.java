package com.ib.health.controller;

import java.net.URI;
import com.ib.health.bean.DentalAppointment;
import com.ib.health.service.DentalAppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Rest controller that serves the requests.
 */

@RestController
@RequestMapping(path = "/dentalAppointments")
public class DentalRestController {

    private static final Logger logger = LoggerFactory.getLogger(DentalRestController.class);

    @Autowired
    DentalAppointmentService dentalAppointmentService;

    @GetMapping(path = "/{id}", produces = "application/json")
    public DentalAppointment getAppointment(@PathVariable int id) {
        logger.info("Request received to view the dental appointment id: " + id);
        return dentalAppointmentService.getDentalAppointmentById(id);
    }

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> addAppointment(@RequestBody DentalAppointment appointment) {
        logger.info("Request received to create dental appointment id: " + appointment.toString());
        DentalAppointment newAppointment = dentalAppointmentService.createDentalAppointment(appointment);
        if (newAppointment == null){
            logger.info("Unable to create dental appointment with details: " + appointment.toString());
            return ResponseEntity.noContent().build();
        }else{
            logger.info("Created the dental appointment: " + newAppointment.toString());
        }

        //Returns 201 response we don't need to send back created appointment but just send a URL. (Kind of HATEOS in action)
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(newAppointment.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
