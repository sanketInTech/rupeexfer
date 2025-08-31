package com.sanket.rupeexfer.service.impl;

import com.sanket.rupeexfer.dto.account.AccountCreateRequest;
import com.sanket.rupeexfer.dto.account.AccountResponse;
import com.sanket.rupeexfer.dto.PageResponse;
import com.sanket.rupeexfer.dto.transaction.TransactionView;
import com.sanket.rupeexfer.exception.NotFoundException;
import com.sanket.rupeexfer.exception.ValidationException;
import com.sanket.rupeexfer.mapper.AccountMapper;
import com.sanket.rupeexfer.model.Account;
import com.sanket.rupeexfer.model.MoneyTransaction;
import com.sanket.rupeexfer.repository.AccountRepository;
import com.sanket.rupeexfer.repository.MoneyTransactionRepository;
import com.sanket.rupeexfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link AccountService} providing account-related operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final MoneyTransactionRepository transactionRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountCreateRequest request) {
        log.info("Creating new account with number: {}", request.getAccountNumber());
        
        if (accountRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new ValidationException("Account number already exists: " + request.getAccountNumber());
        }
        
        Account account = accountMapper.toEntity(request);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toResponse(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(String accountNumber) {
        log.debug("Getting balance for account: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
                .map(Account::getBalance)
                .orElseThrow(() -> new NotFoundException("Account not found with number: " + accountNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TransactionView> getTransactions(String accountNumber, Pageable pageable) {
        log.debug("Getting transactions for account: {}", accountNumber);
        
        if (!accountRepository.existsByAccountNumber(accountNumber)) {
            throw new NotFoundException("Account not found with number: " + accountNumber);
        }
        
        Page<MoneyTransaction> transactionPage = transactionRepository.findByAccountNumber(accountNumber, pageable);
                
        Page<TransactionView> transactions = transactionPage.map(transaction -> {
            TransactionView view = new TransactionView();
            view.setId(transaction.getId());
            view.setExternalId(transaction.getExternalId());
            view.setFromAccount(transaction.getFromAccountNumber());
            view.setToAccount(transaction.getToAccountNumber());
            view.setAmount(transaction.getAmount());
            view.setStatus(transaction.getStatus());
            view.setCreatedAt(transaction.getCreatedAt());
            return view;
        });
                
        return PageResponse.of(transactions);
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            // Generate a 12-digit account number
            long number = (long) (Math.random() * 9_000_000_000L) + 1_000_000_000L;
            accountNumber = String.valueOf(number);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        
        return accountNumber;
    }
}
