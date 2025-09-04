package com.binarycode.InventorySystemBackend.controller;

import com.binarycode.InventorySystemBackend.model.AuditHistory;
import com.binarycode.InventorySystemBackend.service.AuditHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditHistoryController {

    private final AuditHistoryService auditHistoryService;


    @GetMapping
    public List<AuditHistory> getAllAuditHistory() {
        return auditHistoryService.getAllAuditHistory();
    }

   
    @GetMapping("/table/{tableName}")
    public List<AuditHistory> getAuditByTableName(@PathVariable String tableName) {
        return auditHistoryService.getAuditHistoryByTable(tableName);
    }

   
    @GetMapping("/user/{userId}")
    public List<AuditHistory> getAuditByUser(@PathVariable Long userId) {
        return auditHistoryService.getAuditHistoryByUser(userId);
    }

    @GetMapping("/date-range")
    public List<AuditHistory> getAuditByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return auditHistoryService.getAuditHistoryByDateRange(startDate, endDate);
    }

 
    @GetMapping("/record")
    public List<AuditHistory> getAuditByRecord(
            @RequestParam String tableName,
            @RequestParam Long recordId) {
        return auditHistoryService.getAuditHistoryByRecord(tableName, recordId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditHistory> getAuditById(@PathVariable Long id) {
        Optional<AuditHistory> audit = auditHistoryService.getAuditHistoryById(id);
        return audit.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AuditHistory> createAudit(@RequestBody AuditHistory auditHistory) {
        AuditHistory saved = auditHistoryService.createAuditHistory(auditHistory);
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AuditHistory> updateAudit(@PathVariable Long id, @RequestBody AuditHistory auditHistory) {
        return auditHistoryService.updateAuditHistory(id, auditHistory)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAudit(@PathVariable Long id) {
        auditHistoryService.deleteAuditHistory(id);
        return ResponseEntity.noContent().build();
    }
}
