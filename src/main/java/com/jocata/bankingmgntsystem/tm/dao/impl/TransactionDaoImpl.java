package com.jocata.bankingmgntsystem.tm.dao.impl;

import com.jocata.bankingmgntsystem.config.HibernateConfig;
import com.jocata.bankingmgntsystem.tm.dao.TransactionDao;
import com.jocata.bankingmgntsystem.tm.entity.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionDaoImpl implements TransactionDao {

    private final HibernateConfig hibernateConfig;

    public TransactionDaoImpl(HibernateConfig hibernateConfig) {
        this.hibernateConfig = hibernateConfig;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return hibernateConfig.saveEntity(transaction);
    }

    @Override
    public Transaction getTransaction(Integer id) {
        return hibernateConfig.findEntityById(Transaction.class, id);
    }

    @Override
    public List<Transaction> getTransactionByAccountId(Integer id) {
        return hibernateConfig.findEntitiesByCriteria(Transaction.class, "account.accountId", id);
    }

    @Override
    public List<Transaction> getAllTransaction() {
        return hibernateConfig.loadEntitiesByCriteria(Transaction.class);
    }

    @Override
    public List<Transaction> getTransactionsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        return hibernateConfig.getEntitiesBetweenDates(Transaction.class, "timestamp", startTimestamp, endTimestamp);
    }


    @Override
    public Long countWithdrawalsInCurrentMonth(Integer accountId) {
        Map<String, Object> criteriaMap = Map.of(
                "accountId", accountId,
                "type", "WITHDRAWAL"
        );

        List<Transaction> transactions = hibernateConfig.findEntitiesByMultipleCriteria(Transaction.class, criteriaMap);

        if (transactions == null) {
            transactions = Collections.emptyList();
        }

        return transactions.stream()
                .filter(tx -> tx.getTimestamp().getMonth() == java.time.LocalDate.now().getMonthValue() - 1)
                .count();
    }

}
