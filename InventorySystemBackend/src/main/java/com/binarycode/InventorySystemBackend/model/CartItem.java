package com.binarycode.InventorySystemBackend.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItem {
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