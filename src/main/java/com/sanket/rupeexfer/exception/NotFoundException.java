package com.sanket.rupeexfer.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException account(String accountNumber) {
        return new NotFoundException("Account not found: " + accountNumber);
    }
}
