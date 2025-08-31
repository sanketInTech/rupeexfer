package com.sanket.rupeexfer.service;

import com.sanket.rupeexfer.dto.transaction.TransactionResponse;
import com.sanket.rupeexfer.dto.transaction.TransferRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.UUID;

public interface TransactionService {
    /**
     * Transfer funds between two accounts
     * @param request The transfer request containing transaction details
     * @return TransactionResponse with transaction details
     */
    TransactionResponse transferFunds(TransferRequest request);
    
    /**
     * Get transaction details by external ID
     * @param externalId The external transaction ID
     * @return Transaction details if found
     */
    TransactionResponse getTransaction(UUID externalId);
    
    /**
     * Get transaction history for an account
     * @param accountNumber The account number
     * @param pageable Pagination information
     * @return Page of transaction history
     */
    Page<TransactionResponse> getAccountTransactions(String accountNumber, Pageable pageable);
    
    /**
     * Get transaction history for an account within a date range
     * @param accountNumber The account number
     * @param startDate Start date (inclusive)
     * @param endDate End date (inclusive)
     * @param pageable Pagination information
     * @return Page of transaction history
     */
    Page<TransactionResponse> getAccountTransactionsByDateRange(
        String accountNumber, 
        Instant startDate, 
        Instant endDate, 
        Pageable pageable
    );
}
