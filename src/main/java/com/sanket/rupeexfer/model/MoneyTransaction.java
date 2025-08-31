package com.sanket.rupeexfer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "money_transaction")
public class MoneyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "external_id", nullable = false, unique = true, updatable = false)
    private UUID externalId = UUID.randomUUID();

    @NotBlank
    @Column(name = "from_account_number", nullable = false, length = 50)
    private String fromAccountNumber;

    @NotBlank
    @Column(name = "to_account_number", nullable = false, length = 50)
    private String toAccountNumber;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status;

    @Column(length = 255)
    private String reference;

    @Column(name = "idempotency_key", unique = true, length = 255)
    private String idempotencyKey;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public MoneyTransaction() {
        // JPA requires a no-arg constructor
    }

    public MoneyTransaction(String fromAccountNumber, String toAccountNumber, 
                           BigDecimal amount, String reference, String idempotencyKey) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.reference = reference;
        this.idempotencyKey = idempotencyKey;
        this.status = TransactionStatus.SUCCESS; // Default status, can be changed
    }

    public enum TransactionStatus {
        SUCCESS,
        FAILED
    }
}
