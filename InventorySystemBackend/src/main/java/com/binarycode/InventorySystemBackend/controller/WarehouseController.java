package com.binarycode.InventorySystemBackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/warehouse")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN', 'WAREHOUSE_STAFF')")
public class WarehouseController {

    @GetMapping("/inventory")
    public ResponseEntity<?> getInventory() {
        return ResponseEntity.ok("Inventario completo - ADMIN/WAREHOUSE_STAFF");
    }

    @PostMapping("/inventory/add")
    public ResponseEntity<?> addToInventory() {
        return ResponseEntity.ok("Agregar al inventario - ADMIN/WAREHOUSE_STAFF");
    }

    @PutMapping("/inventory/{id}/adjust")
    public ResponseEntity<?> adjustStock(@PathVariable Long id) {
        return ResponseEntity.ok("Ajustar stock " + id + " - ADMIN/WAREHOUSE_STAFF");
    }

    @GetMapping("/movements")
    public ResponseEntity<?> getMovements() {
        return ResponseEntity.ok("Movimientos de bodega - ADMIN/WAREHOUSE_STAFF");
    }
}