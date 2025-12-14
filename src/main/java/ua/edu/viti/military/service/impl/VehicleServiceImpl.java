package ua.edu.viti.military.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import ua.edu.viti.military.mapper.VehicleMapper;
import ua.edu.viti.military.repository.DriverRepository;
import ua.edu.viti.military.repository.VehicleCategoryRepository;
import ua.edu.viti.military.repository.VehicleRepository;
import ua.edu.viti.military.service.MetricsService;
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
    private final VehicleMapper vehicleMapper;
    private final MetricsService metricsService;

    @Override
    @Transactional
    @CacheEvict(value = {"vehicles", "statistics"}, allEntries = true)
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

        Vehicle vehicle = vehicleMapper.toEntity(request);
        vehicle.setCategory(category);
        vehicle.setDriver(driver);
        vehicle.setStatus(VehicleStatus.OPERATIONAL);

        Vehicle saved = vehicleRepository.save(vehicle);
        metricsService.recordVehicleCreated();
        return vehicleMapper.toResponseDTO(saved);
    }

    @Override
    @Cacheable(value = "vehicles", key = "#id")
    public VehicleResponse getById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Транспорт з ID " + id + " не знайдено"));
        return vehicleMapper.toResponseDTO(vehicle);
    }

    @Override
    @Cacheable(value = "vehicles", key = "'all-' + #status + '-' + #categoryId + '-' + #driverId")
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

        return vehicleMapper.toResponseDTOList(vehicles);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"vehicles", "statistics"}, allEntries = true)
    public VehicleResponse update(Long id, VehicleUpdateRequest request) {
        log.info("Updating vehicle with ID {}", id);

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Транспорт не знайдено"));

        if (request.getRegistrationNumber() != null) {
            vehicleRepository.findByRegistrationNumber(request.getRegistrationNumber())
                    .filter(v -> !v.getId().equals(id))
                    .ifPresent(v -> {
                        throw new DuplicateResourceException(
                                "Транспорт з реєстраційним номером " + request.getRegistrationNumber() + " вже існує"
                        );
                    });
        }

        vehicleMapper.updateEntityFromDTO(request, vehicle);

        if (request.getCategoryId() != null) {
            VehicleCategory category = vehicleCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Категорія не знайдена"));
            vehicle.setCategory(category);
        }
        if (request.getDriverId() != null) {
            Driver driver = driverRepository.findById(request.getDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Водія не знайдено"));
            vehicle.setDriver(driver);
        }
        if (request.getFuelType() != null) {
            vehicle.setFuelType(FuelType.valueOf(request.getFuelType().toUpperCase()));
        }
        if (request.getStatus() != null) {
            vehicle.setStatus(VehicleStatus.valueOf(request.getStatus().toUpperCase()));
        }

        Vehicle updated = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"vehicles", "statistics"}, allEntries = true)
    public void delete(Long id) {
        log.info("Deleting vehicle with ID {}", id);

        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Транспорт з ID " + id + " не знайдено");
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    public List<VehicleResponse> findVehiclesRequiringMaintenance() {
        return vehicleMapper.toResponseDTOList(vehicleRepository.findVehiclesRequiringMaintenance());
    }
}
