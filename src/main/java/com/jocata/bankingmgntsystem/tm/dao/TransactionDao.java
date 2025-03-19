package com.jocata.bankingmgntsystem.tm.dao;

import com.jocata.bankingmgntsystem.tm.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionDao {

    Transaction createTransaction(Transaction transaction);

    Transaction getTransaction(Integer id);

    List<Transaction> getTransactionByAccountId(Integer id);

    List<Transaction> getAllTransaction();

    List<Transaction> getTransactionsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    Long countWithdrawalsInCurrentMonth(Integer accountId);

}
