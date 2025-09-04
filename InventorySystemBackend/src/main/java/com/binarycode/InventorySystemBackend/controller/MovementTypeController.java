package com.binarycode.InventorySystemBackend.controller;

import com.binarycode.InventorySystemBackend.model.MovementType;
import com.binarycode.InventorySystemBackend.model.MovementTypeEnum;
import com.binarycode.InventorySystemBackend.service.MovementTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movement-types")
@RequiredArgsConstructor
public class MovementTypeController {

    private final MovementTypeService movementTypeService;

    @GetMapping
    public ResponseEntity<List<MovementType>> getAllMovementTypes() {
        return ResponseEntity.ok(movementTypeService.getAllMovementTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementType> getMovementTypeById(@PathVariable Long id) {
        return movementTypeService.getMovementTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<MovementType> getMovementTypeByName(@PathVariable MovementTypeEnum name) {
        return movementTypeService.getMovementTypeByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MovementType> createMovementType(@RequestBody MovementType movementType) {
        return ResponseEntity.ok(movementTypeService.createMovementType(movementType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovementType> updateMovementType(@PathVariable Long id, @RequestBody MovementType movementTypeDetails) {
        try {
            return ResponseEntity.ok(movementTypeService.updateMovementType(id, movementTypeDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovementType(@PathVariable Long id) {
        movementTypeService.deleteMovementType(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{name}")
    public ResponseEntity<Boolean> movementTypeExists(@PathVariable MovementTypeEnum name) {
        return ResponseEntity.ok(movementTypeService.movementTypeExists(name));
    }
}