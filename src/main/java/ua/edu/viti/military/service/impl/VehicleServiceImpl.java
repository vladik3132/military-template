package ua.edu.viti.military.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.VehicleCreateRequest;
import ua.edu.viti.military.dto.request.VehicleUpdateRequest;
import ua.edu.viti.military.dto.response.VehicleResponse;
import ua.edu.viti.military.entity.Driver;
import ua.edu.viti.military.entity.FuelType;
import ua.edu.viti.military.entity.Vehicle;
import ua.edu.viti.military.entity.VehicleCategory;
import ua.edu.viti.military.entity.VehicleStatus;
import ua.edu.viti.military.exception.DuplicateResourceException;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.DriverRepository;
import ua.edu.viti.military.repository.VehicleCategoryRepository;
import ua.edu.viti.military.repository.VehicleRepository;
import ua.edu.viti.military.service.VehicleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final DriverRepository driverRepository;

    @Override
    @Transactional
    public VehicleResponse create(VehicleCreateRequest request) {
        log.info("Creating vehicle with registration number: {}", request.getRegistrationNumber());

        if (vehicleRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new DuplicateResourceException(
                    "Транспорт з реєстраційним номером " + request.getRegistrationNumber() + " вже існує"
            );
        }

        VehicleCategory category = vehicleCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Категорія не знайдена"));

        Driver driver = null;
        if (request.getDriverId() != null) {
            driver = driverRepository.findById(request.getDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Водія не знайдено"));
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setModel(request.getModel());
        vehicle.setRegistrationNumber(request.getRegistrationNumber());
        vehicle.setCategory(category);
        vehicle.setEngineNumber(request.getEngineNumber());
        vehicle.setChassisNumber(request.getChassisNumber());
        vehicle.setManufactureYear(request.getManufactureYear());
        vehicle.setMileage(request.getMileage());
        vehicle.setFuelType(FuelType.valueOf(request.getFuelType()));
        vehicle.setFuelConsumption(request.getFuelConsumption());
        vehicle.setMaintenanceIntervalKm(request.getMaintenanceIntervalKm());
        vehicle.setLastMaintenanceMileage(request.getLastMaintenanceMileage());
        vehicle.setDriver(driver);
        vehicle.setStatus(VehicleStatus.OPERATIONAL);

        Vehicle saved = vehicleRepository.save(vehicle);
        return toResponse(saved);
    }

    @Override
    public VehicleResponse getById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Транспорт з ID " + id + " не знайдено"));
        return toResponse(vehicle);
    }

    @Override
    public List<VehicleResponse> getAll(VehicleStatus status, Long categoryId, Long driverId) {
        List<Vehicle> vehicles;

        if (status != null && categoryId != null) {
            vehicles = vehicleRepository.findByStatus(status).stream()
                    .filter(v -> v.getCategory() != null && categoryId.equals(v.getCategory().getId()))
                    .collect(Collectors.toList());
        } else if (status != null) {
            vehicles = vehicleRepository.findByStatus(status);
        } else if (categoryId != null) {
            vehicles = vehicleRepository.findByCategoryId(categoryId);
        } else if (driverId != null) {
            vehicles = vehicleRepository.findByDriverId(driverId);
        } else {
            vehicles = vehicleRepository.findAll();
        }

        return vehicles.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VehicleResponse update(Long id, VehicleUpdateRequest request) {
        log.info("Updating vehicle with ID {}", id);

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Транспорт не знайдено"));

        if (request.getModel() != null) {
            vehicle.setModel(request.getModel());
        }
        if (request.getRegistrationNumber() != null) {
            // Перевірка унікальності
            vehicleRepository.findByRegistrationNumber(request.getRegistrationNumber())
                    .filter(v -> !v.getId().equals(id))
                    .ifPresent(v -> {
                        throw new DuplicateResourceException(
                                "Транспорт з реєстраційним номером " + request.getRegistrationNumber() + " вже існує"
                        );
                    });
            vehicle.setRegistrationNumber(request.getRegistrationNumber());
        }
        if (request.getCategoryId() != null) {
            VehicleCategory category = vehicleCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Категорія не знайдена"));
            vehicle.setCategory(category);
        }
        if (request.getEngineNumber() != null) {
            vehicle.setEngineNumber(request.getEngineNumber());
        }
        if (request.getChassisNumber() != null) {
            vehicle.setChassisNumber(request.getChassisNumber());
        }
        if (request.getManufactureYear() != null) {
            vehicle.setManufactureYear(request.getManufactureYear());
        }
        if (request.getMileage() != null) {
            vehicle.setMileage(request.getMileage());
        }
        if (request.getFuelType() != null) {
            vehicle.setFuelType(FuelType.valueOf(request.getFuelType().toUpperCase()));
        }
        if (request.getFuelConsumption() != null) {
            vehicle.setFuelConsumption(request.getFuelConsumption());
        }
        if (request.getMaintenanceIntervalKm() != null) {
            vehicle.setMaintenanceIntervalKm(request.getMaintenanceIntervalKm());
        }
        if (request.getLastMaintenanceDate() != null) {
            vehicle.setLastMaintenanceDate(request.getLastMaintenanceDate());
        }
        if (request.getLastMaintenanceMileage() != null) {
            vehicle.setLastMaintenanceMileage(request.getLastMaintenanceMileage());
        }
        if (request.getDriverId() != null) {
            Driver driver = driverRepository.findById(request.getDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Водія не знайдено"));
            vehicle.setDriver(driver);
        }
        if (request.getStatus() != null) {
            vehicle.setStatus(VehicleStatus.valueOf(request.getStatus().toUpperCase()));
        }

        Vehicle updated = vehicleRepository.save(vehicle);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting vehicle with ID {}", id);

        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Транспорт з ID " + id + " не знайдено");
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    public List<VehicleResponse> findVehiclesRequiringMaintenance() {
        return vehicleRepository.findVehiclesRequiringMaintenance().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private VehicleResponse toResponse(Vehicle vehicle) {
        VehicleResponse dto = new VehicleResponse();
        dto.setId(vehicle.getId());
        dto.setModel(vehicle.getModel());
        dto.setRegistrationNumber(vehicle.getRegistrationNumber());

        if (vehicle.getCategory() != null) {
            dto.setCategoryId(vehicle.getCategory().getId());
            dto.setCategoryName(vehicle.getCategory().getName());
            dto.setCategoryCode(vehicle.getCategory().getCode());
        }

        dto.setEngineNumber(vehicle.getEngineNumber());
        dto.setChassisNumber(vehicle.getChassisNumber());
        dto.setManufactureYear(vehicle.getManufactureYear());
        dto.setMileage(vehicle.getMileage());
        dto.setFuelType(vehicle.getFuelType());
        dto.setFuelConsumption(vehicle.getFuelConsumption());
        dto.setMaintenanceIntervalKm(vehicle.getMaintenanceIntervalKm());
        dto.setLastMaintenanceDate(vehicle.getLastMaintenanceDate());
        dto.setLastMaintenanceMileage(vehicle.getLastMaintenanceMileage());

        if (vehicle.getDriver() != null) {
            dto.setDriverId(vehicle.getDriver().getId());
            String fullName = String.join(" ",
                    vehicle.getDriver().getLastName() != null ? vehicle.getDriver().getLastName() : "",
                    vehicle.getDriver().getFirstName() != null ? vehicle.getDriver().getFirstName() : ""
            ).trim();
            dto.setDriverFullName(fullName);
        }

        dto.setStatus(vehicle.getStatus());
        dto.setCreatedAt(vehicle.getCreatedAt());
        dto.setUpdatedAt(vehicle.getUpdatedAt());

        return dto;
    }
}
