package com.sanket.rupeexfer.service;

import com.sanket.rupeexfer.dto.transaction.TransferRequest;
import com.sanket.rupeexfer.dto.transaction.TransferResponse;
import org.springframework.data.domain.Pageable;
import com.sanket.rupeexfer.dto.PageResponse;

public interface TransferService {
    TransferResponse transferFunds(TransferRequest request, String idempotencyKey);
    PageResponse<TransferResponse> getTransferHistory(String accountNumber, Pageable pageable);
}
