package com.binarycode.InventorySystemBackend.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "movement_types")
@Data
@NoArgsConstructor

@AllArgsConstructor
@Builder
public class MovementType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_type_id")
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_name")
    private MovementTypeEnum name; 
    
    @Column(length = 255)
    private String description;
    
    @OneToMany(mappedBy = "movementType", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore 
    private List<StockMovement> movements = new ArrayList<>();
}