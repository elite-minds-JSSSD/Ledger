package com.techwing.ledger.service;

import com.techwing.ledger.dto.DashboardStatsDTO;
import com.techwing.ledger.model.Discrepancy;
import com.techwing.ledger.model.Reconciliation;
import com.techwing.ledger.model.Transaction;
import com.techwing.ledger.repository.AuditLogRepository;
import com.techwing.ledger.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionService transactionService;
    private final LedgerEntryService ledgerEntryService;
    private final ReconciliationService reconciliationService;
    private final DiscrepancyService discrepancyService;
    private final ReportRepository reportRepository;

    public DashboardStatsDTO getStats() {
        long totalBank = transactionService.countBySource(Transaction.TransactionSource.BANK);
        long totalLedger = ledgerEntryService.countByStatus(null) > 0
                ? ledgerEntryService.getAll(org.springframework.data.domain.Pageable.unpaged()).getTotalElements()
                : 0;
        long matched = reconciliationService.countByMatchStatus(Reconciliation.MatchStatus.MATCHED);
        long unmatched = reconciliationService.countByMatchStatus(Reconciliation.MatchStatus.UNMATCHED);
        long openDiscrepancies = discrepancyService.countByStatus(Discrepancy.DiscrepancyStatus.OPEN);
        long resolvedDiscrepancies = discrepancyService.countByStatus(Discrepancy.DiscrepancyStatus.RESOLVED);
        long totalReports = reportRepository.count();

        double matchRate = (totalBank > 0) ? ((double) matched / totalBank) * 100 : 0;

        return DashboardStatsDTO.builder()
                .totalBankTransactions(totalBank)
                .totalLedgerEntries(totalLedger)
                .matchedTransactions(matched)
                .unmatchedTransactions(unmatched)
                .openDiscrepancies(openDiscrepancies)
                .resolvedDiscrepancies(resolvedDiscrepancies)
                .totalReports(totalReports)
                .matchRate(Math.round(matchRate * 100.0) / 100.0)
                .discrepancyByType(discrepancyService.getCountByType())
                .build();
    }
}
