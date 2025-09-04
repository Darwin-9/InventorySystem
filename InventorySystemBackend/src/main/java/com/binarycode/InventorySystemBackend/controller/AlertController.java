package com.binarycode.InventorySystemBackend.controller;

import com.binarycode.InventorySystemBackend.model.Alert;
import com.binarycode.InventorySystemBackend.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Alert>> getActiveAlerts() {
        return ResponseEntity.ok(alertService.getActiveAlerts());
    }

    @GetMapping("/resolved")
    public ResponseEntity<List<Alert>> getResolvedAlerts() {
        return ResponseEntity.ok(alertService.getResolvedAlerts());
    }

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody Alert alert) {
        return ResponseEntity.ok(alertService.createAlert(alert));
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Alert> resolveAlert(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(alertService.resolveAlert(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Alert>> getAlertsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(alertService.getAlertsByProduct(productId));
    }

    @GetMapping("/stats")
    public ResponseEntity<String> getAlertStats() {
        long active = alertService.countActiveAlerts();
        long resolved = alertService.countResolvedAlerts();
        return ResponseEntity.ok("Alertas activas: " + active + ", Resueltas: " + resolved);
    }
}