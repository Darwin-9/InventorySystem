package com.binarycode.InventorySystemBackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SALES')")
public class SalesController {

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        return ResponseEntity.ok("Todas las órdenes - ADMIN/SALES");
    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder() {
        return ResponseEntity.ok("Crear orden - ADMIN/SALES");
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers() {
        return ResponseEntity.ok("Lista de clientes - ADMIN/SALES");
    }

    @GetMapping("/reports")
    public ResponseEntity<?> getSalesReports() {
        return ResponseEntity.ok("Reportes de ventas - ADMIN/SALES");
    }
}