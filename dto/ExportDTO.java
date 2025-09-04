package com.binarycode.InventorySystemBackend.dto;

import lombok.Data;

@Data
public class ExportDTO {

    @Data
    public static class ExportRequest {
        private String format;
        private String startDate;
        private String endDate;
        private Long categoryId;
        private Long supplierId;
        private Boolean lowStockOnly;
    }

    @Data
    public static class ReportFilterRequest {
        private String reportType;
        private String startDate;
        private String endDate;
        private String groupBy;
    }
}