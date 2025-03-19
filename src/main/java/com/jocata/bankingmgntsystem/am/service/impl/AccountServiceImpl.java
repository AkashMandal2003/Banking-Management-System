package com.jocata.bankingmgntsystem.am.service.impl;

import com.jocata.bankingmgntsystem.am.dao.AccountDao;
import com.jocata.bankingmgntsystem.am.entity.AccountDetails;
import com.jocata.bankingmgntsystem.am.entity.AccountStatus;
import com.jocata.bankingmgntsystem.am.entity.AccountType;
import com.jocata.bankingmgntsystem.am.form.AccountForm;
import com.jocata.bankingmgntsystem.am.service.AccountService;
import com.jocata.bankingmgntsystem.um.dao.UMDao;
import com.jocata.bankingmgntsystem.um.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;
    private final UMDao umDao;

    public AccountServiceImpl(AccountDao accountDao, UMDao umDao) {
        this.accountDao = accountDao;
        this.umDao = umDao;
    }

    @Override
    public AccountForm createAccount(AccountForm accountForm) {

        AccountDetails accountDetails = accountFormToAccountDetails(accountForm);
        AccountDetails account = accountDao.createAccount(accountDetails);
        return accountDetailsToAccountForm(account);

    }

    @Override
    public AccountForm getAccountDetails(Integer id) {

        AccountDetails account = accountDao.getAccount(id);
        return accountDetailsToAccountForm(account);

    }

    @Override
    public AccountForm updateAccountStatus(Integer accountId,AccountForm accountForm) {

        AccountDetails account = accountDao.getAccount(accountId);
        account.setAccountStatus(AccountStatus.valueOf(accountForm.getStatus()));
        AccountDetails accountDetails = accountDao.updateAccount(account);
        return accountDetailsToAccountForm(accountDetails);


    }

    @Override
    public List<AccountForm> getAllAccounts() {

        List<AccountDetails> allAccounts = accountDao.getAllAccounts();
        List<AccountForm> accountForms = new ArrayList<AccountForm>();
        for (AccountDetails account : allAccounts) {
            accountForms.add(accountDetailsToAccountForm(account));
        }
        return accountForms;

    }

    private AccountDetails accountFormToAccountDetails(AccountForm accountForm) {

        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setAccountNumber(accountNoGenerator());
        accountDetails.setAccountStatus(AccountStatus.ACTIVE);
        accountDetails.setBalance(BigDecimal.ZERO);
        accountDetails.setAccountType(AccountType.valueOf(accountForm.getAccountType()));
        accountDetails.setUser(getUserForAccount(accountForm));

        return accountDetails;

    }

    private AccountForm accountDetailsToAccountForm(AccountDetails accountDetails) {

        AccountForm accountForm = new AccountForm();

        accountForm.setAccountNumber(accountDetails.getAccountNumber());
        accountForm.setAccountType(accountDetails.getAccountType().name());
        accountForm.setBalance(accountDetails.getBalance().toString());
        accountForm.setStatus(accountDetails.getAccountStatus().name());
        accountForm.setUserId(accountDetails.getUser().getId().toString());

        return accountForm;
    }

    private User getUserForAccount(AccountForm accountForm) {
        return umDao.getUser(Integer.valueOf(accountForm.getUserId()));
    }

    private String accountNoGenerator() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder("4");
        for (int i = 1; i < 16; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }
}
