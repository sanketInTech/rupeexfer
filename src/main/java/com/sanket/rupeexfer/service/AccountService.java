package com.sanket.rupeexfer.service;

import com.sanket.rupeexfer.dto.PageResponse;
import com.sanket.rupeexfer.dto.account.AccountCreateRequest;
import com.sanket.rupeexfer.dto.account.AccountResponse;
import com.sanket.rupeexfer.dto.transaction.TransactionView;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * Service interface for account operations.
 */
public interface AccountService {
    
    /**
     * Creates a new bank account.
     *
     * @param request the account creation request
     * @return the created account response
     * @throws com.sanket.rupeexfer.exception.ValidationException if account number already exists
     */
    AccountResponse createAccount(AccountCreateRequest request);
    
    /**
     * Retrieves the current balance of an account.
     *
     * @param accountNumber the account number
     * @return the current balance
     * @throws com.sanket.rupeexfer.exception.NotFoundException if account not found
     */
    BigDecimal getBalance(String accountNumber);
    
    /**
     * Retrieves the transaction history for an account.
     *
     * @param accountNumber the account number
     * @param pageable     pagination information
     * @return paginated list of transactions
     * @throws com.sanket.rupeexfer.exception.NotFoundException if account not found
     */
    PageResponse<TransactionView> getTransactions(String accountNumber, Pageable pageable);
}
