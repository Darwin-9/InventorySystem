package com.binarycode.InventorySystemBackend.repository;

import com.binarycode.InventorySystemBackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
    
    @Query("SELECT c FROM Category c WHERE c.active = true")
    List<Category> findActiveCategories();
    
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Category> searchByName(@Param("name") String name);
}