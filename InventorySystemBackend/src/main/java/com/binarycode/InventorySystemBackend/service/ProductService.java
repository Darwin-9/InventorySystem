package com.binarycode.InventorySystemBackend.service;

import com.binarycode.InventorySystemBackend.model.Product;
import com.binarycode.InventorySystemBackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getActiveProducts() {
        return productRepository.findActiveProducts();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setPurchasePrice(productDetails.getPurchasePrice());
                    product.setSalePrice(productDetails.getSalePrice());
                    product.setCurrentStock(productDetails.getCurrentStock());
                    product.setMinStock(productDetails.getMinStock());
                    product.setLocation(productDetails.getLocation());
                    product.setCategory(productDetails.getCategory());
                    product.setSupplier(productDetails.getSupplier());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setActive(false);
            productRepository.save(product);
        });
    }

    public List<Product> getProductsWithLowStock() {
        return productRepository.findProductsWithLowStock();
    }

    public List<Product> getOutOfStockProducts() {
        return productRepository.findOutOfStockProducts();
    }

    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getProductsBySupplier(Long supplierId) {
        return productRepository.findBySupplierId(supplierId);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public void updateStock(Long productId, Integer quantity) {
        productRepository.findById(productId).ifPresent(product -> {
            product.setCurrentStock(product.getCurrentStock() + quantity);
            productRepository.save(product);
        });
    }
}