package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.request.MovementLogCreateRequest;
import ua.edu.viti.military.dto.response.MovementLogResponse;
import ua.edu.viti.military.service.MovementLogService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Movement Logs", description = "API для журналу операцій з транспортом")
public class MovementLogController {
    
    private final MovementLogService movementLogService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Записати операцію з транспортом")
    public ResponseEntity<MovementLogResponse> recordMovement(
            @Valid @RequestBody MovementLogCreateRequest dto) {
        
        log.info("POST /api/movements - Recording new movement");
        MovementLogResponse response = movementLogService.recordMovement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/vehicle/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Історія операцій для транспорту")
    public ResponseEntity<List<MovementLogResponse>> getVehicleHistory(
            @PathVariable Long vehicleId) {
        
        log.info("GET /api/movements/vehicle/{} - Fetching vehicle history", vehicleId);
        List<MovementLogResponse> history = movementLogService.getVehicleHistory(vehicleId);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/driver/{driverId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Історія операцій для водія")
    public ResponseEntity<List<MovementLogResponse>> getDriverHistory(
            @PathVariable Long driverId) {
        
        log.info("GET /api/movements/driver/{} - Fetching driver history", driverId);
        List<MovementLogResponse> history = movementLogService.getDriverHistory(driverId);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Статистика операцій за період")
    public ResponseEntity<Long> getStatistics(
            @RequestParam String type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        log.info("GET /api/movements/statistics - type={}, period: {} to {}", type, start, end);
        Long count = movementLogService.getMovementStatistics(type, start, end);
        return ResponseEntity.ok(count);
    }
}
