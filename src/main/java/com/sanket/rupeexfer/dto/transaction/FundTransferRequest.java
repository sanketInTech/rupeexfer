package com.sanket.rupeexfer.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FundTransferRequest {
    
    @NotBlank(message = "Source account number is required")
    private String sourceAccountNumber;
    
    @NotBlank(message = "Destination account number is required")
    private String destinationAccountNumber;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;
    
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
    
    @NotBlank(message = "Idempotency key is required for safe retries")
    private String idempotencyKey;
}
