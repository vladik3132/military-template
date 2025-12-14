package ua.edu.viti.military.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ua.edu.viti.military.dto.request.VehicleCategoryCreateRequest;
import ua.edu.viti.military.dto.request.VehicleCategoryUpdateRequest;
import ua.edu.viti.military.dto.response.VehicleCategoryResponse;
import ua.edu.viti.military.entity.VehicleCategory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleCategoryMapper {
    
    // Entity → ResponseDTO
    @Mapping(target = "id", ignore = false)
    VehicleCategoryResponse toResponseDTO(VehicleCategory entity);
    
    // List<Entity> → List<ResponseDTO>
    List<VehicleCategoryResponse> toResponseDTOList(List<VehicleCategory> entities);
    
    // CreateDTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "requiredLicense", ignore = true)
    @Mapping(source = "maxLoadKg", target = "maxLoadCapacity")
    VehicleCategory toEntity(VehicleCategoryCreateRequest dto);
    
    // Оновлення існуючого Entity з DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "requiredLicense", ignore = true)
    @Mapping(source = "maxLoadKg", target = "maxLoadCapacity")
    void updateEntityFromDTO(VehicleCategoryUpdateRequest dto, @MappingTarget VehicleCategory entity);
}
