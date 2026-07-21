package com.techwing.ledger.repository;

import com.techwing.ledger.model.LedgerEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    Page<LedgerEntry> findAllByOrderByEntryDateDesc(Pageable pageable);

    List<LedgerEntry> findByStatus(LedgerEntry.EntryStatus status);

    boolean existsByEntryId(String entryId);

    Page<LedgerEntry> findByEntryDateBetween(LocalDate from, LocalDate to, Pageable pageable);

    long countByStatus(LedgerEntry.EntryStatus status);
}
