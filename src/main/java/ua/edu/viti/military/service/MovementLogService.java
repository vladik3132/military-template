package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.MovementLogCreateRequest;
import ua.edu.viti.military.dto.response.MovementLogResponse;
import ua.edu.viti.military.entity.Driver;
import ua.edu.viti.military.entity.MovementLog;
import ua.edu.viti.military.entity.Vehicle;
import ua.edu.viti.military.event.*;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.DriverRepository;
import ua.edu.viti.military.repository.MovementLogRepository;
import ua.edu.viti.military.repository.VehicleRepository;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MovementLogService {
    
    private final MovementLogRepository movementLogRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final MetricsService metricsService;
    
    /**
     * Записати операцію з транспортом
     * @Transactional з REPEATABLE_READ для запобігання phantom reads
     */
    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        rollbackFor = Exception.class
    )
    public MovementLogResponse recordMovement(MovementLogCreateRequest dto) {
        log.info("Recording movement: vehicleId={}, type={}", 
            dto.getVehicleId(), dto.getMovementType());
        
        // 1. Знайти транспорт
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
            .orElseThrow(() -> new ResourceNotFoundException("Транспорт не знайдено"));
        
        // 2. Знайти водія (якщо вказано)
        Driver driver = null;
        if (dto.getDriverId() != null) {
            driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Водій не знайдено"));
        }
        
        // 3. Створити запис в журналі
        MovementLog log = new MovementLog();
        log.setVehicle(vehicle);
        log.setDriver(driver);
        log.setMovementType(dto.getMovementType());
        log.setDestination(dto.getDestination());
        log.setNotes(dto.getNotes());
        log.setPerformedBy(getCurrentUser());
        
        // 4. Зберегти
        MovementLog saved = movementLogRepository.save(log);
        
        // 5. Публікувати подію на основі типу руху
        publishMovementEvent(saved);
        metricsService.recordMovementLogged(dto.getMovementType().name());
        
        return toResponseDTO(saved);
    }
    
    /**
     * Публікує відповідну подію на основі типу руху
     */
    private void publishMovementEvent(MovementLog movement) {
        String performedBy = movement.getPerformedBy();
        Long vehicleId = movement.getVehicle().getId();
        String registrationNumber = movement.getVehicle().getRegistrationNumber();
        
        switch(movement.getMovementType()) {
            case ASSIGNMENT -> {
                if (movement.getDriver() != null) {
                    eventPublisher.publishEvent(new DriverAssignedEvent(
                        this,
                        movement.getDriver().getId(),
                        movement.getDriver().getFirstName() + " " + movement.getDriver().getLastName(),
                        vehicleId,
                        registrationNumber,
                        performedBy
                    ));
                }
            }
            case RETURN -> {
                eventPublisher.publishEvent(new VehicleReturnedEvent(
                    this,
                    vehicleId,
                    registrationNumber,
                    movement.getDriver() != null ? movement.getDriver().getId() : null,
                    movement.getDestination(),
                    performedBy
                ));
            }
            case MAINTENANCE -> {
                eventPublisher.publishEvent(new MaintenanceScheduledEvent(
                    this,
                    vehicleId,
                    registrationNumber,
                    movement.getNotes() != null ? movement.getNotes() : "Планове обслуговування",
                    performedBy
                ));
            }
            case ACCIDENT -> {
                eventPublisher.publishEvent(new AccidentReportedEvent(
                    this,
                    vehicleId,
                    registrationNumber,
                    movement.getDriver() != null ? movement.getDriver().getId() : null,
                    movement.getNotes() != null ? movement.getNotes() : "Аварія",
                    performedBy
                ));
            }
            default -> {}
        }
    }
    
    /**
     * Отримати історію операцій для транспорту
     */
    public List<MovementLogResponse> getVehicleHistory(Long vehicleId) {
        log.info("Fetching history for vehicle: {}", vehicleId);
        
        return movementLogRepository.findByVehicleIdOrderByPerformedAtDesc(vehicleId)
            .stream()
            .map(this::toResponseDTO)
            .toList();
    }
    
    /**
     * Отримати історію операцій для водія
     */
    public List<MovementLogResponse> getDriverHistory(Long driverId) {
        log.info("Fetching history for driver: {}", driverId);
        
        return movementLogRepository.findByDriverIdOrderByPerformedAtDesc(driverId)
            .stream()
            .map(this::toResponseDTO)
            .toList();
    }
    
    /**
     * Статистика: кількість операцій за період
     */
    public Long getMovementStatistics(String type, LocalDateTime start, LocalDateTime end) {
        log.info("Calculating statistics for type: {}, period: {} to {}", type, start, end);
        
        try {
            return movementLogRepository.countByTypeAndPeriod(
                ua.edu.viti.military.entity.MovementType.valueOf(type),
                start,
                end
            );
        } catch (IllegalArgumentException e) {
            log.warn("Invalid movement type: {}", type);
            return 0L;
        }
    }
    
    // ======== Helper методи ========
    
    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        
        return "system";
    }
    
    private MovementLogResponse toResponseDTO(MovementLog log) {
        MovementLogResponse dto = new MovementLogResponse();
        dto.setId(log.getId());
        dto.setVehicleId(log.getVehicle().getId());
        dto.setVehicleModel(log.getVehicle().getModel());
        dto.setRegistrationNumber(log.getVehicle().getRegistrationNumber());
        
        if (log.getDriver() != null) {
            dto.setDriverId(log.getDriver().getId());
            dto.setDriverFullName(log.getDriver().getFirstName() + " " + log.getDriver().getLastName());
        }
        
        dto.setMovementType(log.getMovementType());
        dto.setDestination(log.getDestination());
        dto.setNotes(log.getNotes());
        dto.setPerformedBy(log.getPerformedBy());
        dto.setPerformedAt(log.getPerformedAt());
        
        return dto;
    }
}
