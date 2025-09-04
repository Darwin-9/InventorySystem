package com.binarycode.InventorySystemBackend.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long id;
    
    @Column(name = "table_name", length = 100)
    private String tableName;
    
    @Column(name = "record_id")
    private Long recordId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ActionType actionType;
    
    @Column(name = "old_values", columnDefinition = "JSON")
    private String oldValues;
    
    @Column(name = "new_values", columnDefinition = "JSON")
    private String newValues;
    
    @Column(name = "action_timestamp")
    @Builder.Default
    private LocalDateTime actionTimestamp = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
