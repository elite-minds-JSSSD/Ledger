package com.techwing.ledger.controller;

import com.techwing.ledger.dto.ApiResponse;
import com.techwing.ledger.dto.ReconciliationRequestDTO;
import com.techwing.ledger.model.Reconciliation;
import com.techwing.ledger.service.AiClientService;
import com.techwing.ledger.service.ReconciliationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reconciliation")
@RequiredArgsConstructor
@Tag(name = "Reconciliation", description = "Run reconciliation and view results")
public class ReconciliationController {

    private final ReconciliationService reconciliationService;
    private final AiClientService aiClientService;

    @PostMapping("/run")
    @Operation(summary = "Run reconciliation for a date range")
    public ResponseEntity<ApiResponse<List<Reconciliation>>> runReconciliation(
            @Valid @RequestBody ReconciliationRequestDTO request,
            Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        List<Reconciliation> results = reconciliationService.runReconciliation(
                request.getFromDate(), request.getToDate(), username);
        return ResponseEntity.ok(ApiResponse.success(
                "Reconciliation completed: " + results.size() + " records", results));
    }

    @GetMapping("/results")
    @Operation(summary = "Get all reconciliation results")
    public ResponseEntity<ApiResponse<List<Reconciliation>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(reconciliationService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reconciliation by ID")
    public ResponseEntity<ApiResponse<Reconciliation>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reconciliationService.getById(id)));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get reconciliations by match status")
    public ResponseEntity<ApiResponse<List<Reconciliation>>> getByStatus(
            @PathVariable Reconciliation.MatchStatus status) {
        return ResponseEntity.ok(ApiResponse.success(reconciliationService.getByMatchStatus(status)));
    }

    @PostMapping("/ai-analyze")
    @Operation(summary = "Run AI-based reconciliation analysis (Groq-powered)")
    public ResponseEntity<ApiResponse<Object>> aiAnalyze(@RequestBody Map<String, Object> payload) {
        Object result = aiClientService.callAiReconcile(payload);
        return ResponseEntity.ok(ApiResponse.success("AI analysis complete", result));
    }
}
