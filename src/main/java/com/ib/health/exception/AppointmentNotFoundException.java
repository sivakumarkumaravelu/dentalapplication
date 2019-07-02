package com.ib.health.exception;

public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(int id) {
        super("Appointment id not found : " + id);
    }

}
