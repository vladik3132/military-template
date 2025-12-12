package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ua.edu.viti.military.validation.OnUpdate;

import java.time.LocalDate;

@Data
public class DriverUpdateRequest {

    @Size(max = 50, groups = OnUpdate.class)
    private String militaryId;

    @Size(max = 100, groups = OnUpdate.class)
    private String firstName;

    @Size(max = 100, groups = OnUpdate.class)
    private String lastName;

    @Size(max = 100, groups = OnUpdate.class)
    private String middleName;

    @Size(max = 50, groups = OnUpdate.class)
    private String rank;

    @Size(max = 50, groups = OnUpdate.class)
    private String licenseNumber;

    private LocalDate licenseExpiryDate;

    @Size(max = 50, groups = OnUpdate.class)
    private String licenseCategories;

    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            groups = OnUpdate.class,
            message = "Телефон має містити 10-15 цифр (можна з +)"
    )
    private String phoneNumber;

    private Boolean isActive;
}
