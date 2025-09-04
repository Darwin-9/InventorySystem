package com.binarycode.InventorySystemBackend.controller;

import com.binarycode.InventorySystemBackend.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/products/excel")
    public ResponseEntity<byte[]> exportProductsToExcel() {
        try {
            byte[] excelData = exportService.exportProductsToExcel();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "productos.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/products/csv", produces = "text/csv")
    public ResponseEntity<String> exportProductsToCsv() {
        String csvData = exportService.exportProductsToCsv();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "productos.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }
}