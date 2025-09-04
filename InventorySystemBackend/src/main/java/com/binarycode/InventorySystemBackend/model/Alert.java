package com.binarycode.InventorySystemBackend.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", length = 50, nullable = false) 
    private AlertType alertType;

    @Column(name = "alert_message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_resolved", nullable = false)
    @Builder.Default
    private Boolean resolved = false;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
