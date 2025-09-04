package com.binarycode.InventorySystemBackend.repository;

import com.binarycode.InventorySystemBackend.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    
    @Query("SELECT a FROM Alert a WHERE a.resolved = false ORDER BY a.createdAt DESC")
    List<Alert> findActiveAlerts();
    
    @Query("SELECT a FROM Alert a WHERE a.resolved = true ORDER BY a.resolvedAt DESC")
    List<Alert> findResolvedAlerts();
    
    @Query("SELECT a FROM Alert a WHERE a.product.id = :productId ORDER BY a.createdAt DESC")
    List<Alert> findByProductId(@Param("productId") Long productId);
    
    @Query("SELECT a FROM Alert a WHERE a.alertType = :alertType ORDER BY a.createdAt DESC")
    List<Alert> findByAlertType(@Param("alertType") String alertType);
    
    
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.resolved = false")
    long countActiveAlerts();
    
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.resolved = true")
    long countResolvedAlerts();
    
  
    long countByResolved(Boolean resolved); 
}