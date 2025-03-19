package com.jocata.bankingmgntsystem.am.service;

import com.jocata.bankingmgntsystem.am.form.AccountForm;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountForm createAccount(AccountForm accountForm);

    AccountForm getAccountDetails(Integer id);

    AccountForm updateAccountStatus(Integer accountId,AccountForm accountForm);

    List<AccountForm> getAllAccounts();

}
