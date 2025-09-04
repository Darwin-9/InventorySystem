package com.binarycode.InventorySystemBackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {

    @Data
    public static class CreateCategoryRequest {
        @NotBlank(message = "El nombre de la categoría es requerido")
        private String name;
        
        private String description;
    }

    @Data
    public static class UpdateCategoryRequest {
        private String name;
        private String description;
        private Boolean active;
    }
}