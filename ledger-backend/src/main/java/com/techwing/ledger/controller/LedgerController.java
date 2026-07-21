package com.techwing.ledger.controller;

import com.techwing.ledger.dto.ApiResponse;
import com.techwing.ledger.model.LedgerEntry;
import com.techwing.ledger.service.LedgerEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ledger")
@RequiredArgsConstructor
@Tag(name = "Ledger Entries", description = "Internal Ledger CRUD endpoints")
public class LedgerController {

    private final LedgerEntryService ledgerEntryService;

    @GetMapping
    @Operation(summary = "Get all ledger entries (paginated)")
    public ResponseEntity<ApiResponse<Page<LedgerEntry>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                ledgerEntryService.getAll(PageRequest.of(page, size))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ledger entry by ID")
    public ResponseEntity<ApiResponse<LedgerEntry>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(ledgerEntryService.getById(id)));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get ledger entries by status")
    public ResponseEntity<ApiResponse<?>> getByStatus(@PathVariable LedgerEntry.EntryStatus status) {
        return ResponseEntity.ok(ApiResponse.success(ledgerEntryService.getByStatus(status)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a ledger entry")
    public ResponseEntity<ApiResponse<LedgerEntry>> update(
            @PathVariable Long id, @RequestBody LedgerEntry updatedEntry) {
        LedgerEntry existing = ledgerEntryService.getById(id);
        existing.setDescription(updatedEntry.getDescription());
        existing.setAmount(updatedEntry.getAmount());
        existing.setType(updatedEntry.getType());
        return ResponseEntity.ok(ApiResponse.success("Updated", ledgerEntryService.save(existing)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a ledger entry")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ledgerEntryService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true).message("Ledger entry deleted").build());
    }
}
