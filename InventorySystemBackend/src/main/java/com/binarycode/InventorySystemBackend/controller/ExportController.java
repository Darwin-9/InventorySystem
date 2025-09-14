package com.binarycode.InventorySystemBackend.controller;

import com.binarycode.InventorySystemBackend.dto.ExportDTO;
import com.binarycode.InventorySystemBackend.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/products")
    public ResponseEntity<?> exportProducts(
            @RequestParam String format,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Boolean lowStockOnly) {
        
        try {
            if ("excel".equalsIgnoreCase(format)) {
                byte[] excelData = exportService.exportProductsToExcel(
                    startDate, endDate, categoryId, supplierId, lowStockOnly);
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", "productos.xlsx");
                
                return ResponseEntity.ok().headers(headers).body(excelData);
                        
            } else if ("csv".equalsIgnoreCase(format)) {
                String csvData = exportService.exportProductsToCsv(
                    startDate, endDate, categoryId, supplierId, lowStockOnly);
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("text/csv"));
                headers.setContentDispositionFormData("attachment", "productos.csv");
                
                return ResponseEntity.ok().headers(headers).body(csvData);
            } else {
                return ResponseEntity.badRequest()
                        .body("Formato no válido. Use 'excel' o 'csv'.");
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error al generar el archivo: " + e.getMessage());
        }
    }

    @PostMapping("/report")
    public ResponseEntity<?> generateReport(@RequestBody ExportDTO.ExportRequest request) {
        try {
            byte[] reportData = exportService.exportFilteredReport(
                request.getFormat(),
                request.getStartDate(),
                request.getEndDate(),
                request.getCategoryId(),
                request.getSupplierId(),
                request.getLowStockOnly()
            );
            
            String filename = "reporte_inventario." + 
                ("excel".equalsIgnoreCase(request.getFormat()) ? "xlsx" : "csv");
            
            String contentType = "excel".equalsIgnoreCase(request.getFormat()) ? 
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" : "text/csv";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", filename);
            
            return ResponseEntity.ok().headers(headers).body(reportData);
                    
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error al generar el reporte: " + e.getMessage());
        }
    }
}