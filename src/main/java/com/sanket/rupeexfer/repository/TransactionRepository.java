package com.sanket.rupeexfer.repository;

import com.sanket.rupeexfer.model.Account;
import com.sanket.rupeexfer.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    Optional<Transaction> findByTransactionId(String transactionId);
    
    boolean existsByIdempotencyKey(String idempotencyKey);
    
    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount = :account OR t.destinationAccount = :account")
    List<Transaction> findAllByAccount(@Param("account") Account account);
    
    @Query("SELECT t FROM Transaction t WHERE " +
           "(t.sourceAccount = :account OR t.destinationAccount = :account) AND " +
           "t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByAccountAndDateRange(
        @Param("account") Account account,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT t FROM Transaction t WHERE " +
           "(t.sourceAccount.user = :user OR t.destinationAccount.user = :user)")
    Page<Transaction> findUserTransactions(
        @Param("user") User user,
        Pageable pageable
    );
    
    @Query("SELECT t FROM Transaction t WHERE " +
           "t.idempotencyKey = :idempotencyKey AND " +
           "t.sourceAccount = :sourceAccount AND " +
           "t.destinationAccount = :destinationAccount AND " +
           "t.amount = :amount")
    Optional<Transaction> findDuplicateTransfer(
        @Param("idempotencyKey") String idempotencyKey,
        @Param("sourceAccount") Account sourceAccount,
        @Param("destinationAccount") Account destinationAccount,
        @Param("amount") BigDecimal amount
    );
}
