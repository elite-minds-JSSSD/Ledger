package com.techwing.ledger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFileFormatException extends LedgerException {
    public InvalidFileFormatException(String message) {
        super(message);
    }
}
