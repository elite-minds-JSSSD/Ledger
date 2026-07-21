package com.techwing.ledger.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discrepancies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discrepancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private DiscrepancyType discrepancyType;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal expectedAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal actualAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal difference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private DiscrepancyStatus status = DiscrepancyStatus.OPEN;

    @Column(length = 1000)
    private String comments;

    @Column(length = 10)
    @Builder.Default
    private String priority = "MEDIUM"; // HIGH, MEDIUM, LOW

    @Column(length = 100)
    private String resolvedBy;

    private LocalDateTime resolvedAt;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public enum DiscrepancyType {
        AMOUNT_MISMATCH,
        MISSING_BANK_ENTRY,
        MISSING_LEDGER_ENTRY,
        DUPLICATE_TRANSACTION
    }

    public enum DiscrepancyStatus {
        OPEN, RESOLVED
    }
}
