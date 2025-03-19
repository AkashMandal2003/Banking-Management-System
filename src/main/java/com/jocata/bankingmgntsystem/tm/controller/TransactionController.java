package com.jocata.bankingmgntsystem.tm.controller;

import com.jocata.bankingmgntsystem.tm.form.TransactionForm;
import com.jocata.bankingmgntsystem.tm.form.TransactionTimePeriod;
import com.jocata.bankingmgntsystem.tm.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<String> depositMoney(@RequestBody TransactionForm transactionForm) {

        transactionService.deposit(transactionForm);
        return ResponseEntity.ok("Deposit successful.");

    }

    @PostMapping("/account/withdraw")
    public ResponseEntity<String> withdrawMoney(@RequestBody TransactionForm transactionForm) {

        transactionService.withdraw(transactionForm);
        return ResponseEntity.ok("Withdrawal successful.");

    }

    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<List<TransactionForm>> getTransactionHistory(@PathVariable Integer accountId) {

        return ResponseEntity.ok(transactionService.getAllTransactionsByAccountId(accountId));

    }

    @GetMapping("/admin/transactions")
    public ResponseEntity<List<TransactionForm>> getAllTransactions() {

        return ResponseEntity.ok(transactionService.getTransactions());

    }

    @PostMapping("/account/transactions/download")
    public ResponseEntity<Resource> downloadTransactionPdf(@RequestBody TransactionTimePeriod transactionTimePeriod) {

        LocalDateTime startDate = transactionTimePeriod.getStartDate();
        LocalDateTime endDate = transactionTimePeriod.getEndDate();

        String filePath = transactionService.generateTransactionPdf(startDate, endDate);

        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions_report.pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

}
