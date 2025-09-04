package com.binarycode.InventorySystemBackend.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id")
    private Long id;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    @Column(name = "reference_number", length = 100)
    private String referenceNumber;
    
    @Column(name = "movement_date")
    @Builder.Default
    private LocalDateTime movementDate = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @ManyToOne
    @JoinColumn(name = "movement_type_id")
    private MovementType movementType;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}