package com.sanket.rupeexfer.repository;

import com.sanket.rupeexfer.model.MoneyTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MoneyTransactionRepository extends JpaRepository<MoneyTransaction, Long> {
    
    Optional<MoneyTransaction> findByExternalId(UUID externalId);
    
    boolean existsByIdempotencyKey(String idempotencyKey);
    
    Optional<MoneyTransaction> findByIdempotencyKey(String idempotencyKey);
    
    @Query("SELECT t FROM MoneyTransaction t " +
           "WHERE t.fromAccountNumber = :accountNumber OR t.toAccountNumber = :accountNumber")
    Page<MoneyTransaction> findByAccountNumber(
        @Param("accountNumber") String accountNumber, 
        Pageable pageable
    );
    
    @Query("SELECT t FROM MoneyTransaction t " +
           "WHERE (t.fromAccountNumber = :accountNumber OR t.toAccountNumber = :accountNumber) " +
           "AND t.createdAt BETWEEN :startDate AND :endDate")
    Page<MoneyTransaction> findByAccountNumberAndDateRange(
        @Param("accountNumber") String accountNumber,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        Pageable pageable
    );
}
