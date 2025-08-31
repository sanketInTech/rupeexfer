package com.sanket.rupeexfer.service.impl;

import com.sanket.rupeexfer.dto.transaction.TransferRequest;
import com.sanket.rupeexfer.dto.transaction.TransferResponse;
import com.sanket.rupeexfer.exception.InsufficientFundsException;
import com.sanket.rupeexfer.exception.NotFoundException;
import com.sanket.rupeexfer.exception.ValidationException;
import com.sanket.rupeexfer.mapper.TransactionMapper;
import com.sanket.rupeexfer.model.Account;
import com.sanket.rupeexfer.model.MoneyTransaction;
import com.sanket.rupeexfer.repository.AccountRepository;
import com.sanket.rupeexfer.repository.MoneyTransactionRepository;
import com.sanket.rupeexfer.service.TransferService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final MoneyTransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    public TransferResponse transferFunds(TransferRequest request, String idempotencyKey) {
        // Check for existing transaction with idempotency key
        if (idempotencyKey != null) {
            Optional<MoneyTransaction> existingTx = transactionRepository.findByIdempotencyKey(idempotencyKey);
            if (existingTx.isPresent()) {
                return transactionMapper.toResponse(existingTx.get());
            }
        }

        // Load accounts
        Account fromAccount = accountRepository.findByAccountNumber(request.fromAccountNumber())
                .orElseThrow(() -> NotFoundException.account(request.fromAccountNumber()));
        
        Account toAccount = accountRepository.findByAccountNumber(request.toAccountNumber())
                .orElseThrow(() -> NotFoundException.account(request.toAccountNumber()));

        // Validate transfer
        if (fromAccount.equals(toAccount)) {
            throw new ValidationException("Source and destination accounts must be different");
        }
        
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Transfer amount must be positive");
        }

        if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds for transfer");
        }

        // Perform transfer
        fromAccount.withdraw(request.amount());
        toAccount.deposit(request.amount());

        // Save accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Create and save transaction
        MoneyTransaction transaction = new MoneyTransaction();
        transaction.setFromAccountNumber(request.fromAccountNumber());
        transaction.setToAccountNumber(request.toAccountNumber());
        transaction.setAmount(request.amount());
        transaction.setReference(request.reference());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setIdempotencyKey(idempotencyKey);
        MoneyTransaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    public PageResponse<TransferResponse> getTransferHistory(String accountNumber, Pageable pageable) {
        Page<MoneyTransaction> transactions = transactionRepository
                .findByFromAccountNumberOrToAccountNumber(accountNumber, accountNumber, pageable);
        
        return PageResponse.of(transactions.map(transactionMapper::toResponse));
    }
}
