package ua.edu.viti.military.service;

import ua.edu.viti.military.dto.request.VehicleCreateRequest;
import ua.edu.viti.military.dto.request.VehicleUpdateRequest;
import ua.edu.viti.military.dto.response.VehicleResponse;
import ua.edu.viti.military.entity.VehicleStatus;

import java.util.List;

public interface VehicleService {

    VehicleResponse create(VehicleCreateRequest request);

    VehicleResponse update(Long id, VehicleUpdateRequest request);

    VehicleResponse getById(Long id);

    List<VehicleResponse> getAll(VehicleStatus status, Long categoryId, Long driverId);

    void delete(Long id);

    List<VehicleResponse> findVehiclesRequiringMaintenance();
}
