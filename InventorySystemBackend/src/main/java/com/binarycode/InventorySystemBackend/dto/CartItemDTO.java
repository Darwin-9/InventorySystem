package com.binarycode.InventorySystemBackend.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long productId;
    private String productName;
    private double price;
    private int quantity;

    public CartItemDTO(Long productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }
}
