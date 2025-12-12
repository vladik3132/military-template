package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ua.edu.viti.military.validation.OnCreate;

import java.time.LocalDate;

@Data
public class DriverCreateRequest {

    @NotBlank(groups = OnCreate.class, message = "Військовий ID обов'язковий при створенні")
    @Size(max = 50, groups = OnCreate.class)
    private String militaryId;

    @NotBlank(groups = OnCreate.class, message = "Ім'я обов'язкове при створенні")
    @Size(max = 100, groups = OnCreate.class)
    private String firstName;

    @NotBlank(groups = OnCreate.class, message = "Прізвище обов'язкове при створенні")
    @Size(max = 100, groups = OnCreate.class)
    private String lastName;

    @Size(max = 100, groups = OnCreate.class)
    private String middleName;

    @NotBlank(groups = OnCreate.class, message = "Військове звання обов'язкове при створенні")
    @Size(max = 50, groups = OnCreate.class)
    private String rank;

    @NotBlank(groups = OnCreate.class, message = "Номер посвідчення водія обов'язковий")
    @Size(max = 50, groups = OnCreate.class)
    private String licenseNumber;

    @NotNull(groups = OnCreate.class, message = "Дата закінчення дії посвідчення обов'язкова")
    private LocalDate licenseExpiryDate;

    @NotBlank(groups = OnCreate.class, message = "Категорії прав обов'язкові")
    @Size(max = 50, groups = OnCreate.class)
    private String licenseCategories;

    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            groups = OnCreate.class,
            message = "Телефон має містити 10-15 цифр (можна з +)"
    )
    private String phoneNumber;

    @NotNull(groups = OnCreate.class, message = "Статус active обов'язковий при створенні")
    private Boolean isActive;
}
