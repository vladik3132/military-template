package ua.edu.viti.military.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.DriverCreateRequest;
import ua.edu.viti.military.dto.request.DriverUpdateRequest;
import ua.edu.viti.military.dto.response.DriverResponse;
import ua.edu.viti.military.entity.Driver;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.mapper.DriverMapper;
import ua.edu.viti.military.repository.DriverRepository;
import ua.edu.viti.military.service.MetricsService;
import ua.edu.viti.military.service.DriverService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final MetricsService metricsService;

    @Override
    @Transactional
    @CacheEvict(value = "drivers", allEntries = true)
    public DriverResponse create(DriverCreateRequest request) {
        log.info("Creating driver with militaryId {}", request.getMilitaryId());

        Driver driver = driverMapper.toEntity(request);
        driver.setIsActive(Boolean.TRUE);

        Driver saved = driverRepository.save(driver);
        metricsService.recordDriverCreated();
        return driverMapper.toResponseDTO(saved);
    }

    @Override
    @Cacheable(value = "drivers", key = "#id")
    public DriverResponse getById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Водія не знайдено"));
        return driverMapper.toResponseDTO(driver);
    }

    @Override
    @Cacheable(value = "drivers", key = "'all-' + #isActive")
    public List<DriverResponse> getAll(Boolean isActive) {
        List<Driver> drivers;
        if (isActive != null) {
            drivers = driverRepository.findByIsActive(isActive);
        } else {
            drivers = driverRepository.findAll();
        }
        return driverMapper.toResponseDTOList(drivers);
    }

    @Override
    @Transactional
    @CacheEvict(value = "drivers", allEntries = true)
    public DriverResponse update(Long id, DriverUpdateRequest request) {
        log.info("Updating driver with ID {}", id);

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Водія не знайдено"));

        driverMapper.updateEntityFromDTO(request, driver);

        Driver updated = driverRepository.save(driver);
        return driverMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = "drivers", allEntries = true)
    public void delete(Long id) {
        log.info("Deleting driver with ID {}", id);

        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Водія не знайдено");
        }
        driverRepository.deleteById(id);
    }
}
