package ua.edu.viti.military.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.DriverCreateRequest;
import ua.edu.viti.military.dto.request.DriverUpdateRequest;
import ua.edu.viti.military.dto.response.DriverResponse;
import ua.edu.viti.military.entity.Driver;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.DriverRepository;
import ua.edu.viti.military.service.DriverService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Override
    @Transactional
    public DriverResponse create(DriverCreateRequest request) {
        log.info("Creating driver with militaryId {}", request.getMilitaryId());

        Driver driver = new Driver();
        driver.setMilitaryId(request.getMilitaryId());
        driver.setFirstName(request.getFirstName());
        driver.setLastName(request.getLastName());
        driver.setMiddleName(request.getMiddleName());
        driver.setRank(request.getRank());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setLicenseCategories(request.getLicenseCategories());
        driver.setLicenseExpiryDate(request.getLicenseExpiryDate());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setIsActive(Boolean.TRUE);

        Driver saved = driverRepository.save(driver);
        return toResponse(saved);
    }

    @Override
    public DriverResponse getById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Водія не знайдено"));
        return toResponse(driver);
    }

    @Override
    public List<DriverResponse> getAll(Boolean isActive) {
        List<Driver> drivers;
        if (isActive != null) {
            drivers = driverRepository.findByIsActive(isActive);
        } else {
            drivers = driverRepository.findAll();
        }
        return drivers.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DriverResponse update(Long id, DriverUpdateRequest request) {
        log.info("Updating driver with ID {}", id);

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Водія не знайдено"));

        if (request.getFirstName() != null) {
            driver.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            driver.setLastName(request.getLastName());
        }
        if (request.getMiddleName() != null) {
            driver.setMiddleName(request.getMiddleName());
        }
        if (request.getRank() != null) {
            driver.setRank(request.getRank());
        }
        if (request.getLicenseNumber() != null) {
            driver.setLicenseNumber(request.getLicenseNumber());
        }
        if (request.getLicenseCategories() != null) {
            driver.setLicenseCategories(request.getLicenseCategories());
        }
        if (request.getLicenseExpiryDate() != null) {
            driver.setLicenseExpiryDate(request.getLicenseExpiryDate());
        }
        if (request.getPhoneNumber() != null) {
            driver.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getIsActive() != null) {
            driver.setIsActive(request.getIsActive());
        }

        Driver updated = driverRepository.save(driver);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting driver with ID {}", id);

        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Водія не знайдено");
        }
        driverRepository.deleteById(id);
    }

    private DriverResponse toResponse(Driver driver) {
        DriverResponse dto = new DriverResponse();
        dto.setId(driver.getId());
        dto.setMilitaryId(driver.getMilitaryId());
        dto.setFirstName(driver.getFirstName());
        dto.setLastName(driver.getLastName());
        dto.setMiddleName(driver.getMiddleName());
        dto.setRank(driver.getRank());
        dto.setLicenseNumber(driver.getLicenseNumber());
        dto.setLicenseCategories(driver.getLicenseCategories());
        dto.setLicenseExpiryDate(driver.getLicenseExpiryDate());
        dto.setPhoneNumber(driver.getPhoneNumber());
        dto.setIsActive(driver.getIsActive());
        dto.setCreatedAt(driver.getCreatedAt());
        dto.setUpdatedAt(driver.getUpdatedAt());
        return dto;
    }
}
