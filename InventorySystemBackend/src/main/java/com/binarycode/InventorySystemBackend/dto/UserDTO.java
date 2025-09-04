package com.binarycode.InventorySystemBackend.dto;

import com.binarycode.InventorySystemBackend.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

    @Data
    public static class CreateUserRequest {
        @NotBlank(message = "El username es requerido")
        private String username;
        
        @NotBlank(message = "El email es requerido")
        @Email(message = "El email debe ser válido")
        private String email;
        
        @NotBlank(message = "La contraseña es requerida")
        private String password;
        
        private String fullName;
        private UserRole role;
        private Boolean twoFactorEnabled = false;
    }

    @Data
    public static class UpdateUserRequest {
        private String fullName;
        private String email;
        private UserRole role;
        private Boolean active;
        private Boolean twoFactorEnabled;
    }

    @Data
    public static class ChangePasswordRequest {
        @NotBlank(message = "La contraseña actual es requerida")
        private String currentPassword;
        
        @NotBlank(message = "La nueva contraseña es requerida")
        private String newPassword;
        
        @NotBlank(message = "La confirmación de contraseña es requerida")
        private String confirmPassword;
    }
}