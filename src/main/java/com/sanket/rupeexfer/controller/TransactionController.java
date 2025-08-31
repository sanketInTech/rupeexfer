package com.sanket.rupeexfer.controller;

import com.sanket.rupeexfer.dto.ApiResponse;
import com.sanket.rupeexfer.dto.transaction.FundTransferRequest;
import com.sanket.rupeexfer.dto.transaction.TransactionDto;
import com.sanket.rupeexfer.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TransactionDto>> transferFunds(
            @Valid @RequestBody FundTransferRequest request,
            Authentication authentication) {
        
        String username = authentication.getName();
        TransactionDto transaction = transactionService.transferFunds(
                username,
                request.getSourceAccountNumber(),
                request.getDestinationAccountNumber(),
                request.getAmount(),
                request.getDescription(),
                request.getIdempotencyKey()
        );
        
        return ResponseEntity.ok(ApiResponse.success("Funds transferred successfully", transaction));
    }

    @GetMapping("/{transactionId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TransactionDto>> getTransaction(
            @PathVariable String transactionId,
            Authentication authentication) {
        
        String username = authentication.getName();
        TransactionDto transaction = transactionService.getTransaction(transactionId, username);
        return ResponseEntity.ok(ApiResponse.success(transaction));
    }

    @GetMapping("/account/{accountNumber}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<TransactionDto>>> getAccountTransactions(
            @PathVariable String accountNumber,
            @PageableDefault(size = 10) Pageable pageable,
            Authentication authentication) {
        
        String username = authentication.getName();
        Page<TransactionDto> transactions = transactionService.getAccountTransactions(
                accountNumber, username, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/my-transactions")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<TransactionDto>>> getUserTransactions(
            @PageableDefault(size = 10) Pageable pageable,
            Authentication authentication) {
        
        String username = authentication.getName();
        Page<TransactionDto> transactions = transactionService.getUserTransactions(username, pageable);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }
}
