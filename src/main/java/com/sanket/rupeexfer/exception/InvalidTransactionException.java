package com.sanket.rupeexfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTransactionException extends ApiException {
    public InvalidTransactionException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "INVALID_TRANSACTION");
    }

    public InvalidTransactionException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST, "INVALID_TRANSACTION");
    }
}
