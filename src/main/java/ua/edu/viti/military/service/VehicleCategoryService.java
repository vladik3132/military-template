package ua.edu.viti.military.service;

import ua.edu.viti.military.dto.request.VehicleCategoryCreateRequest;
import ua.edu.viti.military.dto.request.VehicleCategoryUpdateRequest;
import ua.edu.viti.military.dto.response.VehicleCategoryResponse;

import java.util.List;

public interface VehicleCategoryService {

    VehicleCategoryResponse create(VehicleCategoryCreateRequest request);

    VehicleCategoryResponse getById(Long id);

    List<VehicleCategoryResponse> getAll();

    VehicleCategoryResponse update(Long id, VehicleCategoryUpdateRequest request);

    void delete(Long id);
}
