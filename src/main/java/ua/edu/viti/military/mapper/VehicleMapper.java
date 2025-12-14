package ua.edu.viti.military.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ua.edu.viti.military.dto.request.VehicleCreateRequest;
import ua.edu.viti.military.dto.request.VehicleUpdateRequest;
import ua.edu.viti.military.dto.response.VehicleResponse;
import ua.edu.viti.military.entity.Vehicle;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VehicleCategoryMapper.class})
public interface VehicleMapper {
    
    // Entity → ResponseDTO (включає вкладені дані категорії та водія)
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "category.code", target = "categoryCode")
    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(target = "driverFullName", expression = "java(buildDriverFullName(entity))")
    VehicleResponse toResponseDTO(Vehicle entity);
    
    // List
    List<VehicleResponse> toResponseDTOList(List<Vehicle> entities);
    
    // CreateDTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "active", ignore = true)
    Vehicle toEntity(VehicleCreateRequest dto);
    
    // UpdateDTO → Entity (тільки заповнені поля)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationNumber", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntityFromDTO(VehicleUpdateRequest dto, @MappingTarget Vehicle entity);

    // Допоміжний метод для формування ПІБ водія
    default String buildDriverFullName(Vehicle entity) {
        if (entity == null || entity.getDriver() == null) {
            return null;
        }
        String first = entity.getDriver().getFirstName();
        String last = entity.getDriver().getLastName();
        StringBuilder sb = new StringBuilder();
        if (last != null) {
            sb.append(last.trim());
        }
        if (first != null) {
            if (!sb.isEmpty()) {
                sb.append(" ");
            }
            sb.append(first.trim());
        }
        return sb.isEmpty() ? null : sb.toString();
    }
}
