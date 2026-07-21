package com.techwing.ledger.repository;

import com.techwing.ledger.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySource(Transaction.TransactionSource source);

    Page<Transaction> findBySource(Transaction.TransactionSource source, Pageable pageable);

    List<Transaction> findByStatus(Transaction.TransactionStatus status);

    List<Transaction> findBySourceAndStatus(Transaction.TransactionSource source, Transaction.TransactionStatus status);

    Page<Transaction> findBySourceAndTransactionDateBetween(
            Transaction.TransactionSource source,
            LocalDate from,
            LocalDate to,
            Pageable pageable);

    boolean existsByTransactionId(String transactionId);

    long countBySource(Transaction.TransactionSource source);

    long countByStatus(Transaction.TransactionStatus status);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = 'CREDIT' AND t.source = :source")
    Double sumCreditsBySource(Transaction.TransactionSource source);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = 'DEBIT' AND t.source = :source")
    Double sumDebitsBySource(Transaction.TransactionSource source);
}
