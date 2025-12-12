package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ua.edu.viti.military.validation.OnUpdate;

@Data
public class VehicleCategoryUpdateRequest {

    @Size(max = 100, groups = OnUpdate.class)
    private String name;

    @Size(max = 255, groups = OnUpdate.class)
    private String description;

    @Positive(groups = OnUpdate.class, message = "Вантажопідйомність має бути > 0")
    private Integer maxLoadKg;

    @Positive(groups = OnUpdate.class, message = "Кількість місць має бути > 0")
    private Integer seats;
}
