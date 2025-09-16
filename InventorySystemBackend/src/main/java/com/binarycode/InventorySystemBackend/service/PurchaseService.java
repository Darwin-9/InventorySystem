package com.binarycode.InventorySystemBackend.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PurchaseService {

    private final List<Purchase> purchases = new ArrayList<>();

    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
    }

    public List<Purchase> getAllPurchases() {
        return Collections.unmodifiableList(purchases);
    }

    @Data
    public static class Purchase {
        private String productName;
        private int quantity;
        private BigDecimal price;
        private BigDecimal subtotal;
        private LocalDateTime purchaseDate = LocalDateTime.now();
    }
}
