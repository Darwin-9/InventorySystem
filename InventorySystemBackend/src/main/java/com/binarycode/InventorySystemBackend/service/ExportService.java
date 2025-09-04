package com.binarycode.InventorySystemBackend.service;

import com.binarycode.InventorySystemBackend.model.Product;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final ProductService productService;

    public byte[] exportProductsToExcel() throws IOException {
        List<Product> products = productService.getActiveProducts();
        
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Productos");
            
          
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
           
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Nombre", "Descripción", "Precio Compra", "Precio Venta", "Stock Actual", "Stock Mínimo", "Ubicación"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            
            int rowIdx = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(product.getId()); 
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getDescription());
                row.createCell(3).setCellValue(product.getPurchasePrice().doubleValue());
                row.createCell(4).setCellValue(product.getSalePrice().doubleValue());
                row.createCell(5).setCellValue(product.getCurrentStock());
                row.createCell(6).setCellValue(product.getMinStock());
                row.createCell(7).setCellValue(product.getLocation());
            }
            
       
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public String exportProductsToCsv() {
        List<Product> products = productService.getActiveProducts();
        StringBuilder csvBuilder = new StringBuilder();
        
      
        csvBuilder.append("ID,Nombre,Descripción,Precio Compra,Precio Venta,Stock Actual,Stock Mínimo,Ubicación\n");
        
        
        for (Product product : products) {
            csvBuilder.append(product.getId()).append(",");
            csvBuilder.append("\"").append(escapeCsv(product.getName())).append("\","); 
            csvBuilder.append("\"").append(escapeCsv(product.getDescription())).append("\",");
            csvBuilder.append(product.getPurchasePrice()).append(",");
            csvBuilder.append(product.getSalePrice()).append(",");
            csvBuilder.append(product.getCurrentStock()).append(",");
            csvBuilder.append(product.getMinStock()).append(",");
            csvBuilder.append("\"").append(escapeCsv(product.getLocation())).append("\"\n");
        }
        
        return csvBuilder.toString();
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }
}