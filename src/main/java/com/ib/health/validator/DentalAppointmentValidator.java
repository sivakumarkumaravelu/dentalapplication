package com.ib.health.validator;

import com.ib.health.bean.DentalAppointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Dental appointment validator class helps in performing all the validations before creating a dental appointment
 */
@Component
public class DentalAppointmentValidator {
    private static final Logger logger = LoggerFactory.getLogger(DentalAppointmentValidator.class);

    public static final int duration = 30;

    /**
     * Validates the date interval by accepting start and end time
     * @param startTime
     * @param endTime
     * @return
     */
    public boolean validateDateInterval(long startTime, long endTime) {
        if (startTime <= 0 || endTime <= 0)
            return false;
        Timestamp fromTs = new Timestamp(startTime);
        Timestamp toTs = new Timestamp(endTime);
        if (isValidFutureTimeStamp(fromTs) && isValidFutureTimeStamp(toTs) && isValidTimeDuration(fromTs, toTs, duration)) {
            logger.debug("DentalAppointmentValidator.validateDateInterval(): Valid date interval");
            return true;
        }
        return false;
    }

    /**
     * This method checks if the timestamp provided is a valid future time stamp.
     * @param time
     * @return
     */
    public boolean isValidFutureTimeStamp(Timestamp time) {
        boolean isValid = true;
        try {
            LocalDate today = LocalDate.now();
            LocalDate localDate = time.toLocalDateTime().toLocalDate();
            isValid = localDate.isAfter(today);
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * This method accepts the from timestamp, to timestamp and minimum duration minutes and returns if the time between those
     * time stamp is more than the limit.
     * @param fromTs
     * @param toTs
     * @param minutes
     * @return
     */
    public boolean isValidTimeDuration(Timestamp fromTs, Timestamp toTs, int minutes) {
        LocalDateTime fromLocalTime = fromTs.toLocalDateTime();
        LocalDateTime toLocalTime = toTs.toLocalDateTime();
        Duration duration = Duration.between(fromLocalTime, toLocalTime);
        return duration.toMinutes() >= minutes ? true : false;
    }

    /**
     * This method checks if two appointments are conflicting with each other.
     * @param reservedAppointment
     * @param newAppointMent
     * @return
     */
    public boolean isConflictingAppointment(DentalAppointment reservedAppointment, DentalAppointment newAppointMent) {
        Timestamp fromAppointment1 = new Timestamp(reservedAppointment.getStartTime());
        Timestamp toAppointment1 = new Timestamp(reservedAppointment.getEndTime());

        Timestamp fromAppointment2 = new Timestamp(newAppointMent.getStartTime());
        Timestamp toAppointment2 = new Timestamp(newAppointMent.getEndTime());

        if ((fromAppointment2.after(fromAppointment1) && fromAppointment2.before(toAppointment1)) || (toAppointment2.after(fromAppointment1) && toAppointment2.before(toAppointment1))) {
            logger.info("DentalAppointmentValidator.isConflictingAppointment(): Conflicting appointment found");
            return false;
        }
        return true;
    }


    /**
     * This appointment accepts an appointment and map of all appointments and checks if its a valid dental appointment.
     * TODO We search here by the values in the map and check each appointment, a potential O(n), database would guarantee a O(logn).
     * @param appointment
     * @param allAppointments
     * @return
     */
    public boolean isValidDentistAppointment(DentalAppointment appointment, ConcurrentHashMap<Integer, DentalAppointment> allAppointments) {
        if (allAppointments == null || allAppointments.isEmpty()) {
            logger.info("DentalAppointmentValidator.isValidDentistAppointment(): No new appointments yet, creating first appointment");
            return true;
        }
        List<DentalAppointment> conflictingAppointments = allAppointments.values()
                .stream()
                .filter(map -> appointment.getDentist_id() == map.getDentist_id())
                .filter(reservedAppointment -> isConflictingAppointment(reservedAppointment, appointment))
                .collect(Collectors.toList());
        return (conflictingAppointments != null && conflictingAppointments.size() > 0) ? false : true;
    }

}
