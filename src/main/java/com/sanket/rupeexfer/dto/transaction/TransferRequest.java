package com.sanket.rupeexfer.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequest(
    @NotBlank(message = "Source account number is required")
    String fromAccountNumber,
    
    @NotBlank(message = "Destination account number is required")
    String toAccountNumber,
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    BigDecimal amount,
    
    @Size(max = 255, message = "Reference must not exceed 255 characters")
    String reference,
    
    @NotBlank(message = "Idempotency key is required")
    String idempotencyKey
) {}
