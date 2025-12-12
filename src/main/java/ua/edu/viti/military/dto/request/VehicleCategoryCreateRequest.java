package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ua.edu.viti.military.validation.OnCreate;

@Data
public class VehicleCategoryCreateRequest {

    @NotBlank(groups = OnCreate.class, message = "Назва категорії обов'язкова при створенні")
    @Size(max = 100, groups = OnCreate.class)
    private String name;

    @NotBlank(groups = OnCreate.class, message = "Опис обов'язковий при створенні")
    @Size(max = 255, groups = OnCreate.class)
    private String description;

    @NotNull(groups = OnCreate.class, message = "Максимальна вантажопідйомність обов'язкова")
    @Positive(groups = OnCreate.class, message = "Вантажопідйомність має бути > 0")
    private Integer maxLoadKg;

    @NotNull(groups = OnCreate.class, message = "Кількість місць обов'язкова")
    @Positive(groups = OnCreate.class, message = "Кількість місць має бути > 0")
    private Integer seats;
}
