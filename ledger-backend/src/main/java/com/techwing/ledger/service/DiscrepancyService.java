package com.techwing.ledger.service;

import com.techwing.ledger.exception.ResourceNotFoundException;
import com.techwing.ledger.model.Discrepancy;
import com.techwing.ledger.repository.DiscrepancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiscrepancyService {

    private final DiscrepancyRepository discrepancyRepository;

    public List<Discrepancy> getAll() {
        return discrepancyRepository.findAll();
    }

    public Discrepancy getById(Long id) {
        return discrepancyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discrepancy", "id", id));
    }

    public List<Discrepancy> getByStatus(Discrepancy.DiscrepancyStatus status) {
        return discrepancyRepository.findByStatus(status);
    }

    public List<Discrepancy> getByType(Discrepancy.DiscrepancyType type) {
        return discrepancyRepository.findByDiscrepancyType(type);
    }

    public Discrepancy resolve(Long id, String resolvedBy, String comments) {
        Discrepancy discrepancy = getById(id);
        discrepancy.setStatus(Discrepancy.DiscrepancyStatus.RESOLVED);
        discrepancy.setResolvedBy(resolvedBy);
        discrepancy.setResolvedAt(LocalDateTime.now());
        if (comments != null && !comments.isEmpty()) {
            discrepancy.setComments(comments);
        }
        return discrepancyRepository.save(discrepancy);
    }

    public Map<String, Long> getCountByType() {
        return Map.of(
                "AMOUNT_MISMATCH",
                discrepancyRepository.countByDiscrepancyType(Discrepancy.DiscrepancyType.AMOUNT_MISMATCH),
                "MISSING_BANK_ENTRY",
                discrepancyRepository.countByDiscrepancyType(Discrepancy.DiscrepancyType.MISSING_BANK_ENTRY),
                "MISSING_LEDGER_ENTRY",
                discrepancyRepository.countByDiscrepancyType(Discrepancy.DiscrepancyType.MISSING_LEDGER_ENTRY),
                "DUPLICATE_TRANSACTION",
                discrepancyRepository.countByDiscrepancyType(Discrepancy.DiscrepancyType.DUPLICATE_TRANSACTION));
    }

    public long countByStatus(Discrepancy.DiscrepancyStatus status) {
        return discrepancyRepository.countByStatus(status);
    }
}
