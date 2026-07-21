package com.techwing.ledger.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String reportName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by_id")
    private User generatedBy;

    @Column(nullable = false)
    private LocalDateTime generatedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ReportStatus status = ReportStatus.IN_PROGRESS;

    private Long totalTransactions;
    private Long matchedTransactions;
    private Long unmatchedTransactions;

    @Column(length = 500)
    private String downloadUrl;

    @Column(length = 100)
    private String dateRangeLabel;

    @Column(length = 20)
    private String fileSize;

    @PrePersist
    public void prePersist() {
        this.generatedDate = LocalDateTime.now();
    }

    public enum ReportType {
        PDF, EXCEL,
        RECONCILIATION, AUDIT, EXCEPTION, COMPLIANCE, SUMMARY, TRANSACTION
    }

    public enum ReportStatus {
        IN_PROGRESS, COMPLETED, FAILED
    }
}
