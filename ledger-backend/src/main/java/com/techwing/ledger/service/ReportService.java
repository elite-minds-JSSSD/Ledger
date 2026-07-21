package com.techwing.ledger.service;

import com.techwing.ledger.exception.ResourceNotFoundException;
import com.techwing.ledger.model.Report;
import com.techwing.ledger.model.User;
import com.techwing.ledger.repository.ReconciliationRepository;
import com.techwing.ledger.repository.ReportRepository;
import com.techwing.ledger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReconciliationRepository reconciliationRepository;

    public List<Report> getAll() {
        return reportRepository.findAll();
    }

    public Report getById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", id));
    }

    public List<Report> getByStatus(Report.ReportStatus status) {
        return reportRepository.findByStatus(status);
    }

    public Report generateReport(String reportName, Report.ReportType reportType,
            String dateRangeLabel, String userEmail) {
        User user = null;
        if (userEmail != null && !userEmail.isEmpty()) {
            user = userRepository.findByEmail(userEmail).orElse(null);
        }

        long total = reconciliationRepository.count();
        long matched = reconciliationRepository.countByMatchStatus(
                com.techwing.ledger.model.Reconciliation.MatchStatus.MATCHED);
        long unmatched = reconciliationRepository.countByMatchStatus(
                com.techwing.ledger.model.Reconciliation.MatchStatus.UNMATCHED);

        Report report = Report.builder()
                .reportName(reportName)
                .generatedBy(user)
                .reportType(reportType)
                .status(Report.ReportStatus.IN_PROGRESS)
                .totalTransactions(total)
                .matchedTransactions(matched)
                .unmatchedTransactions(unmatched)
                .dateRangeLabel(dateRangeLabel)
                .build();

        report = reportRepository.save(report);

        // Simulate generation complete
        report.setStatus(Report.ReportStatus.COMPLETED);
        report.setDownloadUrl("/api/reports/" + report.getId() + "/download");
        report.setFileSize("~2.5 MB");
        return reportRepository.save(report);
    }

    public void deleteById(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Report", "id", id);
        }
        reportRepository.deleteById(id);
    }

    public long countByStatus(Report.ReportStatus status) {
        return reportRepository.countByStatus(status);
    }
}
