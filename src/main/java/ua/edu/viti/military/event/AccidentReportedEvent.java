package ua.edu.viti.military.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Подія, яка публікується коли відбувся інцидент/аварія з транспортом
 */
@Getter
public class AccidentReportedEvent extends ApplicationEvent {
    private final Long vehicleId;
    private final String registrationNumber;
    private final Long driverId;
    private final String description;
    private final String performedBy;

    public AccidentReportedEvent(Object source, Long vehicleId, String registrationNumber,
                                 Long driverId, String description, String performedBy) {
        super(source);
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.driverId = driverId;
        this.description = description;
        this.performedBy = performedBy;
    }
}
