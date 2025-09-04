package com.binarycode.InventorySystemBackend.service;

import com.binarycode.InventorySystemBackend.model.MovementType;
import com.binarycode.InventorySystemBackend.model.MovementTypeEnum;
import com.binarycode.InventorySystemBackend.repository.MovementTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovementTypeService {

    private final MovementTypeRepository movementTypeRepository;

    public List<MovementType> getAllMovementTypes() {
        return movementTypeRepository.findAll();
    }

    public Optional<MovementType> getMovementTypeById(Long id) {
        return movementTypeRepository.findById(id);
    }

    public Optional<MovementType> getMovementTypeByName(MovementTypeEnum name) {
        // Cambiado: findByTypeName → findByName
        return movementTypeRepository.findByName(name);
    }
    
    public MovementType createMovementType(MovementType movementType) {
        return movementTypeRepository.save(movementType);
    }

    public MovementType updateMovementType(Long id, MovementType movementTypeDetails) {
        return movementTypeRepository.findById(id)
                .map(movementType -> {
                    movementType.setDescription(movementTypeDetails.getDescription());
                    return movementTypeRepository.save(movementType);
                })
                .orElseThrow(() -> new RuntimeException("Tipo de movimiento no encontrado"));
    }

    @Transactional
    public void deleteMovementType(Long id) {
        movementTypeRepository.deleteById(id);
    }

    public boolean movementTypeExists(MovementTypeEnum name) {
        return movementTypeRepository.existsByName(name);
    }
    
}