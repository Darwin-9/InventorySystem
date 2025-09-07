package com.binarycode.InventorySystemBackend.controller;

import com.binarycode.InventorySystemBackend.dto.RegisterRequestDTO;
import com.binarycode.InventorySystemBackend.model.User;
import com.binarycode.InventorySystemBackend.model.UserRole;
import com.binarycode.InventorySystemBackend.service.JwtService; 
import com.binarycode.InventorySystemBackend.service.CustomUserDetailsService;
import com.binarycode.InventorySystemBackend.service.UserService;
import com.binarycode.InventorySystemBackend.service.TwoFactorService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TwoFactorService twoFactorService;
    private final JwtService jwtService; 
    private final CustomUserDetailsService userDetailsService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.verifyCredentials(request.getUsername(), request.getPassword());
            
            if (user.getTwoFactorEnabled() != null && user.getTwoFactorEnabled()) {
                twoFactorService.generateAndSend2FACode(user.getEmail());
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Código de verificación enviado");
                response.put("userId", user.getId());
                response.put("email", user.getEmail());
                response.put("requires2FA", true);
                
                return ResponseEntity.ok(response);
            } else {
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                String jwtToken = jwtService.generateToken(userDetails);
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login exitoso");
                response.put("user", user);
                response.put("role", user.getRole());
                response.put("token", jwtToken);
                
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.out.println("ERROR completo: ");
            e.printStackTrace();
            return ResponseEntity.status(401).body("Credenciales inválidas: " + e.getMessage());
        }
    }

   @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2FA(@RequestBody Verify2FARequest request) {
    try {
        User user = userService.getUserById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        boolean isValid = twoFactorService.verify2FACode(user.getEmail(), request.getCode());
        
        if (!isValid) {
            return ResponseEntity.status(401).body("Código de verificación inválido");
        }
        
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login exitoso");
        response.put("user", user);
        response.put("role", user.getRole());
        response.put("token", jwtToken);
        
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error en verificación: " + e.getMessage());
    }
}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
    try {
        if (userService.usernameExists(request.getUsername())) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
        }

        if (userService.emailExists(request.getEmail())) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }

        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .passwordHash(request.getPassword())
            .fullName(request.getFullName())
            .role(request.getRole() != null ? request.getRole() : UserRole.SALES)
            .twoFactorEnabled(true)
            .build();

        User savedUser = userService.createUser(user);

        return ResponseEntity.ok(savedUser);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}


    @PostMapping("/resend-2fa")
    public ResponseEntity<?> resend2FACode(@RequestParam String email) {
        try {
            twoFactorService.generateAndSend2FACode(email);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Nuevo código de verificación enviado");
            response.put("email", email);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al reenviar código: " + e.getMessage());
        }
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class Verify2FARequest {
        private Long userId;
        private String code;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
        private String fullName;
        private UserRole role;
    }
}