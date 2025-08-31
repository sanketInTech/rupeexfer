package com.sanket.rupeexfer.dto.transaction;

import com.sanket.rupeexfer.model.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {
    private Long transactionId;
    private UUID externalId;
    private TransactionStatus status;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private String reference;
    private Instant timestamp;
}
