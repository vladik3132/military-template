package ua.edu.viti.military.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String model;

    @Column(name = "registration_number", nullable = false, unique = true, length = 20)
    private String registrationNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private VehicleCategory category;

    @Column(name = "engine_number", unique = true, length = 50)
    private String engineNumber;

    @Column(name = "chassis_number", unique = true, length = 50)
    private String chassisNumber;

    @Column(name = "manufacture_year")
    private Integer manufactureYear;

    @Column(nullable = false)
    private Integer mileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false, length = 20)
    private FuelType fuelType;

    @Column(name = "fuel_consumption")
    private Double fuelConsumption;

    @Column(name = "maintenance_interval_km")
    private Integer maintenanceIntervalKm;

    @Column(name = "last_maintenance_date")
    private LocalDate lastMaintenanceDate;

    @Column(name = "last_maintenance_mileage")
    private Integer lastMaintenanceMileage;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehicleStatus status = VehicleStatus.OPERATIONAL;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Допоміжні методи для сервісу

    public boolean isMaintenanceOverdue() {
        if (maintenanceIntervalKm == null || lastMaintenanceMileage == null || mileage == null) {
            return false;
        }
        return mileage - lastMaintenanceMileage >= maintenanceIntervalKm;
    }

    public boolean isActive() {
        return VehicleStatus.OPERATIONAL.equals(status);
    }

    public void setActive(Boolean active) {
        if (Boolean.TRUE.equals(active)) {
            this.status = VehicleStatus.OPERATIONAL;
        } else {
            this.status = VehicleStatus.OUT_OF_SERVICE;
        }
    }
}
