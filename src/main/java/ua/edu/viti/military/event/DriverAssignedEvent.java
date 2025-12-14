package ua.edu.viti.military.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Подія, яка публікується коли водій призначений до транспортного засобу
 */
@Getter
public class DriverAssignedEvent extends ApplicationEvent {
    private final Long driverId;
    private final String driverName;
    private final Long vehicleId;
    private final String registrationNumber;
    private final String performedBy;

    public DriverAssignedEvent(Object source, Long driverId, String driverName, 
                              Long vehicleId, String registrationNumber, String performedBy) {
        super(source);
        this.driverId = driverId;
        this.driverName = driverName;
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.performedBy = performedBy;
    }
}
