package com.jocata.bankingmgntsystem.tm.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jocata.bankingmgntsystem.am.dao.AccountDao;
import com.jocata.bankingmgntsystem.am.entity.AccountDetails;
import com.jocata.bankingmgntsystem.tm.dao.TransactionDao;
import com.jocata.bankingmgntsystem.tm.entity.Transaction;
import com.jocata.bankingmgntsystem.tm.entity.TransactionType;
import com.jocata.bankingmgntsystem.tm.form.TransactionForm;
import com.jocata.bankingmgntsystem.tm.service.TransactionService;
import com.jocata.bankingmgntsystem.um.dao.UMDao;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;
    private final AccountDao accountDao;
    private final UMDao umDao;

    public TransactionServiceImpl(TransactionDao transactionDao, AccountDao accountDao, UMDao umDao) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
        this.umDao = umDao;
    }


    @Override
    public void deposit(TransactionForm transactionForm) {

        AccountDetails account = accountDao.getAccount(Integer.valueOf(transactionForm.getAccountId()));

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAccount(account);
        transaction.setAmount(new BigDecimal(transactionForm.getAmount()));
        transaction.setTimestamp(Timestamp.from(Instant.now()));

        transactionDao.createTransaction(transaction);

        account.setBalance(account.getBalance().add(new BigDecimal(transactionForm.getAmount())));
        accountDao.updateAccount(account);

    }

    @Override
    public void withdraw(TransactionForm transactionForm) {

        AccountDetails account = accountDao.getAccount(Integer.valueOf(transactionForm.getAccountId()));

        BigDecimal charge = calculateWithdrawalCharge(transactionForm, account);
        BigDecimal totalDeduction = new BigDecimal(transactionForm.getAmount()).add(charge);

        if (account.getBalance().compareTo(totalDeduction) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setAccount(account);
        transaction.setAmount(new BigDecimal(transactionForm.getAmount()));
        transaction.setTimestamp(Timestamp.from(Instant.now()));
        transaction.setCharge(charge);

        transactionDao.createTransaction(transaction);

        account.setBalance(account.getBalance().subtract(totalDeduction));
        accountDao.updateAccount(account);
    }

    @Override
    public List<TransactionForm> getTransactions() {

        List<Transaction> allTransaction = transactionDao.getAllTransaction();
        List<TransactionForm> transactionForms = new ArrayList<>();
        for (Transaction transaction : allTransaction) {
            transactionForms.add(transactionEntityToForm(transaction));
        }
        return transactionForms;

    }

    @Override
    public List<TransactionForm> getAllTransactionsByAccountId(Integer accountId) {

        List<Transaction> transactionByAccountId = transactionDao.getTransactionByAccountId(accountId);
        List<TransactionForm> transactionForms = new ArrayList<>();
        for (Transaction transaction : transactionByAccountId) {
            transactionForms.add(transactionEntityToForm(transaction));
        }
        return transactionForms;

    }


    @Override
    public String generateTransactionPdf(LocalDateTime startDate, LocalDateTime endDate) {

        List<Transaction> transactions = transactionDao.getTransactionsBetweenDates(startDate, endDate);

        String filePath = "transactions_report.pdf";
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            document.add(new Paragraph("Transaction Report"));
            document.add(new Paragraph("From: " + startDate + " To: " + endDate));
            document.add(new Paragraph("\n"));

            for (Transaction txn : transactions) {
                document.add(new Paragraph(
                        "ID: " + txn.getId() +
                                " | Type: " + txn.getType() +
                                " | Amount: " + txn.getAmount() +
                                " | Charge: " + txn.getCharge() +
                                " | Account Number: " + txn.getAccount().getAccountNumber() +
                                " | Timestamp: " + txn.getTimestamp()
                ));
            }

            System.out.println("PDF Generated: " + filePath);
            return filePath;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return "Document not generated";
    }


    private TransactionForm transactionEntityToForm(Transaction transaction) {

        TransactionForm response = new TransactionForm();
        response.setType(transaction.getType().toString());
        response.setAccountId(String.valueOf(transaction.getAccount().getAccountId()));
        response.setAmount(String.valueOf(transaction.getAmount()));
        response.setCharge(String.valueOf(transaction.getCharge()));
        return response;

    }

    private BigDecimal calculateWithdrawalCharge(TransactionForm transactionForm, AccountDetails account) {

        BigDecimal charge = BigDecimal.ZERO;
        BigDecimal amount = new BigDecimal(transactionForm.getAmount());

        if (amount.compareTo(new BigDecimal("10000")) > 0) {
            charge = charge.add(amount.multiply(new BigDecimal("0.01")));
        }

        if (!transactionForm.getBankName().equals("ABC")) {
            charge = charge.add(new BigDecimal("2"));
        }

        Long withdrawalCount = transactionDao.countWithdrawalsInCurrentMonth(account.getAccountId());
        if (withdrawalCount >= 4) {
            charge = charge.add(new BigDecimal("20"));
        }

        charge = charge.max(new BigDecimal("10")).min(new BigDecimal("1000"));

        if (!getUserNationality(account.getUser().getId()).equalsIgnoreCase("Indian")) {
            charge = charge.add(charge.multiply(new BigDecimal("0.05")));
        }

        return charge;
    }

    private String getUserNationality(Integer userId) {
        return umDao.getUser(userId).getNationality();
    }


}
