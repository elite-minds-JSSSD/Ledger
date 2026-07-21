package com.techwing.ledger.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reconciliations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reconciliation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String bankTransactionId;

    @Column(nullable = false, length = 100)
    private String ledgerTransactionId;

    @Column(nullable = false)
    private LocalDate reconciliationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MatchStatus matchStatus;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal matchedAmount;

    @Column(length = 1000)
    private String remarks;

    @Column(length = 100)
    private String reconciledBy;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public enum MatchStatus {
        MATCHED, PARTIAL_MATCH, UNMATCHED
    }
}
