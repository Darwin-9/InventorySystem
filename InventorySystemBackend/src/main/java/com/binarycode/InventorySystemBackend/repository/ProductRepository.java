package com.binarycode.InventorySystemBackend.repository;

import com.binarycode.InventorySystemBackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
    List<Product> findByCurrentStockLessThan(Integer minStock);
    
    List<Product> findBySalePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT p FROM Product p WHERE p.active = true")
    List<Product> findActiveProducts();
    
    @Query("SELECT p FROM Product p WHERE p.currentStock <= p.minStock AND p.active = true")
    List<Product> findProductsWithLowStock();
    
    @Query("SELECT p FROM Product p WHERE p.currentStock = 0 AND p.active = true")
    List<Product> findOutOfStockProducts();
    
    @Query("SELECT p FROM Product p WHERE p.salePrice BETWEEN :minPrice AND :maxPrice AND p.active = true")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                  @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.active = true")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT p FROM Product p WHERE p.supplier.id = :supplierId AND p.active = true")
    List<Product> findBySupplierId(@Param("supplierId") Long supplierId);
    
    @Query("SELECT p FROM Product p WHERE p.createdAt BETWEEN :startDate AND :endDate AND p.active = true")
    List<Product> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.active = true")
    long countActiveProducts();
}