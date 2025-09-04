package com.binarycode.InventorySystemBackend.service;

import com.binarycode.InventorySystemBackend.model.MovementTypeEnum;
import com.binarycode.InventorySystemBackend.model.StockMovement;
import com.binarycode.InventorySystemBackend.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductService productService;

    public List<StockMovement> getAllMovements() {
        return stockMovementRepository.findAll();
    }

    public Optional<StockMovement> getMovementById(Long id) {
        return stockMovementRepository.findById(id);
    }

    @Transactional
    public StockMovement createMovement(StockMovement movement) {
        MovementTypeEnum movementType = movement.getMovementType().getName();
        
        if (movementType == MovementTypeEnum.ENTRADA) {
            productService.updateStock(movement.getProduct().getId(), movement.getQuantity()); 
        } else if (movementType == MovementTypeEnum.SALIDA || 
                   movementType == MovementTypeEnum.VENTA) {
            productService.updateStock(movement.getProduct().getId(), -movement.getQuantity()); 
        }
        
        return stockMovementRepository.save(movement);
    }

    public List<StockMovement> getMovementsByProduct(Long productId) {
        return stockMovementRepository.findByProductId(productId);
    }

    public List<StockMovement> getMovementsByUser(Long userId) {
        return stockMovementRepository.findByUserId(userId);
    }

    public List<StockMovement> getMovementsByType(MovementTypeEnum movementType) {
        return stockMovementRepository.findByMovementType(movementType);
    }

    public List<StockMovement> getMovementsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return stockMovementRepository.findByDateRange(startDate, endDate);
    }

    public Optional<StockMovement> getMovementByReference(String referenceNumber) {
        return stockMovementRepository.findByReferenceNumber(referenceNumber);
    }

    public Integer getTotalEntradasByProduct(Long productId) {
        return stockMovementRepository.getTotalEntradasByProduct(productId);
    }

    public Integer getTotalSalidasByProduct(Long productId) {
        return stockMovementRepository.getTotalSalidasByProduct(productId);
    }

    public Integer getCurrentStockByProduct(Long productId) {
        Integer entradas = getTotalEntradasByProduct(productId);
        Integer salidas = getTotalSalidasByProduct(productId);
        return entradas - salidas;
    }
}