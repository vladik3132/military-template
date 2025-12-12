package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ua.edu.viti.military.validation.FutureDate;
import ua.edu.viti.military.validation.OnUpdate;

import java.time.LocalDate;

@Data
public class VehicleUpdateRequest {

    @Size(max = 100, groups = OnUpdate.class)
    private String model;

    @Positive(groups = OnUpdate.class, message = "Рік має бути > 0")
    private Integer manufactureYear;

    @Size(max = 50, groups = OnUpdate.class)
    private String registrationNumber;

    @Size(max = 50, groups = OnUpdate.class)
    private String engineNumber;

    @Size(max = 50, groups = OnUpdate.class)
    private String chassisNumber;

    @Positive(groups = OnUpdate.class, message = "Пробіг має бути > 0")
    private Integer mileage;

    @Size(max = 30, groups = OnUpdate.class)
    private String fuelType;

    @Positive(groups = OnUpdate.class, message = "Витрата палива має бути > 0")
    private Double fuelConsumption;

    @Positive(groups = OnUpdate.class, message = "Інтервал ТО має бути > 0")
    private Integer maintenanceIntervalKm;

    @Positive(groups = OnUpdate.class, message = "Пробіг останнього ТО має бути > 0")
    private Integer lastMaintenanceMileage;

    private LocalDate lastMaintenanceDate;

    @FutureDate(groups = OnUpdate.class, message = "Дата наступного ТО має бути в майбутньому")
    private LocalDate nextMaintenanceDate;

    private Long categoryId;

    private Long driverId;

    @Size(max = 30, groups = OnUpdate.class)
    private String status;
}
