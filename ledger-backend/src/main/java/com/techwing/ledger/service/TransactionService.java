package com.techwing.ledger.service;

import com.techwing.ledger.exception.ResourceNotFoundException;
import com.techwing.ledger.model.Transaction;
import com.techwing.ledger.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Page<Transaction> getAll(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public Transaction getById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
    }

    public List<Transaction> getBySource(Transaction.TransactionSource source) {
        return transactionRepository.findBySource(source);
    }

    public Page<Transaction> getBySourcePaged(Transaction.TransactionSource source, Pageable pageable) {
        return transactionRepository.findBySource(source, pageable);
    }

    public List<Transaction> getByStatus(Transaction.TransactionStatus status) {
        return transactionRepository.findByStatus(status);
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void deleteById(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction", "id", id);
        }
        transactionRepository.deleteById(id);
    }

    public long countBySource(Transaction.TransactionSource source) {
        return transactionRepository.countBySource(source);
    }

    public long countByStatus(Transaction.TransactionStatus status) {
        return transactionRepository.countByStatus(status);
    }
}
