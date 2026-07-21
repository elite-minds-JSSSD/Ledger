package com.techwing.ledger.repository;

import com.techwing.ledger.model.Reconciliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReconciliationRepository extends JpaRepository<Reconciliation, Long> {
    List<Reconciliation> findByMatchStatus(Reconciliation.MatchStatus status);

    long countByMatchStatus(Reconciliation.MatchStatus status);

    List<Reconciliation> findByReconciliationDateBetween(LocalDate from, LocalDate to);
}
