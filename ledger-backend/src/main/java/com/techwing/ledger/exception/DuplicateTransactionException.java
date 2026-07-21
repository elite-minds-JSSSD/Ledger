package com.techwing.ledger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateTransactionException extends LedgerException {
    public DuplicateTransactionException(String transactionId) {
        super("Transaction already exists with ID: " + transactionId);
    }
}
