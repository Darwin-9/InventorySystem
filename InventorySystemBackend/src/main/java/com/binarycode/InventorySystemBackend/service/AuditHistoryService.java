package com.binarycode.InventorySystemBackend.service;

import com.binarycode.InventorySystemBackend.model.AuditHistory;
import com.binarycode.InventorySystemBackend.repository.AuditHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditHistoryService {

    private final AuditHistoryRepository auditHistoryRepository;

 
    public List<AuditHistory> getAllAuditHistory() {
        return auditHistoryRepository.findAll();
    }

  
    public Optional<AuditHistory> getAuditHistoryById(Long id) {
        return auditHistoryRepository.findById(id);
    }


    public List<AuditHistory> getAuditHistoryByUser(Long userId) {
        return auditHistoryRepository.findByUserId(userId);
    }

   
    public List<AuditHistory> getAuditHistoryByTable(String tableName) {
        return auditHistoryRepository.findByTableName(tableName);
    }

  
    public List<AuditHistory> getAuditHistoryByRecord(String tableName, Long recordId) {
        return auditHistoryRepository.findByRecord(tableName, recordId);
    }

   
    public List<AuditHistory> getAuditHistoryByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditHistoryRepository.findByDateRange(startDate, endDate);
    }

   
    public List<AuditHistory> getAuditHistoryByActionType(String actionType) {
        return auditHistoryRepository.findByActionType(actionType);
    }

 
    public AuditHistory createAuditHistory(AuditHistory auditHistory) {
        return auditHistoryRepository.save(auditHistory);
    }

    
    @Transactional
    public Optional<AuditHistory> updateAuditHistory(Long id, AuditHistory auditHistory) {
        return auditHistoryRepository.findById(id).map(existing -> {
            existing.setTableName(auditHistory.getTableName());
            existing.setRecordId(auditHistory.getRecordId());
            existing.setActionType(auditHistory.getActionType());
            existing.setOldValues(auditHistory.getOldValues());
            existing.setNewValues(auditHistory.getNewValues());
            existing.setUser(auditHistory.getUser());
            existing.setActionTimestamp(auditHistory.getActionTimestamp());
            return auditHistoryRepository.save(existing);
        });
    }

    @Transactional
    public void deleteAuditHistory(Long id) {
        auditHistoryRepository.deleteById(id);
    }
}
