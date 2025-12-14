package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.MovementLog;
import ua.edu.viti.military.entity.MovementType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovementLogRepository extends JpaRepository<MovementLog, Long> {
    
    // Історія операцій для конкретного транспорту
    List<MovementLog> findByVehicleIdOrderByPerformedAtDesc(Long vehicleId);
    
    // Операції певного типу
    List<MovementLog> findByMovementType(MovementType movementType);
    
    // Операції за період
    List<MovementLog> findByPerformedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // Операції для конкретного водія
    List<MovementLog> findByDriverIdOrderByPerformedAtDesc(Long driverId);
    
    // Статистика: кількість операцій за період
    @Query("SELECT COUNT(m) FROM MovementLog m WHERE m.movementType = :type AND m.performedAt BETWEEN :start AND :end")
    Long countByTypeAndPeriod(MovementType type, LocalDateTime start, LocalDateTime end);
}
