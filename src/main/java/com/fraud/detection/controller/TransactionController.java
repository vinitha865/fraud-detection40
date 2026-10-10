

package com.fraud.detection.controller;

import com.fraud.detection.model.Transaction;
import com.fraud.detection.repository.TransactionRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionRepository repository;

    public TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    // Get all saved transactions
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    // Check and save a transaction
    @PostMapping
    public Transaction addTransaction(
            @RequestBody Transaction transaction) {

        if (transaction.getAmount() == null) {
            throw new IllegalArgumentException(
                    "Transaction amount is required");
        }

        int hour = transaction.getTransactionHour();

        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException(
                    "Transaction hour must be between 0 and 23");
        }

        boolean fraud = false;
        String reason = "Normal transaction";

        if (transaction.getAmount().doubleValue() > 50000) {
            fraud = true;
            reason = "High transaction amount";
        } else if (hour < 6 || hour > 22) {
            fraud = true;
            reason = "Unusual transaction hour";
        }

        transaction.setFlaggedFraud(fraud);
        transaction.setFraudReason(reason);

        return repository.save(transaction);
    }
}