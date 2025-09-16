package com.binarycode.InventorySystemBackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartDTO {

    @Data
    public static class AddToCartRequest {
        @NotNull(message = "El producto es requerido")
        private Long productId;

        @NotNull(message = "La cantidad es requerida")
        @Positive(message = "La cantidad debe ser positiva")
        private Integer quantity;
    }

    @Data
    public static class UpdateCartRequest {
        @NotNull(message = "La cantidad es requerida")
        @Positive(message = "La cantidad debe ser positiva")
        private Integer quantity;

        @NotNull(message = "El producto es requerido")
        private Long productId;
    }

    @Data
    public static class CartOperationRequest {
        @NotNull(message = "El producto es requerido")
        private Long productId;
    }

    @Data
    public static class CartItemResponse {
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal salePrice;
        private BigDecimal subtotal;
    }

    @Data
    public static class CartResponse {
        private java.util.List<CartItemResponse> items;
        private BigDecimal total;
    }
}
