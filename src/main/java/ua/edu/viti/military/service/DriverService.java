package ua.edu.viti.military.service;

import ua.edu.viti.military.dto.request.DriverCreateRequest;
import ua.edu.viti.military.dto.request.DriverUpdateRequest;
import ua.edu.viti.military.dto.response.DriverResponse;

import java.util.List;

public interface DriverService {

    DriverResponse create(DriverCreateRequest request);

    DriverResponse getById(Long id);

    List<DriverResponse> getAll(Boolean isActive);

    DriverResponse update(Long id, DriverUpdateRequest request);

    void delete(Long id);
}
