package com.sanket.rupeexfer.mapper;

import com.sanket.rupeexfer.dto.account.AccountCreateRequest;
import com.sanket.rupeexfer.dto.account.AccountResponse;
import com.sanket.rupeexfer.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for converting between Account entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    /**
     * Converts an AccountCreateRequest DTO to an Account entity.
     * Ignores id, version, and timestamps as they are managed by the persistence layer.
     * 
     * @param request the account creation request DTO
     * @return the mapped Account entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "balance", source = "initialBalance")
    @Mapping(target = "accountNumber", source = "accountNumber")
    @Mapping(target = "ownerName", source = "ownerName")
    Account toEntity(AccountCreateRequest request);

    /**
     * Converts an Account entity to an AccountResponse DTO.
     * Formats the balance to 2 decimal places.
     * 
     * @param account the account entity
     * @return the mapped AccountResponse DTO
     */
    @Mapping(target = "balance", source = "balance", numberFormat = "#.00")
    AccountResponse toResponse(Account account);
}
