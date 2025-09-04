package com.binarycode.InventorySystemBackend.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long id;
    
    @Column(name = "supplier_name", length = 255)
    private String name;
    
    @Column(name = "contact_person", length = 255)
    private String contactPerson;
    
    @Column(name = "phone", length = 250)
    private String phone;
    
    @Column(name = "email", length = 255)
    private String email;
    
    @Column(columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean active = true;
    
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @JsonIgnore 
    @Builder.Default
    @ToString.Exclude
    private List<Product> products = new ArrayList<>();
    
    public Boolean isActive() {
        return active;
    }
}
