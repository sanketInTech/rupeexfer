package com.sanket.rupeexfer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @Column(nullable = false, unique = true, length = 36)
    private String transactionId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;

    @Column(length = 255)
    @Size(max = 255)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime transactionDate;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(length = 50)
    private String referenceNumber;

    @Column(length = 36)
    private String idempotencyKey;

    @PrePersist
    protected void onCreate() {
        if (this.transactionId == null) {
            this.transactionId = UUID.randomUUID().toString();
        }
        if (this.transactionDate == null) {
            this.transactionDate = LocalDateTime.now();
        }
    }

    public Transaction() {
        // Default constructor for JPA
    }

    public static Transaction createDeposit(Account account, BigDecimal amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setDestinationAccount(account);
        transaction.setDescription(description);
        transaction.setStatus("COMPLETED");
        return transaction;
    }

    public static Transaction createWithdrawal(Account account, BigDecimal amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setSourceAccount(account);
        transaction.setDescription(description);
        transaction.setStatus("COMPLETED");
        return transaction;
    }

    public static Transaction createTransfer(Account source, Account destination, BigDecimal amount, String description, String idempotencyKey) {
        Transaction debit = new Transaction();
        debit.setType(TransactionType.TRANSFER_DEBIT);
        debit.setAmount(amount);
        debit.setSourceAccount(source);
        debit.setDestinationAccount(destination);
        debit.setDescription(description);
        debit.setStatus("COMPLETED");
        debit.setIdempotencyKey(idempotencyKey);
        
        return debit;
    }
}
