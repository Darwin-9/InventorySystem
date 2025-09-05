package com.binarycode.InventorySystemBackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok("Lista de todos los usuarios - Solo ADMIN");
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser() {
        return ResponseEntity.ok("Crear usuario - Solo ADMIN");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok("Eliminar usuario " + id + " - Solo ADMIN");
    }

    @GetMapping("/settings")
    public ResponseEntity<?> getSystemSettings() {
        return ResponseEntity.ok("Configuración del sistema - Solo ADMIN");
    }
}