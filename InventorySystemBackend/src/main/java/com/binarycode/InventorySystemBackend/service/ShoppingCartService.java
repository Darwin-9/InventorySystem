package com.binarycode.InventorySystemBackend.service;

import com.binarycode.InventorySystemBackend.model.Product;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@SessionScope
public class ShoppingCartService {
    private final Map<Long, CartItem> items = new ConcurrentHashMap<>();

    @Data
    public static class CartItem {
        private Product product;
        private int quantity;
        private BigDecimal subtotal;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
            calculateSubtotal();
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
            calculateSubtotal();
        }

        private void calculateSubtotal() {
            if (product != null && product.getSalePrice() != null) {
                this.subtotal = product.getSalePrice().multiply(BigDecimal.valueOf(quantity));
            } else {
                this.subtotal = BigDecimal.ZERO;
            }
        }
    }

    public Map<Long, CartItem> getItems() {
        return new HashMap<>(items);
    }

    public void addItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        if (product.getSalePrice() == null || product.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El producto " + product.getName() + " no tiene un precio de venta válido");
        }

        if (product.getCurrentStock() < quantity) {
            throw new RuntimeException("Stock insuficiente. Solo hay " + product.getCurrentStock() + " unidades disponibles");
        }

        if (items.containsKey(product.getId())) {
            CartItem existingItem = items.get(product.getId());
            int newQuantity = existingItem.getQuantity() + quantity;

            if (product.getCurrentStock() < newQuantity) {
                throw new RuntimeException("Stock insuficiente. No puedes agregar " + quantity +
                        " unidades. Ya tienes " + existingItem.getQuantity() +
                        " en el carrito. Stock disponible: " + product.getCurrentStock());
            }

            existingItem.setQuantity(newQuantity);
        } else {
            items.put(product.getId(), new CartItem(product, quantity));
        }
    }

    public void removeItem(Long productId) {
        if (!items.containsKey(productId)) {
            throw new RuntimeException("Producto no encontrado en el carrito");
        }
        items.remove(productId);
    }

    public void updateQuantity(Long productId, int quantity) {
        if (!items.containsKey(productId)) {
            throw new RuntimeException("Producto no encontrado en el carrito");
        }

        if (quantity <= 0) {
            removeItem(productId);
            return;
        }

        CartItem item = items.get(productId);
        if (item.getProduct().getCurrentStock() < quantity) {
            throw new RuntimeException("Stock insuficiente. Solo hay " +
                    item.getProduct().getCurrentStock() + " unidades disponibles");
        }

        item.setQuantity(quantity);
    }

    public BigDecimal getTotal() {
        return items.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTotalItems() {
        return items.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public CartItem getItem(Long productId) {
        return items.get(productId);
    }
}
