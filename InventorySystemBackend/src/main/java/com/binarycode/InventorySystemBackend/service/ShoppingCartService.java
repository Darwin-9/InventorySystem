package com.binarycode.InventorySystemBackend.service;

import com.binarycode.InventorySystemBackend.model.Product;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
@Data
public class ShoppingCartService {
    private Map<Long, CartItem> items = new HashMap<>();
    
    public void addItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        
        if (items.containsKey(product.getId())) { 
            CartItem existingItem = items.get(product.getId());
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.put(product.getId(), new CartItem(product, quantity)); 
        }
    }
    
    public void removeItem(Long productId) {
        items.remove(productId);
    }
    
    public void updateQuantity(Long productId, int quantity) {
        if (quantity <= 0) {
            removeItem(productId);
        } else if (items.containsKey(productId)) {
            items.get(productId).setQuantity(quantity);
        }
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
    
    @Data
    public static class CartItem {
        private Product product;
        private int quantity;
        
        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
        
        public BigDecimal getSubtotal() {
            return product.getSalePrice().multiply(BigDecimal.valueOf(quantity));
        }
    }
}