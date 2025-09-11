package com.binarycode.InventorySystemBackend.controller;

import com.binarycode.InventorySystemBackend.model.MovementTypeEnum;
import com.binarycode.InventorySystemBackend.model.StockMovement;
import com.binarycode.InventorySystemBackend.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movements")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService movementService;

    @GetMapping
    public ResponseEntity<List<StockMovement>> getAllMovements() {
        return ResponseEntity.ok(movementService.getAllMovements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovement> getMovementById(@PathVariable Long id) {
        return movementService.getMovementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StockMovement> createMovement(@RequestBody StockMovement movement) {
        StockMovement createdMovement = movementService.createMovement(movement);
        return ResponseEntity.ok(createdMovement);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockMovement>> getMovementsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(movementService.getMovementsByProduct(productId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StockMovement>> getMovementsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(movementService.getMovementsByUser(userId));
    }

    @GetMapping("/type/{movementType}")
    public ResponseEntity<List<StockMovement>> getMovementsByType(@PathVariable MovementTypeEnum movementType) {
        return ResponseEntity.ok(movementService.getMovementsByType(movementType));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<StockMovement>> getMovementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(movementService.getMovementsByDateRange(startDate, endDate));
    }

    @GetMapping("/reference/{referenceNumber}")
    public ResponseEntity<StockMovement> getMovementByReference(@PathVariable String referenceNumber) {
        return movementService.getMovementByReference(referenceNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}/stock")
    public ResponseEntity<Integer> getCurrentStock(@PathVariable Long productId) {
        return ResponseEntity.ok(movementService.getCurrentStockByProduct(productId));
    }
}