package com.jocata.bankingmgntsystem.am.controller;

import com.jocata.bankingmgntsystem.am.form.AccountForm;
import com.jocata.bankingmgntsystem.am.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account/create")
    public ResponseEntity<AccountForm> createAccount(@RequestBody AccountForm accountForm) {

        AccountForm account = accountService.createAccount(accountForm);
        return new ResponseEntity<>(account, HttpStatus.CREATED);

    }

    @GetMapping("/account/{id}")
    public ResponseEntity<AccountForm> getAccount(@PathVariable Integer id) {

        AccountForm accountDetails = accountService.getAccountDetails(id);
        return new ResponseEntity<>(accountDetails, HttpStatus.OK);

    }

    @GetMapping("/account/balance/{id}")
    public ResponseEntity<Map<String, BigDecimal>> getAccountBalance(@PathVariable Integer id) {

        AccountForm accountDetails = accountService.getAccountDetails(id);
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("Available Balance", new BigDecimal(accountDetails.getBalance()));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/admin/account/status/{id}")
    public ResponseEntity<AccountForm> updateAccountStatus(@PathVariable Integer id,
                                                           @RequestBody AccountForm accountForm) {

        AccountForm account = accountService.updateAccountStatus(id, accountForm);
        return new ResponseEntity<>(account, HttpStatus.OK);

    }

    @GetMapping("/admin/accounts")
    public ResponseEntity<List<AccountForm>> getAllAccounts() {

        List<AccountForm> allAccounts = accountService.getAllAccounts();
        return new ResponseEntity<>(allAccounts, HttpStatus.OK);

    }


}
