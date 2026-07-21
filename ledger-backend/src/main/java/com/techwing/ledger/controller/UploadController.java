package com.techwing.ledger.controller;

import com.techwing.ledger.dto.ApiResponse;
import com.techwing.ledger.dto.CsvUploadResultDTO;
import com.techwing.ledger.service.CsvUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@Tag(name = "CSV Upload", description = "Upload bank and ledger CSV files")
public class UploadController {

    private final CsvUploadService csvUploadService;

    @PostMapping(value = "/bank", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload bank statement CSV")
    public ResponseEntity<ApiResponse<CsvUploadResultDTO>> uploadBankCsv(
            @RequestParam("file") MultipartFile file) {
        CsvUploadResultDTO result = csvUploadService.uploadBankCsv(file);
        return ResponseEntity.ok(ApiResponse.success("Bank CSV uploaded successfully", result));
    }

    @PostMapping(value = "/ledger", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload internal ledger CSV")
    public ResponseEntity<ApiResponse<CsvUploadResultDTO>> uploadLedgerCsv(
            @RequestParam("file") MultipartFile file) {
        CsvUploadResultDTO result = csvUploadService.uploadLedgerCsv(file);
        return ResponseEntity.ok(ApiResponse.success("Ledger CSV uploaded successfully", result));
    }
}
