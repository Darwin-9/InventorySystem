package com.binarycode.InventorySystemBackend.controller;

import com.binarycode.InventorySystemBackend.dto.CartDTO;
import com.binarycode.InventorySystemBackend.model.Product;
import com.binarycode.InventorySystemBackend.service.ProductService;
import com.binarycode.InventorySystemBackend.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService cartService;
    private final ProductService productService;

    // Agregar producto con JSON
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartDTO.AddToCartRequest request) {
        try {
            Product product = productService.getProductById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            cartService.addItem(product, request.getQuantity());
            return ResponseEntity.ok("Producto agregado al carrito: " + product.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar cantidad con JSON
    @PutMapping("/update")
    public ResponseEntity<String> updateQuantity(@RequestBody CartDTO.UpdateCartRequest request) {
        try {
            cartService.updateQuantity(request.getProductId(), request.getQuantity());
            return ResponseEntity.ok("Cantidad actualizada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Remover producto con JSON
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestBody CartDTO.CartOperationRequest request) {
        try {
            cartService.removeItem(request.getProductId());
            return ResponseEntity.ok("Producto removido del carrito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Ver carrito completo
    @GetMapping
    public ResponseEntity<Map<Long, ShoppingCartService.CartItem>> getCart() {
        return ResponseEntity.ok(cartService.getItems());
    }

    // Obtener total
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getCartTotal() {
        return ResponseEntity.ok(cartService.getTotal());
    }

    // Contar items
    @GetMapping("/count")
    public ResponseEntity<Integer> getItemCount() {
        return ResponseEntity.ok(cartService.getTotalItems());
    }

    // Vaciar carrito
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        cartService.clear();
        return ResponseEntity.ok("Carrito vaciado");
    }

    // Verificar si está vacío
    @GetMapping("/empty")
    public ResponseEntity<Boolean> isCartEmpty() {
        return ResponseEntity.ok(cartService.isEmpty());
    }

    // Procesar compra
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout() {
        try {
            String result = cartService.processPurchase(productService);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Obtener item específico
    @GetMapping("/item/{productId}")
    public ResponseEntity<?> getCartItem(@PathVariable Long productId) {
        try {
            ShoppingCartService.CartItem item = cartService.getItem(productId);
            if (item == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}