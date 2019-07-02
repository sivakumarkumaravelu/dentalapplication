package com.ib.health.controller;

import java.net.URI;

import com.ib.health.bean.DentalAppointment;
import com.ib.health.service.DentalAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/dentalAppointments")
public class DentalRestController {


    @Autowired
    DentalAppointmentService dentalAppointmentService;

    @GetMapping(path = "/{id}", produces = "application/json")
    public DentalAppointment getAppointment(@PathVariable int id) {
        return dentalAppointmentService.getDentalAppointmentById(id);
    }

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> addAppointment(@RequestBody DentalAppointment appointment) {
        DentalAppointment newAppointment = dentalAppointmentService.createDentalAppointment(appointment);
        if (newAppointment == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(newAppointment.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
