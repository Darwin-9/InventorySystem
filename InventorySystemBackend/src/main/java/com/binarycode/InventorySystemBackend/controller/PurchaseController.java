package com.binarycode.InventorySystemBackend.controller;

import com.binarycode.InventorySystemBackend.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public List<PurchaseService.Purchase> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }
}
