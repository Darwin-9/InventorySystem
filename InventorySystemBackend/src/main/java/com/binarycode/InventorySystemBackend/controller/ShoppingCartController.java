package com.binarycode.InventorySystemBackend.controller;

import com.binarycode.InventorySystemBackend.dto.CartDTO;
import com.binarycode.InventorySystemBackend.model.Product;
import com.binarycode.InventorySystemBackend.service.ProductService;
import com.binarycode.InventorySystemBackend.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService cartService;
    private final ProductService productService;

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

    @PutMapping("/update")
    public ResponseEntity<String> updateQuantity(@RequestBody CartDTO.UpdateCartRequest request) {
        try {
            cartService.updateQuantity(request.getProductId(), request.getQuantity());
            return ResponseEntity.ok("Cantidad actualizada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestBody CartDTO.CartOperationRequest request) {
        try {
            cartService.removeItem(request.getProductId());
            return ResponseEntity.ok("Producto removido del carrito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<CartDTO.CartResponse> getCart() {
        CartDTO.CartResponse response = new CartDTO.CartResponse();

        List<CartDTO.CartItemResponse> items = cartService.getItems().values().stream().map(item -> {
            CartDTO.CartItemResponse dto = new CartDTO.CartItemResponse();
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setQuantity(item.getQuantity());
            dto.setSalePrice(item.getProduct().getSalePrice());
            dto.setSubtotal(item.getSubtotal());
            return dto;
        }).toList();

        response.setItems(items);
        response.setTotal(cartService.getTotal());

        return ResponseEntity.ok(response);
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

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout() {
        try {
            String result = cartService.processPurchase(productService);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/item/{productId}")
    public ResponseEntity<?> getCartItem(@PathVariable Long productId) {
        try {
            ShoppingCartService.CartItem item = cartService.getItem(productId);
            if (item == null) {
                return ResponseEntity.notFound().build();
            }

            CartDTO.CartItemResponse dto = new CartDTO.CartItemResponse();
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setQuantity(item.getQuantity());
            dto.setSalePrice(item.getProduct().getSalePrice());
            dto.setSubtotal(item.getSubtotal());

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
