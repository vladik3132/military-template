package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.VehicleCategory;

import java.util.Optional;

@Repository
public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory, Long> {

    Optional<VehicleCategory> findByName(String name);

    boolean existsByName(String name);

    Optional<VehicleCategory> findByCode(String code);

    boolean existsByCode(String code);
}
