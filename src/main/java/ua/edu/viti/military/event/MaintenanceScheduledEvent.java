package ua.edu.viti.military.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Подія, яка публікується коли транспортний засіб потребує технічного обслуговування
 */
@Getter
public class MaintenanceScheduledEvent extends ApplicationEvent {
    private final Long vehicleId;
    private final String registrationNumber;
    private final String maintenanceType;
    private final String performedBy;

    public MaintenanceScheduledEvent(Object source, Long vehicleId, String registrationNumber,
                                     String maintenanceType, String performedBy) {
        super(source);
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.maintenanceType = maintenanceType;
        this.performedBy = performedBy;
    }
}
