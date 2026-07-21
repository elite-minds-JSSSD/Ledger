package com.techwing.ledger.controller;

import com.techwing.ledger.dto.ApiResponse;
import com.techwing.ledger.model.Report;
import com.techwing.ledger.service.AiClientService;
import com.techwing.ledger.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Audit Reports", description = "Generate and download audit reports")
public class ReportController {

    private final ReportService reportService;
    private final AiClientService aiClientService;

    @GetMapping
    @Operation(summary = "Get all reports")
    public ResponseEntity<ApiResponse<List<Report>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get report by ID")
    public ResponseEntity<ApiResponse<Report>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getById(id)));
    }

    @PostMapping("/generate")
    @Operation(summary = "Generate a new report")
    public ResponseEntity<ApiResponse<Report>> generate(
            @RequestParam String reportName,
            @RequestParam Report.ReportType reportType,
            @RequestParam(required = false, defaultValue = "All period") String dateRangeLabel,
            Authentication auth) {
        String email = auth != null ? auth.getName() : null;
        Report report = reportService.generateReport(reportName, reportType, dateRangeLabel, email);
        return ResponseEntity.ok(ApiResponse.success("Report generated", report));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get reports by status")
    public ResponseEntity<ApiResponse<List<Report>>> getByStatus(@PathVariable Report.ReportStatus status) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getByStatus(status)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a report")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        reportService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).message("Report deleted").build());
    }

    @PostMapping("/ai-summary")
    @Operation(summary = "Generate AI-powered report summary (Groq)")
    public ResponseEntity<ApiResponse<Object>> aiSummary(@RequestBody Map<String, Object> payload) {
        Object result = aiClientService.callAiReportSummary(payload);
        return ResponseEntity.ok(ApiResponse.success("AI summary generated", result));
    }
}
