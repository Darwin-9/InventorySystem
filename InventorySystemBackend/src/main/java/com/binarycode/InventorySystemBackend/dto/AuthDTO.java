package com.binarycode.InventorySystemBackend.dto;

import com.binarycode.InventorySystemBackend.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthDTO {

    @Data
    public static class LoginRequest {
        @NotBlank(message = "El username es requerido")
        private String username;
        
        @NotBlank(message = "La contraseña es requerida")
        private String password;
    }

    @Data
    public static class RegisterRequest {
        @NotBlank(message = "El username es requerido")
        private String username;
        
        @NotBlank(message = "El email es requerido")
        @Email(message = "El email debe ser válido")
        private String email;
        
        @NotBlank(message = "La contraseña es requerida")
        private String password;
        
        private String fullName;
        private UserRole role;
    }

    @Data
    public static class Verify2FARequest {
        private Long userId;
        
        @NotBlank(message = "El código de verificación es requerido")
        private String code;
    }

    @Data
    public static class ResetPasswordRequest {
        @NotBlank @Email
        private String email;
        
        @NotBlank(message = "La nueva contraseña es requerida")
        private String newPassword;
        
        @NotBlank(message = "La confirmación de contraseña es requerida")
        private String confirmPassword;
    }

    @Data
    public static class ForgotPasswordRequest {
        @NotBlank @Email
        private String email;
    }
}