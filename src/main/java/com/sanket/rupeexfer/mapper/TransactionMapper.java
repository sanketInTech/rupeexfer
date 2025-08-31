package com.sanket.rupeexfer.mapper;

import com.sanket.rupeexfer.dto.transaction.TransferRequest;
import com.sanket.rupeexfer.dto.transaction.TransferResponse;
import com.sanket.rupeexfer.model.MoneyTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "externalId", expression = "java(generateExternalId())")
    @Mapping(target = "fromAccountNumber", source = "fromAccountNumber")
    @Mapping(target = "toAccountNumber", source = "toAccountNumber")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "idempotencyKey", source = "idempotencyKey")
    MoneyTransaction toEntity(TransferRequest request);

    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "timestamp", source = "createdAt")
    TransferResponse toResponse(MoneyTransaction transaction);

    default UUID generateExternalId() {
        return UUID.randomUUID();
    }
}
