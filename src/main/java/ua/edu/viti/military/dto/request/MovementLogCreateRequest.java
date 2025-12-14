package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import ua.edu.viti.military.entity.MovementType;

@Data
public class MovementLogCreateRequest {
    
    @NotNull(message = "ID транспорту обов'язковий")
    @Positive
    private Long vehicleId;
    
    @Positive
    private Long driverId;
    
    @NotNull(message = "Тип операції обов'язковий")
    private MovementType movementType;
    
    @Size(max = 200)
    private String destination;
    
    @Size(max = 500)
    private String notes;
}
