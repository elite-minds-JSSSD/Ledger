package com.techwing.ledger.controller;

import com.techwing.ledger.dto.ApiResponse;
import com.techwing.ledger.model.Transaction;
import com.techwing.ledger.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Bank and Ledger transaction endpoints")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @Operation(summary = "Get all transactions (paginated)")
    public ResponseEntity<ApiResponse<Page<Transaction>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Transaction> txns = transactionService.getAll(PageRequest.of(page, size, sort));
        return ResponseEntity.ok(ApiResponse.success(txns));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<ApiResponse<Transaction>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getById(id)));
    }

    @GetMapping("/bank")
    @Operation(summary = "Get bank transactions")
    public ResponseEntity<ApiResponse<List<Transaction>>> getBankTransactions() {
        return ResponseEntity.ok(ApiResponse.success(
                transactionService.getBySource(Transaction.TransactionSource.BANK)));
    }

    @GetMapping("/ledger")
    @Operation(summary = "Get ledger transactions")
    public ResponseEntity<ApiResponse<List<Transaction>>> getLedgerTransactions() {
        return ResponseEntity.ok(ApiResponse.success(
                transactionService.getBySource(Transaction.TransactionSource.LEDGER)));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get transactions by status")
    public ResponseEntity<ApiResponse<List<Transaction>>> getByStatus(
            @PathVariable Transaction.TransactionStatus status) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getByStatus(status)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a transaction")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        transactionService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true).message("Transaction deleted").build());
    }
}
