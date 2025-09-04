package com.binarycode.InventorySystemBackend.repository;

import com.binarycode.InventorySystemBackend.model.MovementTypeEnum;
import com.binarycode.InventorySystemBackend.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
  
    @Query("SELECT sm FROM StockMovement sm WHERE sm.product.id = :productId ORDER BY sm.movementDate DESC")
    List<StockMovement> findByProductId(@Param("productId") Long productId);
    
    @Query("SELECT sm FROM StockMovement sm WHERE sm.user.id = :userId ORDER BY sm.movementDate DESC")
    List<StockMovement> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType.name = :movementType ORDER BY sm.movementDate DESC")
    List<StockMovement> findByMovementType(@Param("movementType") MovementTypeEnum movementType);
    
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementDate BETWEEN :startDate AND :endDate ORDER BY sm.movementDate DESC")
    List<StockMovement> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT sm FROM StockMovement sm WHERE sm.referenceNumber = :referenceNumber")
    Optional<StockMovement> findByReferenceNumber(@Param("referenceNumber") String referenceNumber);
    
    @Query("SELECT COALESCE(SUM(sm.quantity), 0) FROM StockMovement sm WHERE sm.product.id = :productId AND sm.movementType.name = 'ENTRADA'")
    Integer getTotalEntradasByProduct(@Param("productId") Long productId);
    

    @Query("SELECT COALESCE(SUM(sm.quantity), 0) FROM StockMovement sm WHERE sm.product.id = :productId AND sm.movementType.name = 'SALIDA'")
    Integer getTotalSalidasByProduct(@Param("productId") Long productId);
}