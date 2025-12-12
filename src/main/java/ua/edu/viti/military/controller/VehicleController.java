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
import ua.edu.viti.military.dto.request.VehicleCreateRequest;
import ua.edu.viti.military.dto.request.VehicleUpdateRequest;
import ua.edu.viti.military.dto.response.VehicleResponse;
import ua.edu.viti.military.entity.VehicleStatus;
import ua.edu.viti.military.service.VehicleService;
import ua.edu.viti.military.validation.OnCreate;
import ua.edu.viti.military.validation.OnUpdate;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(
        name = "Vehicles",
        description = "API для управління військовим транспортом. CRUD операції, фільтрація, контроль ТО"
)
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @Operation(
            summary = "Створити новий транспорт",
            description = """
                    Створює новий запис про транспорт в системі.
                    
                    **Бізнес-правила:**
                    - Реєстраційний номер має бути унікальним
                    - Категорія має існувати в системі
                    - Пробіг має бути > 0 км
                    - Тип палива обов'язковий
                    - Статус за замовчуванням: OPERATIONAL
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Транспорт успішно створен",
                    content = @Content(schema = @Schema(implementation = VehicleResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Помилка валідації"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категорія або водій не знайдені"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Транспорт з таким номером вже існує"
            )
    })
    public ResponseEntity<VehicleResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Дані для створення нового транспорту",
                    required = true
            )
            @Validated(OnCreate.class) @RequestBody VehicleCreateRequest request) {

        VehicleResponse created = vehicleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Отримати транспорт за ID",
            description = "Повертає детальну інформацію про транспорт"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Транспорт знайдено",
                    content = @Content(schema = @Schema(implementation = VehicleResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Транспорт не знайдено"
            )
    })
    public ResponseEntity<VehicleResponse> getById(
            @Parameter(description = "ID транспорту", required = true, example = "1")
            @PathVariable Long id) {

        VehicleResponse vehicle = vehicleService.getById(id);
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping
    @Operation(
            summary = "Отримати список всього транспорту",
            description = "Повертає список транспорту з можливістю фільтрації по статусу, категорії та водію"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список успішно отримано"
            )
    })
    public ResponseEntity<List<VehicleResponse>> getAll(
            @Parameter(description = "Фільтр по статусу (опційно)", example = "OPERATIONAL")
            @RequestParam(required = false) VehicleStatus status,
            
            @Parameter(description = "Фільтр по ID категорії (опційно)", example = "1")
            @RequestParam(required = false) Long categoryId,
            
            @Parameter(description = "Фільтр по ID водія (опційно)", example = "1")
            @RequestParam(required = false) Long driverId) {

        List<VehicleResponse> vehicles = vehicleService.getAll(status, categoryId, driverId);
        return ResponseEntity.ok(vehicles);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Оновити інформацію про транспорт",
            description = "Оновлює дані про транспорт. Всі поля опційні (partial update)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Транспорт успішно оновлено",
                    content = @Content(schema = @Schema(implementation = VehicleResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Транспорт не знайдено"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Помилка валідації"
            )
    })
    public ResponseEntity<VehicleResponse> update(
            @Parameter(description = "ID транспорту", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Дані для оновлення"
            )
            @Validated(OnUpdate.class) @RequestBody VehicleUpdateRequest request) {

        VehicleResponse updated = vehicleService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Видалити транспорт",
            description = "Видаляє запис про транспорт з системи"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Транспорт успішно видалено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Транспорт не знайдено"
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID транспорту", required = true, example = "1")
            @PathVariable Long id) {

        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/requiring-maintenance")
    @Operation(
            summary = "Транспорт що потребує технічного обслуговування",
            description = "Повертає список транспорту який перевищив інтервал ТО (пробіг >= ліміт)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список успішно отримано"
            )
    })
    public ResponseEntity<List<VehicleResponse>> getRequiringMaintenance() {
        List<VehicleResponse> vehicles = vehicleService.findVehiclesRequiringMaintenance();
        return ResponseEntity.ok(vehicles);
    }
}
