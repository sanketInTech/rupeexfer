package com.sanket.rupeexfer.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountCreateRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    
    @NotBlank(message = "Owner name is required")
    private String ownerName;
    
    @PositiveOrZero(message = "Initial balance must be positive or zero")
    private BigDecimal initialBalance = BigDecimal.ZERO;
}
