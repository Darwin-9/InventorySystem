package com.binarycode.InventorySystemBackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()") 
public class DashboardController {

    @GetMapping("/overview")
    public ResponseEntity<?> getOverview() {
        return ResponseEntity.ok("Resumen general - Todos los usuarios autenticados");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        return ResponseEntity.ok("Perfil de usuario - Todos los usuarios autenticados");
    }
}