package com.sanket.rupeexfer.dto.transaction;

import com.sanket.rupeexfer.model.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
    UUID transactionId,
    String fromAccountNumber,
    String toAccountNumber,
    BigDecimal amount,
    TransactionStatus status,
    String reference,
    Instant timestamp
) {
    public TransactionResponse withStatus(TransactionStatus status) {
        return new TransactionResponse(
            this.transactionId,
            this.fromAccountNumber,
            this.toAccountNumber,
            this.amount,
            status,
            this.reference,
            this.timestamp
        );
    }
}
