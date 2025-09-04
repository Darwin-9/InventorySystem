package com.binarycode.InventorySystemBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {

    @Data
    public static class CreateProductRequest {
        @NotBlank(message = "El nombre del producto es requerido")
        private String name;
        
        private String description;
        
        @NotNull(message = "El precio de venta es requerido")
        @Positive(message = "El precio de venta debe ser positivo")
        private BigDecimal salePrice;
        
        private BigDecimal purchasePrice;
        
        @NotNull(message = "El stock actual es requerido")
        private Integer currentStock;
        
        private Integer minStock;
        private String location;
        private Long categoryId;
        private Long supplierId;
    }

    @Data
    public static class UpdateProductRequest {
        private String name;
        private String description;
        private BigDecimal salePrice;
        private BigDecimal purchasePrice;
        private Integer currentStock;
        private Integer minStock;
        private String location;
        private Long categoryId;
        private Long supplierId;
        private Boolean active;
    }

    @Data
    public static class StockUpdateRequest {
        @NotNull(message = "La cantidad es requerida")
        private Integer quantity;
        
        private String reason;
        private String referenceNumber;
    }
}