package ua.edu.viti.military.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.VehicleCategoryCreateRequest;
import ua.edu.viti.military.dto.request.VehicleCategoryUpdateRequest;
import ua.edu.viti.military.dto.response.VehicleCategoryResponse;
import ua.edu.viti.military.entity.VehicleCategory;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.mapper.VehicleCategoryMapper;
import ua.edu.viti.military.repository.VehicleCategoryRepository;
import ua.edu.viti.military.service.VehicleCategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleCategoryServiceImpl implements VehicleCategoryService {

    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final VehicleCategoryMapper vehicleCategoryMapper;

    @Override
    @Transactional
    @CacheEvict(value = "vehicleCategories", allEntries = true)
    public VehicleCategoryResponse create(VehicleCategoryCreateRequest request) {
        log.info("Creating vehicle category: {}", request.getName());
        
        VehicleCategory category = vehicleCategoryMapper.toEntity(request);

        VehicleCategory saved = vehicleCategoryRepository.save(category);
        log.info("Vehicle category created with ID: {}", saved.getId());
        
        return vehicleCategoryMapper.toResponseDTO(saved);
    }

    @Override
    @Cacheable(value = "vehicleCategories", key = "#id")
    public VehicleCategoryResponse getById(Long id) {
        log.debug("Fetching vehicle category with ID: {}", id);
        
        VehicleCategory category = vehicleCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VehicleCategory not found: " + id));
        
        return vehicleCategoryMapper.toResponseDTO(category);
    }

    @Override
    @Cacheable(value = "vehicleCategories", key = "'all'")
    public List<VehicleCategoryResponse> getAll() {
        log.debug("Fetching all vehicle categories");
        
        return vehicleCategoryRepository.findAll()
                .stream()
                .map(vehicleCategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = "vehicleCategories", allEntries = true)
    public VehicleCategoryResponse update(Long id, VehicleCategoryUpdateRequest request) {
        log.info("Updating vehicle category with ID: {}", id);
        
        VehicleCategory category = vehicleCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VehicleCategory not found: " + id));

        vehicleCategoryMapper.updateEntityFromDTO(request, category);

        VehicleCategory saved = vehicleCategoryRepository.save(category);
        log.info("Vehicle category with ID {} updated successfully", id);
        
        return vehicleCategoryMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "vehicleCategories", allEntries = true)
    public void delete(Long id) {
        log.info("Deleting vehicle category with ID: {}", id);
        
        if (!vehicleCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("VehicleCategory not found: " + id);
        }
        
        vehicleCategoryRepository.deleteById(id);
        log.info("Vehicle category with ID {} deleted successfully", id);
    }
}
