package ua.edu.viti.military.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.viti.military.entity.MovementType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovementLogResponse {
    
    private Long id;
    private Long vehicleId;
    private String vehicleModel;
    private String registrationNumber;
    private Long driverId;
    private String driverFullName;
    private MovementType movementType;
    private String destination;
    private String notes;
    private String performedBy;
    private LocalDateTime performedAt;
}
