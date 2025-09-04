package com.binarycode.InventorySystemBackend.service;

import com.binarycode.InventorySystemBackend.model.Category;
import com.binarycode.InventorySystemBackend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getActiveCategories() {
        return categoryRepository.findActiveCategories();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category categoryDetails) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(categoryDetails.getName());
                    category.setDescription(categoryDetails.getDescription());
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).ifPresent(category -> {
            category.setActive(false);
            categoryRepository.save(category);
        });
    }

    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.searchByName(name);
    }

    public long countActiveCategories() {
        return categoryRepository.count();
    }
}