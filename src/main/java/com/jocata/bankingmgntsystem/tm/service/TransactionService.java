package com.jocata.bankingmgntsystem.tm.service;

import com.jocata.bankingmgntsystem.tm.form.TransactionForm;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    void deposit(TransactionForm transactionForm);

    void withdraw(TransactionForm transactionForm);

    List<TransactionForm> getTransactions();

    List<TransactionForm> getAllTransactionsByAccountId(Integer accountId);

    String generateTransactionPdf(LocalDateTime startDate, LocalDateTime endDate);
}
