package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ua.edu.viti.military.validation.OnCreate;
import ua.edu.viti.military.validation.OnUpdate;

@Data
public class VehicleCategoryRequest {

    @NotBlank(message = "Назва категорії обов'язкова", groups = OnCreate.class)
    @Size(max = 100, groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(message = "Код категорії обов'язковий", groups = OnCreate.class)
    @Size(max = 20, groups = {OnCreate.class, OnUpdate.class})
    private String code;

    @Size(max = 500, groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @Size(max = 20, groups = {OnCreate.class, OnUpdate.class})
    private String requiredLicense;

    @Positive(groups = {OnCreate.class, OnUpdate.class})
    private Integer maxLoadCapacity;
}
