package com.binarycode.InventorySystemBackend.controller;

import com.binarycode.InventorySystemBackend.model.Product;
import com.binarycode.InventorySystemBackend.service.ProductService;
import com.binarycode.InventorySystemBackend.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService cartService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ShoppingCartService.CartItem[]> getCart() {
        return ResponseEntity.ok(cartService.getItems().values().toArray(new ShoppingCartService.CartItem[0]));
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<String> addToCart(@PathVariable Long productId, @RequestParam int quantity) {
        try {
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            cartService.addItem(product, quantity);
            return ResponseEntity.ok("Producto agregado al carrito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long productId) {
        cartService.removeItem(productId);
        return ResponseEntity.ok("Producto removido del carrito");
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<String> updateQuantity(@PathVariable Long productId, @RequestParam int quantity) {
        try {
            cartService.updateQuantity(productId, quantity);
            return ResponseEntity.ok("Cantidad actualizada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getCartTotal() {
        return ResponseEntity.ok(cartService.getTotal());
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getItemCount() {
        return ResponseEntity.ok(cartService.getTotalItems());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        cartService.clear();
        return ResponseEntity.ok("Carrito vaciado");
    }

    @GetMapping("/empty")
    public ResponseEntity<Boolean> isCartEmpty() {
        return ResponseEntity.ok(cartService.isEmpty());
    }
}