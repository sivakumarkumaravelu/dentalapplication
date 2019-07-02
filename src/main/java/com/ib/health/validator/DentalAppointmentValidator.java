package com.ib.health.validator;

import com.ib.health.bean.DentalAppointment;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class DentalAppointmentValidator {

    public boolean validateDateInterval(long startTime, long endTime) {
        if (startTime <= 0 || endTime <= 0)
            return false;
        Timestamp fromTs = new Timestamp(startTime);
        Timestamp toTs = new Timestamp(endTime);
        if (isValidFutureTimeStamp(fromTs) && isValidFutureTimeStamp(toTs) && isValidTimeDuration(fromTs, toTs, 30)) {
            return true;
        }
        return false;
    }

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

    public boolean isValidTimeDuration(Timestamp fromTs, Timestamp toTs, int minutes) {
        LocalDateTime fromLocalTime = fromTs.toLocalDateTime();
        LocalDateTime toLocalTime = toTs.toLocalDateTime();
        Duration duration = Duration.between(fromLocalTime, toLocalTime);
        return duration.toMinutes() >= minutes ? true : false;
    }

    public boolean isConflictingAppointment(DentalAppointment reservedAppointment, DentalAppointment newAppointMent) {
        Timestamp fromAppointment1 = new Timestamp(reservedAppointment.getStartTime());
        Timestamp toAppointment1 = new Timestamp(reservedAppointment.getEndTime());

        Timestamp fromAppointment2 = new Timestamp(newAppointMent.getStartTime());
        Timestamp toAppointment2 = new Timestamp(newAppointMent.getEndTime());

        if ((fromAppointment2.after(fromAppointment1) && fromAppointment2.before(toAppointment1)) || (toAppointment2.after(fromAppointment1) && toAppointment2.before(toAppointment1))) {
            return false;
        }
        return true;
    }


    public boolean isValidDentistAppointment(DentalAppointment appointment, ConcurrentHashMap<Integer, DentalAppointment> allAppointments) {
        if (allAppointments == null || allAppointments.isEmpty())
            return true;
        List<DentalAppointment> conflictingAppointments = allAppointments.values()
                .stream()
                .filter(map -> appointment.getDentist_id() == map.getDentist_id())
                .filter(reservedAppointment -> isConflictingAppointment(reservedAppointment, appointment))
                .collect(Collectors.toList());
        return (conflictingAppointments != null && conflictingAppointments.size() > 0) ? false : true;
    }

}
