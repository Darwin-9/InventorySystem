package com.binarycode.InventorySystemBackend.service;

import com.binarycode.InventorySystemBackend.model.Alert;
import com.binarycode.InventorySystemBackend.model.AlertType;
import com.binarycode.InventorySystemBackend.model.Product;
import com.binarycode.InventorySystemBackend.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final ProductService productService;

    public List<Alert> getAlertsByType(AlertType alertType) {
        return alertRepository.findByAlertType(alertType.name());
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public List<Alert> getActiveAlerts() {
        return alertRepository.findActiveAlerts();
    }

    public List<Alert> getResolvedAlerts() {
        return alertRepository.findResolvedAlerts();
    }

    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    @Transactional
    public Alert resolveAlert(Long alertId) {
        return alertRepository.findById(alertId)
                .map(alert -> {
                    alert.setResolved(true);
                    alert.setResolvedAt(LocalDateTime.now());
                    return alertRepository.save(alert);
                })
                .orElseThrow(() -> new RuntimeException("Alert not found"));
    }

    public List<Alert> getAlertsByProduct(Long productId) {
        return alertRepository.findByProductId(productId);
    }

    public long countActiveAlerts() {
        return alertRepository.countActiveAlerts();
    }

    public long countResolvedAlerts() {
        return alertRepository.countResolvedAlerts();
    }

    public long countAlertsByStatus(Boolean resolved) {
        return alertRepository.countByResolved(resolved);
    }

    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public void checkLowStockAlerts() {
        List<Product> lowStockProducts = productService.getProductsWithLowStock();

        for (Product product : lowStockProducts) {
            boolean hasActiveAlert = alertRepository.findByProductId(product.getId())
                    .stream()
                    .anyMatch(alert -> !alert.getResolved() && alert.getAlertType().equals(AlertType.LOW_STOCK));

            if (!hasActiveAlert) {
                Alert alert = new Alert();
                alert.setAlertType(AlertType.LOW_STOCK);
                alert.setMessage("Low stock for product: " + product.getName() +
                        ". Current stock: " + product.getCurrentStock() +
                        ", Minimum stock: " + product.getMinStock());
                alert.setProduct(product);
                alert.setResolved(false);
                alert.setCreatedAt(LocalDateTime.now());

                alertRepository.save(alert);
            }
        }
    }
}