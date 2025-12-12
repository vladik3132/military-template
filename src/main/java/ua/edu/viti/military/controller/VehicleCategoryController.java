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
import ua.edu.viti.military.dto.request.VehicleCategoryCreateRequest;
import ua.edu.viti.military.dto.request.VehicleCategoryUpdateRequest;
import ua.edu.viti.military.dto.response.VehicleCategoryResponse;
import ua.edu.viti.military.service.VehicleCategoryService;
import ua.edu.viti.military.validation.OnCreate;
import ua.edu.viti.military.validation.OnUpdate;

@RestController
@RequestMapping("/api/vehicle-categories")
@RequiredArgsConstructor
@Tag(
        name = "Vehicle Categories",
        description = "API для управління категоріями militar транспорту (легкова, вантажна, спецтехніка)"
)
public class VehicleCategoryController {

    private final VehicleCategoryService vehicleCategoryService;

    @PostMapping
    @Operation(
            summary = "Створити нову категорію транспорту",
            description = """
                    Створює нову категорію транспорту в системі.
                    
                    **Бізнес-правила:**
                    - Назва категорії обов'язкова
                    - Опис обов'язковий
                    - Максимальна вантажопідйомність має бути > 0 кг
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Категорія успішно створена",
                    content = @Content(schema = @Schema(implementation = VehicleCategoryResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Помилка валідації вхідних даних"
            )
    })
    public ResponseEntity<VehicleCategoryResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Дані для створення нової категорії",
                    required = true
            )
            @Validated(OnCreate.class) @RequestBody VehicleCategoryCreateRequest request) {

        VehicleCategoryResponse created = vehicleCategoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Отримати категорію за ID",
            description = "Повертає детальну інформацію про категорію транспорту"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Категорія знайдена",
                    content = @Content(schema = @Schema(implementation = VehicleCategoryResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категорія не знайдена"
            )
    })
    public ResponseEntity<VehicleCategoryResponse> getById(
            @Parameter(description = "ID категорії", required = true, example = "1")
            @PathVariable Long id) {

        VehicleCategoryResponse category = vehicleCategoryService.getById(id);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Оновити категорію транспорту",
            description = "Оновлює інформацію про категорію. Всі поля опційні (partial update)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Категорія успішно оновлена",
                    content = @Content(schema = @Schema(implementation = VehicleCategoryResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категорія не знайдена"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Помилка валідації"
            )
    })
    public ResponseEntity<VehicleCategoryResponse> update(
            @Parameter(description = "ID категорії", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Дані для оновлення категорії"
            )
            @Validated(OnUpdate.class) @RequestBody VehicleCategoryUpdateRequest request) {

        VehicleCategoryResponse updated = vehicleCategoryService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Видалити категорію",
            description = "Видаляє категорію з системи. Не можна видалити якщо вона використовується"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Категорія успішно видалена"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категорія не знайдена"
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID категорії", required = true, example = "1")
            @PathVariable Long id) {

        vehicleCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
