package com.binarycode.InventorySystemBackend.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final ShoppingCartService cartService;
    private final ProductService productService;

    private final List<Purchase> purchaseHistory = new ArrayList<>();
    private int purchaseCounter = 1;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Purchase {
        private String purchaseId;
        private List<ShoppingCartService.CartItem> items;
        private BigDecimal total;
        private LocalDateTime date;
    }


    public Purchase processPurchase() {
        if (cartService.isEmpty()) {
            throw new RuntimeException("El carrito está vacío, no se puede procesar la compra");
        }

        cartService.getItems().values().forEach(item ->
                productService.updateStock(item.getProduct().getId(), -item.getQuantity())
        );

        Purchase purchase = new Purchase(
                "PUR-" + purchaseCounter++,
                new ArrayList<>(cartService.getItems().values()),
                cartService.getTotal(),
                LocalDateTime.now()
        );

        purchaseHistory.add(purchase);

        cartService.clear();

        return purchase;
    }

    public List<Purchase> getAllPurchases() {
        return purchaseHistory;
    }
}
