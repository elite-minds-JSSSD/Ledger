package com.techwing.ledger.repository;

import com.techwing.ledger.model.Discrepancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DiscrepancyRepository extends JpaRepository<Discrepancy, Long> {
    List<Discrepancy> findByStatus(Discrepancy.DiscrepancyStatus status);

    List<Discrepancy> findByDiscrepancyType(Discrepancy.DiscrepancyType type);

    long countByStatus(Discrepancy.DiscrepancyStatus status);

    long countByDiscrepancyType(Discrepancy.DiscrepancyType type);
}
