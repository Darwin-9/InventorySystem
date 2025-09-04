package com.binarycode.InventorySystemBackend.repository;

import com.binarycode.InventorySystemBackend.model.User;
import com.binarycode.InventorySystemBackend.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> findActiveUsers();
    
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.active = true")
    List<User> findByRole(@Param("role") UserRole role);
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%')) AND u.active = true")
    List<User> searchByFullName(@Param("name") String name);
}