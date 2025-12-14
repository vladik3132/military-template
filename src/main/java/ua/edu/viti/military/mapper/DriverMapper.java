package ua.edu.viti.military.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ua.edu.viti.military.dto.request.DriverCreateRequest;
import ua.edu.viti.military.dto.request.DriverUpdateRequest;
import ua.edu.viti.military.dto.response.DriverResponse;
import ua.edu.viti.military.entity.Driver;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    
    // Entity → ResponseDTO
    DriverResponse toResponseDTO(Driver entity);
    
    // List
    List<DriverResponse> toResponseDTOList(List<Driver> entities);
    
    // CreateDTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Driver toEntity(DriverCreateRequest dto);
    
    // UpdateDTO → Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "licenseNumber", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(DriverUpdateRequest dto, @MappingTarget Driver entity);
}
