package com.binarycode.InventorySystemBackend.dto;

import lombok.Data;

@Data
public class AlertDTO {

    @Data
    public static class CreateAlertRequest {
        private String alertType;
        private String message;
        private Long productId;
    }

    @Data
    public static class UpdateAlertRequest {
        private Boolean resolved;
    }

    @Data
    public static class AlertFilterRequest {
        private Boolean resolved;
        private Long productId;
        private String alertType;
    }
}