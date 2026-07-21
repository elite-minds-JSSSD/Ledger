package com.techwing.ledger.service;

import com.techwing.ledger.exception.ResourceNotFoundException;
import com.techwing.ledger.model.LedgerEntry;
import com.techwing.ledger.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LedgerEntryService {

    private final LedgerEntryRepository ledgerEntryRepository;

    public Page<LedgerEntry> getAll(Pageable pageable) {
        return ledgerEntryRepository.findAllByOrderByEntryDateDesc(pageable);
    }

    public LedgerEntry getById(Long id) {
        return ledgerEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LedgerEntry", "id", id));
    }

    public List<LedgerEntry> getByStatus(LedgerEntry.EntryStatus status) {
        return ledgerEntryRepository.findByStatus(status);
    }

    public LedgerEntry save(LedgerEntry entry) {
        return ledgerEntryRepository.save(entry);
    }

    public void deleteById(Long id) {
        if (!ledgerEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("LedgerEntry", "id", id);
        }
        ledgerEntryRepository.deleteById(id);
    }

    public long countByStatus(LedgerEntry.EntryStatus status) {
        return ledgerEntryRepository.countByStatus(status);
    }
}
