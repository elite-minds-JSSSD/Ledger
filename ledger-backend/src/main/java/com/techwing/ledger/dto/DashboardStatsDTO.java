package com.techwing.ledger.dto;

import lombok.*;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalBankTransactions;
    private long totalLedgerEntries;
    private long matchedTransactions;
    private long unmatchedTransactions;
    private long openDiscrepancies;
    private long resolvedDiscrepancies;
    private long totalReports;
    private double matchRate;
    private double totalCredits;
    private double totalDebits;
    private Map<String, Long> discrepancyByType;
}
