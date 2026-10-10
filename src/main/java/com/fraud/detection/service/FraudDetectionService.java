
package com.fraud.detection.service;

import com.fraud.detection.model.Transaction;
import com.fraud.detection.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class FraudDetectionService {

    private final TransactionRepository repository;

    public FraudDetectionService(
            TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction analyze(Transaction tx) {

        if (tx.getAmount() == null
                || tx.getAmount().compareTo(
                        BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero");
        }

        if (tx.getMerchant() == null
                || tx.getMerchant().isBlank()
                || tx.getLocation() == null
                || tx.getLocation().isBlank()) {
            throw new IllegalArgumentException(
                    "Merchant and location are required");
        }

        if (tx.getTransactionHour() < 0
                || tx.getTransactionHour() > 23) {
            throw new IllegalArgumentException(
                    "Hour must be between 0 and 23");
        }

        boolean suspicious = false;
        StringBuilder reasons = new StringBuilder();

        if (tx.getAmount().compareTo(
                new BigDecimal("50000")) > 0) {
            suspicious = true;
            reasons.append("High transaction amount; ");
        }

        if (tx.getTransactionHour() <= 4) {
            suspicious = true;
            reasons.append("Unusual transaction hour; ");
        }

        tx.setFlaggedFraud(suspicious);

        tx.setFraudReason(
                suspicious
                        ? reasons.toString().trim()
                        : "No configured rule triggered");

        return repository.save(tx);
    }

    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    public List<Transaction> getFlaggedTransactions() {
        return repository.findAll().stream()
                .filter(Transaction::isFlaggedFraud)
                .toList();
    }

    public Transaction getTransaction(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Transaction not found: " + id));
    }
}