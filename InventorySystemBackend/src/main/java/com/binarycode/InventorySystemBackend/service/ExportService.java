package com.binarycode.InventorySystemBackend.service;

import com.binarycode.InventorySystemBackend.model.Product;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final ProductService productService;

    public byte[] exportProductsToExcel(String startDate, String endDate, Long categoryId, 
                                      Long supplierId, Boolean lowStockOnly) throws IOException {
        
        List<Product> products = productService.getFilteredProducts(startDate, endDate, 
                                                                  categoryId, supplierId, lowStockOnly);
        
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Productos");
            
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Nombre", "Descripción", "Precio Compra", "Precio Venta", 
                              "Stock Actual", "Stock Mínimo", "Ubicación", "Categoría", "Proveedor",
                              "Fecha Creación"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
            
            int rowIdx = 1;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            
            for (Product product : products) {
                Row row = sheet.createRow(rowIdx++);
                
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getDescription());
                
                Cell purchasePriceCell = row.createCell(3);
                purchasePriceCell.setCellValue(product.getPurchasePrice() != null ? 
                    product.getPurchasePrice().doubleValue() : 0);
                purchasePriceCell.setCellStyle(numberStyle);
                
                Cell salePriceCell = row.createCell(4);
                salePriceCell.setCellValue(product.getSalePrice() != null ? 
                    product.getSalePrice().doubleValue() : 0);
                salePriceCell.setCellStyle(numberStyle);
                
                row.createCell(5).setCellValue(product.getCurrentStock());
                row.createCell(6).setCellValue(product.getMinStock());
                row.createCell(7).setCellValue(product.getLocation());
                row.createCell(8).setCellValue(product.getCategory() != null ? 
                    product.getCategory().getName() : "Sin categoría");
                row.createCell(9).setCellValue(product.getSupplier() != null ? 
                    product.getSupplier().getName() : "Sin proveedor");
                row.createCell(10).setCellValue(
                    product.getCreatedAt().format(dateFormatter));
            }
        
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public String exportProductsToCsv(String startDate, String endDate, Long categoryId, 
                                    Long supplierId, Boolean lowStockOnly) {
        
        List<Product> products = productService.getFilteredProducts(startDate, endDate, 
                                                                  categoryId, supplierId, lowStockOnly);
        
        StringBuilder csvBuilder = new StringBuilder();

        csvBuilder.append("ID,Nombre,Descripción,Precio Compra,Precio Venta,Stock Actual,Stock Mínimo,Ubicación,Categoría,Proveedor,Fecha Creación\n");
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (Product product : products) {
            csvBuilder.append(product.getId()).append(",");
            csvBuilder.append("\"").append(escapeCsv(product.getName())).append("\",");
            csvBuilder.append("\"").append(escapeCsv(product.getDescription())).append("\",");
            csvBuilder.append(product.getPurchasePrice() != null ? product.getPurchasePrice() : "0").append(",");
            csvBuilder.append(product.getSalePrice() != null ? product.getSalePrice() : "0").append(",");
            csvBuilder.append(product.getCurrentStock()).append(",");
            csvBuilder.append(product.getMinStock()).append(",");
            csvBuilder.append("\"").append(escapeCsv(product.getLocation())).append("\",");
            csvBuilder.append("\"").append(escapeCsv(
                product.getCategory() != null ? product.getCategory().getName() : "Sin categoría")).append("\",");
            csvBuilder.append("\"").append(escapeCsv(
                product.getSupplier() != null ? product.getSupplier().getName() : "Sin proveedor")).append("\",");
            csvBuilder.append("\"").append(product.getCreatedAt().format(dateFormatter)).append("\"\n");
        }
        
        return csvBuilder.toString();
    }

    public byte[] exportFilteredReport(String format, String startDate, String endDate, 
                                     Long categoryId, Long supplierId, Boolean lowStockOnly) throws IOException {
        
        if ("excel".equalsIgnoreCase(format)) {
            return exportProductsToExcel(startDate, endDate, categoryId, supplierId, lowStockOnly);
        } else if ("csv".equalsIgnoreCase(format)) {
            return exportProductsToCsv(startDate, endDate, categoryId, supplierId, lowStockOnly).getBytes();
        } else {
            throw new IllegalArgumentException("Formato no válido: " + format);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }
}