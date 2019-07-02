package com.ib.health.bean;

public class DentalAppointment {

    private int id;
    private int dentist_id;
    private int patient_id;
    private long startTime;
    private long endTime;


    public DentalAppointment(int id, int dentist_id, int patient_id, long startTime, long endTime) {
        this.id = id;
        this.dentist_id = dentist_id;
        this.patient_id = patient_id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DentalAppointment() {
    }

    public long getId() {
        return id;
    }

    public int getDentist_id() {
        return this.dentist_id;
    }

    public int getPatient_id() {
        return this.patient_id;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    @Override
    public int hashCode() {
        return id + dentist_id + Long.hashCode(startTime) + Long.hashCode(endTime);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DentalAppointment)) {
            return false;
        }
        DentalAppointment c = (DentalAppointment) o;
        return id == c.id && dentist_id == c.dentist_id && patient_id == c.patient_id && startTime == c.startTime && endTime == c.endTime;
    }

    @Override
    public String toString() {
        return "Id: '" + this.id + "', Dentist Id: '" + this.dentist_id + "', Patient id: '" + this.patient_id + "'";
    }

}
