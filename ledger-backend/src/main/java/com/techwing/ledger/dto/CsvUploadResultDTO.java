package com.techwing.ledger.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CsvUploadResultDTO {
    private String fileName;
    private int totalRows;
    private int successRows;
    private int skippedRows;
    private List<String> errors;
    private String source; // BANK or LEDGER
}
