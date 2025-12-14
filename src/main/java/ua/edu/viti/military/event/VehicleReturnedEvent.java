package ua.edu.viti.military.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Подія, яка публікується коли транспортний засіб повернутий та припиняє будь-яку операцію
 */
@Getter
public class VehicleReturnedEvent extends ApplicationEvent {
    private final Long vehicleId;
    private final String registrationNumber;
    private final Long driverId;
    private final String destination;
    private final String performedBy;

    public VehicleReturnedEvent(Object source, Long vehicleId, String registrationNumber,
                                Long driverId, String destination, String performedBy) {
        super(source);
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.driverId = driverId;
        this.destination = destination;
        this.performedBy = performedBy;
    }
}
