package com.binarycode.InventorySystemBackend.repository;

import com.binarycode.InventorySystemBackend.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    Optional<Supplier> findByName(String name);

    Optional<Supplier> findByEmail(String email);
    
    @Query("SELECT s FROM Supplier s WHERE s.active = true")
    List<Supplier> findActiveSuppliers();
    
    @Query("SELECT s FROM Supplier s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.active = true")
    List<Supplier> searchActiveSuppliersByName(@Param("name") String name);
    
    List<Supplier> findByPhone(String phone);
}
