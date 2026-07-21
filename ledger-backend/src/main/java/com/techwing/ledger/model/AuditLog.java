package com.techwing.ledger.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String action; // e.g. CSV_UPLOAD, RECONCILIATION_RUN, REPORT_GENERATED

    @Column(length = 100)
    private String entityName; // e.g. Transaction, Reconciliation

    @Column(length = 50)
    private String entityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by_id")
    private User performedBy;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 2000)
    private String details;

    @Column(length = 50)
    private String ipAddress;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}
