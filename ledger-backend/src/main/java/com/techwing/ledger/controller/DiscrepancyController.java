package com.techwing.ledger.controller;

import com.techwing.ledger.dto.ApiResponse;
import com.techwing.ledger.model.Discrepancy;
import com.techwing.ledger.service.AiClientService;
import com.techwing.ledger.service.DiscrepancyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/discrepancies")
@RequiredArgsConstructor
@Tag(name = "Discrepancies", description = "View and resolve discrepancies")
public class DiscrepancyController {

    private final DiscrepancyService discrepancyService;
    private final AiClientService aiClientService;

    @GetMapping
    @Operation(summary = "Get all discrepancies")
    public ResponseEntity<ApiResponse<List<Discrepancy>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(discrepancyService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get discrepancy by ID")
    public ResponseEntity<ApiResponse<Discrepancy>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(discrepancyService.getById(id)));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get discrepancies by status (OPEN / RESOLVED)")
    public ResponseEntity<ApiResponse<List<Discrepancy>>> getByStatus(
            @PathVariable Discrepancy.DiscrepancyStatus status) {
        return ResponseEntity.ok(ApiResponse.success(discrepancyService.getByStatus(status)));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get discrepancies by type")
    public ResponseEntity<ApiResponse<List<Discrepancy>>> getByType(
            @PathVariable Discrepancy.DiscrepancyType type) {
        return ResponseEntity.ok(ApiResponse.success(discrepancyService.getByType(type)));
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolve a discrepancy")
    public ResponseEntity<ApiResponse<Discrepancy>> resolve(
            @PathVariable Long id,
            @RequestParam(required = false) String comments,
            Authentication auth) {
        String resolvedBy = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(ApiResponse.success("Resolved",
                discrepancyService.resolve(id, resolvedBy, comments)));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get discrepancy counts by type (for dashboard charts)")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getSummary() {
        return ResponseEntity.ok(ApiResponse.success(discrepancyService.getCountByType()));
    }

    @PostMapping("/ai-analyze")
    @Operation(summary = "Get AI-powered discrepancy pattern analysis (Groq)")
    public ResponseEntity<ApiResponse<Object>> aiAnalyze(@RequestBody Map<String, Object> payload) {
        Object result = aiClientService.callAiDiscrepancyAnalysis(payload);
        return ResponseEntity.ok(ApiResponse.success("AI analysis complete", result));
    }
}
