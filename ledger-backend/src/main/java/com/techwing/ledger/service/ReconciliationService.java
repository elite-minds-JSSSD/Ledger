package com.techwing.ledger.service;

import com.techwing.ledger.exception.ResourceNotFoundException;
import com.techwing.ledger.model.Discrepancy;
import com.techwing.ledger.model.LedgerEntry;
import com.techwing.ledger.model.Reconciliation;
import com.techwing.ledger.model.Transaction;
import com.techwing.ledger.repository.DiscrepancyRepository;
import com.techwing.ledger.repository.LedgerEntryRepository;
import com.techwing.ledger.repository.ReconciliationRepository;
import com.techwing.ledger.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReconciliationService {

    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final ReconciliationRepository reconciliationRepository;
    private final DiscrepancyRepository discrepancyRepository;

    public List<Reconciliation> getAll() {
        return reconciliationRepository.findAll();
    }

    public Reconciliation getById(Long id) {
        return reconciliationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reconciliation", "id", id));
    }

    public List<Reconciliation> getByMatchStatus(Reconciliation.MatchStatus status) {
        return reconciliationRepository.findByMatchStatus(status);
    }

    public long countByMatchStatus(Reconciliation.MatchStatus status) {
        return reconciliationRepository.countByMatchStatus(status);
    }

    /**
     * Core reconciliation engine:
     * 1. Fetch BANK transactions and LEDGER entries in date range
     * 2. Try to match by referenceNumber + amount within tolerance
     * 3. Partial match when amount differs by < 1%
     * 4. Create Reconciliation records and update statuses
     * 5. Auto-create Discrepancy records for unmatched/partial
     */
    @Transactional
    public List<Reconciliation> runReconciliation(LocalDate fromDate, LocalDate toDate, String reconciledBy) {
        log.info("Starting reconciliation from {} to {} by {}", fromDate, toDate, reconciledBy);

        List<Transaction> bankTxns = transactionRepository.findBySourceAndTransactionDateBetween(
                Transaction.TransactionSource.BANK, fromDate, toDate,
                org.springframework.data.domain.Pageable.unpaged())
                .getContent();

        List<LedgerEntry> ledgerEntries = ledgerEntryRepository.findByEntryDateBetween(
                fromDate, toDate, org.springframework.data.domain.Pageable.unpaged())
                .getContent();

        List<Reconciliation> results = new ArrayList<>();
        List<Long> matchedLedgerIds = new ArrayList<>();

        for (Transaction bank : bankTxns) {
            LedgerEntry bestMatch = findBestMatch(bank, ledgerEntries, matchedLedgerIds);

            if (bestMatch != null) {
                BigDecimal diff = bank.getAmount().subtract(bestMatch.getAmount()).abs();
                BigDecimal tolerance = bank.getAmount().multiply(BigDecimal.valueOf(0.01));
                Reconciliation.MatchStatus status = diff.compareTo(BigDecimal.ZERO) == 0
                        ? Reconciliation.MatchStatus.MATCHED
                        : diff.compareTo(tolerance) <= 0
                                ? Reconciliation.MatchStatus.PARTIAL_MATCH
                                : Reconciliation.MatchStatus.PARTIAL_MATCH;

                Reconciliation recon = Reconciliation.builder()
                        .bankTransactionId(bank.getTransactionId())
                        .ledgerTransactionId(bestMatch.getEntryId())
                        .reconciliationDate(LocalDate.now())
                        .matchStatus(status)
                        .matchedAmount(bestMatch.getAmount())
                        .remarks(status == Reconciliation.MatchStatus.MATCHED
                                ? "Auto-matched by reference and amount"
                                : "Partial match: amount differs by " + diff)
                        .reconciledBy(reconciledBy)
                        .build();

                reconciliationRepository.save(recon);
                results.add(recon);

                bank.setStatus(status == Reconciliation.MatchStatus.MATCHED
                        ? Transaction.TransactionStatus.MATCHED
                        : Transaction.TransactionStatus.PENDING);
                transactionRepository.save(bank);

                bestMatch.setStatus(status == Reconciliation.MatchStatus.MATCHED
                        ? LedgerEntry.EntryStatus.MATCHED
                        : LedgerEntry.EntryStatus.PENDING);
                ledgerEntryRepository.save(bestMatch);

                matchedLedgerIds.add(bestMatch.getId());

                // Create discrepancy for partial match
                if (status == Reconciliation.MatchStatus.PARTIAL_MATCH) {
                    createDiscrepancy(bank.getTransactionId(),
                            Discrepancy.DiscrepancyType.AMOUNT_MISMATCH,
                            bank.getAmount(), bestMatch.getAmount());
                }
            } else {
                // No match found
                bank.setStatus(Transaction.TransactionStatus.UNMATCHED);
                transactionRepository.save(bank);

                createDiscrepancy(bank.getTransactionId(),
                        Discrepancy.DiscrepancyType.MISSING_LEDGER_ENTRY,
                        bank.getAmount(), BigDecimal.ZERO);
            }
        }

        // Remaining unmatched ledger entries
        for (LedgerEntry entry : ledgerEntries) {
            if (!matchedLedgerIds.contains(entry.getId())) {
                entry.setStatus(LedgerEntry.EntryStatus.UNMATCHED);
                ledgerEntryRepository.save(entry);

                createDiscrepancy(entry.getEntryId(),
                        Discrepancy.DiscrepancyType.MISSING_BANK_ENTRY,
                        BigDecimal.ZERO, entry.getAmount());
            }
        }

        log.info("Reconciliation complete. {} results created.", results.size());
        return results;
    }

    private LedgerEntry findBestMatch(Transaction bank, List<LedgerEntry> entries, List<Long> alreadyMatched) {
        for (LedgerEntry entry : entries) {
            if (alreadyMatched.contains(entry.getId()))
                continue;
            if (bank.getReferenceNumber() != null
                    && bank.getReferenceNumber().equalsIgnoreCase(entry.getReferenceNumber())) {
                return entry;
            }
        }
        // Fallback: match by amount and date proximity
        for (LedgerEntry entry : entries) {
            if (alreadyMatched.contains(entry.getId()))
                continue;
            BigDecimal diff = bank.getAmount().subtract(entry.getAmount()).abs();
            BigDecimal tolerance = bank.getAmount().multiply(BigDecimal.valueOf(0.05));
            boolean dateSame = Math.abs(bank.getTransactionDate()
                    .toEpochDay() - entry.getEntryDate().toEpochDay()) <= 3;
            if (diff.compareTo(tolerance) <= 0 && dateSame) {
                return entry;
            }
        }
        return null;
    }

    private void createDiscrepancy(String txnId, Discrepancy.DiscrepancyType type,
            BigDecimal expected, BigDecimal actual) {
        Discrepancy d = Discrepancy.builder()
                .transactionId(txnId)
                .discrepancyType(type)
                .expectedAmount(expected)
                .actualAmount(actual)
                .difference(expected.subtract(actual).abs())
                .status(Discrepancy.DiscrepancyStatus.OPEN)
                .priority(type == Discrepancy.DiscrepancyType.AMOUNT_MISMATCH ? "HIGH" : "MEDIUM")
                .build();
        discrepancyRepository.save(d);
    }
}
