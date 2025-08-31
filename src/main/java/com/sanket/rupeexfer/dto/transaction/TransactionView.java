package com.sanket.rupeexfer.dto.transaction;

import com.sanket.rupeexfer.model.MoneyTransaction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class TransactionView {
    private Long id;
    private UUID externalId;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String reference;
    private MoneyTransaction.TransactionStatus status;
    private Instant createdAt;
}
