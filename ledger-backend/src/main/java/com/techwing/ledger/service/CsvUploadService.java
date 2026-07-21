package com.techwing.ledger.service;

import com.techwing.ledger.dto.CsvUploadResultDTO;

import com.techwing.ledger.exception.InvalidFileFormatException;
import com.techwing.ledger.model.LedgerEntry;
import com.techwing.ledger.model.Transaction;
import com.techwing.ledger.repository.LedgerEntryRepository;
import com.techwing.ledger.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvUploadService {

    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    private static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    public CsvUploadResultDTO uploadBankCsv(MultipartFile file) {
        validateCsvFile(file);
        List<String> errors = new ArrayList<>();
        int successRows = 0, skippedRows = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
                        .withIgnoreHeaderCase().withTrim().parse(reader)) {

            for (CSVRecord record : parser) {
                try {
                    String txnId = getField(record, "TransactionId", "Transaction_Id", "ID", "Id");
                    if (txnId == null || txnId.isEmpty()) {
                        txnId = "BANK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    }

                    if (transactionRepository.existsByTransactionId(txnId)) {
                        skippedRows++;
                        continue;
                    }

                    String typeStr = getField(record, "Type", "TransactionType", "Txn_Type");
                    Transaction.TransactionType type = parseEnum(typeStr,
                            Transaction.TransactionType.class, Transaction.TransactionType.DEBIT);

                    Transaction txn = Transaction.builder()
                            .transactionId(txnId)
                            .referenceNumber(getField(record, "Reference", "ReferenceNumber", "Ref", "RefNo"))
                            .transactionDate(parseDate(getField(record, "Date", "TransactionDate", "Txn_Date")))
                            .description(getField(record, "Description", "Narration", "Remarks"))
                            .accountName(getField(record, "AccountName", "Account", "AccountNo"))
                            .amount(parseBigDecimal(getField(record, "Amount", "Debit", "Credit")))
                            .currency(getFieldOrDefault(record, "Currency", "INR"))
                            .type(type)
                            .source(Transaction.TransactionSource.BANK)
                            .status(Transaction.TransactionStatus.PENDING)
                            .category(getField(record, "Category", "Type2", "SubType"))
                            .balance(parseBigDecimal(getField(record, "Balance", "ClosingBalance")))
                            .build();

                    transactionRepository.save(txn);
                    successRows++;
                } catch (Exception e) {
                    errors.add("Row " + record.getRecordNumber() + ": " + e.getMessage());
                    skippedRows++;
                    log.warn("CSV parse error at row {}: {}", record.getRecordNumber(), e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new InvalidFileFormatException("Could not read CSV file: " + e.getMessage());
        }

        return CsvUploadResultDTO.builder()
                .fileName(file.getOriginalFilename())
                .totalRows(successRows + skippedRows)
                .successRows(successRows)
                .skippedRows(skippedRows)
                .errors(errors)
                .source("BANK")
                .build();
    }

    public CsvUploadResultDTO uploadLedgerCsv(MultipartFile file) {
        validateCsvFile(file);
        List<String> errors = new ArrayList<>();
        int successRows = 0, skippedRows = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
                        .withIgnoreHeaderCase().withTrim().parse(reader)) {

            for (CSVRecord record : parser) {
                try {
                    String entryId = getField(record, "EntryId", "Entry_Id", "ID", "Id");
                    if (entryId == null || entryId.isEmpty()) {
                        entryId = "LED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    }

                    if (ledgerEntryRepository.existsByEntryId(entryId)) {
                        skippedRows++;
                        continue;
                    }

                    String typeStr = getField(record, "Type", "EntryType", "Debit/Credit");
                    LedgerEntry.EntryType type = parseEnum(typeStr,
                            LedgerEntry.EntryType.class, LedgerEntry.EntryType.DEBIT);

                    LedgerEntry entry = LedgerEntry.builder()
                            .entryId(entryId)
                            .referenceNumber(getField(record, "Reference", "ReferenceNumber", "Ref"))
                            .entryDate(parseDate(getField(record, "Date", "EntryDate")))
                            .description(getField(record, "Description", "Narration", "Particulars"))
                            .accountName(getField(record, "AccountName", "Account", "LedgerAccount"))
                            .amount(parseBigDecimal(getField(record, "Amount", "Value")))
                            .currency(getFieldOrDefault(record, "Currency", "INR"))
                            .type(type)
                            .status(LedgerEntry.EntryStatus.UNMATCHED)
                            .build();

                    ledgerEntryRepository.save(entry);
                    successRows++;
                } catch (Exception e) {
                    errors.add("Row " + record.getRecordNumber() + ": " + e.getMessage());
                    skippedRows++;
                }
            }
        } catch (IOException e) {
            throw new InvalidFileFormatException("Could not read CSV file: " + e.getMessage());
        }

        return CsvUploadResultDTO.builder()
                .fileName(file.getOriginalFilename())
                .totalRows(successRows + skippedRows)
                .successRows(successRows)
                .skippedRows(skippedRows)
                .errors(errors)
                .source("LEDGER")
                .build();
    }

    // ---- Helpers ----

    private void validateCsvFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileFormatException("File is empty or missing");
        }
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            throw new InvalidFileFormatException("Only CSV files are supported. Got: " + filename);
        }
    }

    private String getField(CSVRecord record, String... headers) {
        for (String header : headers) {
            try {
                String val = record.get(header);
                if (val != null && !val.isEmpty())
                    return val.trim();
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private String getFieldOrDefault(CSVRecord record, String header, String defaultValue) {
        String val = getField(record, header);
        return (val != null && !val.isEmpty()) ? val : defaultValue;
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty())
            return LocalDate.now();
        for (DateTimeFormatter fmt : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(dateStr.trim(), fmt);
            } catch (DateTimeParseException ignored) {
            }
        }
        log.warn("Could not parse date: {}, using today", dateStr);
        return LocalDate.now();
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isEmpty())
            return BigDecimal.ZERO;
        try {
            return new BigDecimal(value.replaceAll("[,₹$]", "").trim());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private <E extends Enum<E>> E parseEnum(String value, Class<E> enumClass, E defaultValue) {
        if (value == null || value.isEmpty())
            return defaultValue;
        try {
            return Enum.valueOf(enumClass, value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }
}
