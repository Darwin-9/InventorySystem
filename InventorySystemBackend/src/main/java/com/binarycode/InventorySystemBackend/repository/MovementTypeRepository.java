package com.binarycode.InventorySystemBackend.repository;

import com.binarycode.InventorySystemBackend.model.MovementType;
import com.binarycode.InventorySystemBackend.model.MovementTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovementTypeRepository extends JpaRepository<MovementType, Long> {
    Optional<MovementType> findByName(MovementTypeEnum name);
    boolean existsByName(MovementTypeEnum name);
}