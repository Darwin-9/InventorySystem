package com.binarycode.InventorySystemBackend.repository;

import com.binarycode.InventorySystemBackend.model.AuditHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditHistoryRepository extends JpaRepository<AuditHistory, Long> {
    
       @Query("SELECT ah FROM AuditHistory ah WHERE ah.user.id = :userId ORDER BY ah.actionTimestamp DESC")
    List<AuditHistory> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT ah FROM AuditHistory ah WHERE ah.tableName = :tableName ORDER BY ah.actionTimestamp DESC")
    List<AuditHistory> findByTableName(@Param("tableName") String tableName);
    
    @Query("SELECT ah FROM AuditHistory ah WHERE ah.recordId = :recordId AND ah.tableName = :tableName ORDER BY ah.actionTimestamp DESC")
    List<AuditHistory> findByRecord(@Param("tableName") String tableName, 
                                   @Param("recordId") Long recordId);
    
    @Query("SELECT ah FROM AuditHistory ah WHERE ah.actionTimestamp BETWEEN :startDate AND :endDate ORDER BY ah.actionTimestamp DESC")
    List<AuditHistory> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT ah FROM AuditHistory ah WHERE ah.actionType = :actionType ORDER BY ah.actionTimestamp DESC")
    List<AuditHistory> findByActionType(@Param("actionType") String actionType);
}