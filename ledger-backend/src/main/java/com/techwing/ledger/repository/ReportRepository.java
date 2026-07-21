package com.techwing.ledger.repository;

import com.techwing.ledger.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStatus(Report.ReportStatus status);

    List<Report> findByReportType(Report.ReportType type);

    long countByStatus(Report.ReportStatus status);
}
