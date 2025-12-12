package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ua.edu.viti.military.validation.FutureDate;
import ua.edu.viti.military.validation.OnCreate;

import java.time.LocalDate;

@Data
public class VehicleCreateRequest {

    @NotBlank(groups = OnCreate.class, message = "Модель обов'язкова при створенні")
    @Size(max = 100, groups = OnCreate.class)
    private String model;

    @NotNull(groups = OnCreate.class, message = "Рік виготовлення обов'язковий при створенні")
    @Positive(groups = OnCreate.class, message = "Рік має бути > 0")
    private Integer manufactureYear;

    @NotBlank(groups = OnCreate.class, message = "Номер (держ/інв) обов'язковий при створенні")
    @Size(max = 50, groups = OnCreate.class)
    private String registrationNumber;

    @NotBlank(groups = OnCreate.class, message = "Номер двигуна обов'язковий при створенні")
    @Size(max = 50, groups = OnCreate.class)
    private String engineNumber;

    @NotBlank(groups = OnCreate.class, message = "Номер шасі обов'язковий при створенні")
    @Size(max = 50, groups = OnCreate.class)
    private String chassisNumber;

    @NotNull(groups = OnCreate.class, message = "Пробіг обов'язковий при створенні")
    @Positive(groups = OnCreate.class, message = "Пробіг має бути > 0")
    private Integer mileage;

    // DTO як String, в Entity швидше за все enum FuelType -> конвертуємо в сервісі
    @NotBlank(groups = OnCreate.class, message = "Тип палива обов'язковий при створенні")
    @Size(max = 30, groups = OnCreate.class)
    private String fuelType;

    @NotNull(groups = OnCreate.class, message = "Витрата палива обов'язкова при створенні")
    @Positive(groups = OnCreate.class, message = "Витрата палива має бути > 0")
    private Double fuelConsumption;

    @NotNull(groups = OnCreate.class, message = "Інтервал ТО (км) обов'язковий при створенні")
    @Positive(groups = OnCreate.class, message = "Інтервал ТО має бути > 0")
    private Integer maintenanceIntervalKm;

    @NotNull(groups = OnCreate.class, message = "Пробіг останнього ТО обов'язковий")
    @Positive(groups = OnCreate.class, message = "Пробіг останнього ТО має бути > 0")
    private Integer lastMaintenanceMileage;

    @NotNull(groups = OnCreate.class, message = "Дата останнього ТО обов'язкова")
    private LocalDate lastMaintenanceDate;

    @FutureDate(groups = OnCreate.class, message = "Дата наступного ТО має бути в майбутньому")
    private LocalDate nextMaintenanceDate;

    @NotNull(groups = OnCreate.class, message = "Категорія авто обов'язкова")
    private Long categoryId;

    // зв’язок з водієм, якщо в тебе так задумано
    private Long driverId;

    // якщо в тебе Vehicle має status (enum/string)
    @Size(max = 30, groups = OnCreate.class)
    private String status;
}
