package com.binarycode.InventorySystemBackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierDTO {

    @Data
    public static class CreateSupplierRequest {
        @NotBlank(message = "El nombre del proveedor es requerido")
        private String name;
        
        private String contactPerson;
        private String phone;
        
        @NotBlank @Email
        private String email;
        
        private String address;
    }

    @Data
    public static class UpdateSupplierRequest {
        private String name;
        private String contactPerson;
        private String phone;
        private String email;
        private String address;
        private Boolean active;
    }
}