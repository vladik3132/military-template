package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.request.DriverCreateRequest;
import ua.edu.viti.military.dto.request.DriverUpdateRequest;
import ua.edu.viti.military.dto.response.DriverResponse;
import ua.edu.viti.military.service.DriverService;
import ua.edu.viti.military.validation.OnCreate;
import ua.edu.viti.military.validation.OnUpdate;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Tag(
        name = "Drivers",
        description = "API для управління водіями військового транспорту"
)
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    @Operation(
            summary = "Створити нового водія",
            description = """
                    Створює новий запис про водія в системі.
                    
                    **Бізнес-правила:**
                    - Військовий ID має бути унікальним
                    - Номер ліцензії має бути унікальним
                    - Статус за замовчуванням: активний (isActive = true)
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Водій успішно створено",
                    content = @Content(schema = @Schema(implementation = DriverResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Помилка валідації"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Водій з таким ID або номером ліцензії вже існує"
            )
    })
    public ResponseEntity<DriverResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Дані для створення нового водія",
                    required = true
            )
            @Validated(OnCreate.class) @RequestBody DriverCreateRequest request) {

        DriverResponse created = driverService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Отримати водія за ID",
            description = "Повертає детальну інформацію про водія"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Водій знайдено",
                    content = @Content(schema = @Schema(implementation = DriverResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Водій не знайдено"
            )
    })
    public ResponseEntity<DriverResponse> getById(
            @Parameter(description = "ID водія", required = true, example = "1")
            @PathVariable Long id) {

        DriverResponse driver = driverService.getById(id);
        return ResponseEntity.ok(driver);
    }

    @GetMapping
    @Operation(
            summary = "Отримати список всіх водіїв",
            description = "Повертає список водіїв з можливістю фільтрації по статусу активності"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список успішно отримано"
            )
    })
    public ResponseEntity<List<DriverResponse>> getAll(
            @Parameter(description = "Фільтр по активності (опційно)", example = "true")
            @RequestParam(required = false) Boolean isActive) {

        List<DriverResponse> drivers = driverService.getAll(isActive);
        return ResponseEntity.ok(drivers);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Оновити інформацію про водія",
            description = "Оновлює дані про водія. Всі поля опційні (partial update)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Водій успішно оновлено",
                    content = @Content(schema = @Schema(implementation = DriverResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Водій не знайдено"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Помилка валідації"
            )
    })
    public ResponseEntity<DriverResponse> update(
            @Parameter(description = "ID водія", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Дані для оновлення"
            )
            @Validated(OnUpdate.class) @RequestBody DriverUpdateRequest request) {

        DriverResponse updated = driverService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Видалити водія",
            description = "Видаляє запис про водія з системи"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Водій успішно видалено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Водій не знайдено"
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID водія", required = true, example = "1")
            @PathVariable Long id) {

        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
