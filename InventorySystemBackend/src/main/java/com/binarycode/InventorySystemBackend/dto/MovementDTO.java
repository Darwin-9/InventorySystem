package com.binarycode.InventorySystemBackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovementDTO {

    @Data
    public static class CreateMovementRequest {
        @NotNull(message = "El producto es requerido")
        private Long productId;
        
        @NotNull(message = "La cantidad es requerida")
        private Integer quantity;
        
        @NotNull(message = "El tipo de movimiento es requerido")
        private Long movementTypeId;
        
        private String reason;
        private String referenceNumber;
        private Long userId;
    }

    @Data
    public static class MovementFilterRequest {
        private Long productId;
        private Long movementTypeId;
        private Long userId;
        private String startDate;
        private String endDate;
    }
}