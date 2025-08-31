package com.sanket.rupeexfer.service.impl;

import com.sanket.rupeexfer.dto.transaction.TransactionResponse;
import com.sanket.rupeexfer.dto.transaction.TransferRequest;
import com.sanket.rupeexfer.exception.*;
import com.sanket.rupeexfer.model.Account;
import com.sanket.rupeexfer.model.MoneyTransaction;
import com.sanket.rupeexfer.model.TransactionStatus;
import com.sanket.rupeexfer.repository.AccountRepository;
import com.sanket.rupeexfer.repository.MoneyTransactionRepository;
import com.sanket.rupeexfer.service.TransactionService;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private static final int MAX_RETRY_ATTEMPTS = 3;
    
    private final MoneyTransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        rollbackFor = {Exception.class}
    )
    @Retryable(
        value = {OptimisticLockException.class, ObjectOptimisticLockingFailureException.class},
        maxAttempts = 3
    )
    public TransactionDto transferFunds(
            String username,
            String sourceAccountNumber,
            String destinationAccountNumber,
            BigDecimal amount,
            String description,
            String idempotencyKey) {
        
        // Validate accounts and ownership
        Account sourceAccount = getAndValidateAccount(sourceAccountNumber, username);
        Account destinationAccount = getAccount(destinationAccountNumber);
        
        // Check for self-transfer
        if (sourceAccountNumber.equals(destinationAccountNumber)) {
            throw new InvalidTransactionException("Source and destination accounts cannot be the same");
        }
        
        // Check for idempotency
        if (idempotencyKey != null) {
            transactionRepository.findDuplicateTransfer(
                    idempotencyKey, sourceAccount, destinationAccount, amount)
                .ifPresent(transaction -> {
                    throw new InvalidTransactionException("Duplicate transaction detected");
                });
        }
        
        // Check sufficient balance with pessimistic locking
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in the source account");
        }
        
        try {
            // Perform the transfer
            sourceAccount.withdraw(amount);
            destinationAccount.deposit(amount);
            
            // Save the transaction
            Transaction transaction = Transaction.createTransfer(
                    sourceAccount, destinationAccount, amount, description, idempotencyKey);
            
            transaction = transactionRepository.save(transaction);
            
            // Save accounts to update balances
            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);
            
            return transactionMapper.toDto(transaction);
            
        } catch (Exception e) {
            log.error("Error during fund transfer: {}", e.getMessage());
            throw new InvalidTransactionException("Failed to process transfer: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDto getTransaction(String transactionId, String username) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));
        
        // Verify the user has access to this transaction
        if (!isTransactionVisibleToUser(transaction, username)) {
            throw new SecurityException("You don't have permission to view this transaction");
        }
        
        return transactionMapper.toDto(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> getAccountTransactions(String accountNumber, String username, Pageable pageable) {
        // This will verify ownership
        accountService.validateAccountOwnership(accountNumber, username);
        
        return transactionRepository.findAllByAccountNumber(accountNumber, pageable)
                .map(transactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> getUserTransactions(String username, Pageable pageable) {
        return transactionRepository.findByUsername(username, pageable)
                .map(transactionMapper::toDto);
    }

    private Account getAndValidateAccount(String accountNumber, String username) {
        Account account = getAccount(accountNumber);
        accountService.validateAccountOwnership(accountNumber, username);
        return account;
    }
    
    private Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumberWithLock(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
    }
    
    private boolean isTransactionVisibleToUser(Transaction transaction, String username) {
        return (transaction.getSourceAccount() != null && 
                    transaction.getSourceAccount().getUser().getUsername().equals(username)) ||
               (transaction.getDestinationAccount() != null && 
                    transaction.getDestinationAccount().getUser().getUsername().equals(username));
    }
}
