package com.sanket.rupeexfer.controller;

import com.sanket.rupeexfer.dto.ApiResponse;
import com.sanket.rupeexfer.dto.PageResponse;
import com.sanket.rupeexfer.dto.account.AccountCreateRequest;
import com.sanket.rupeexfer.dto.account.AccountResponse;
import com.sanket.rupeexfer.dto.transaction.TransactionView;
import com.sanket.rupeexfer.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST controller for managing bank accounts.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AccountResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        log.info("Creating new account for owner: {}", request.getOwnerName());
        AccountResponse response = accountService.createAccount(request);
        return ApiResponse.success("Account created successfully", response);
    }

    @GetMapping("/{accountNumber}/balance")
    public ApiResponse<BigDecimal> getBalance(
            @PathVariable @NotBlank String accountNumber) {
        log.debug("Fetching balance for account: {}", accountNumber);
        BigDecimal balance = accountService.getBalance(accountNumber);
        return ApiResponse.success("Balance retrieved successfully", balance);
    }

    @GetMapping("/{accountNumber}/transactions")
    public ApiResponse<PageResponse<TransactionView>> getTransactionHistory(
            @PathVariable @NotBlank String accountNumber,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Fetching transaction history for account: {}", accountNumber);
        PageResponse<TransactionView> transactions = accountService.getTransactions(accountNumber, pageable);
        return ApiResponse.success("Transaction history retrieved successfully", transactions);
    }
}
