package com.sanket.rupeexfer.dto.transaction;

import com.sanket.rupeexfer.model.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private String transactionId;
    private TransactionType type;
    private BigDecimal amount;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String description;
    private LocalDateTime transactionDate;
    private String status;
    private String referenceNumber;
}
