package com.jocata.bankingmgntsystem.am.dao;

import com.jocata.bankingmgntsystem.am.entity.AccountDetails;

import java.util.List;

public interface AccountDao {

    AccountDetails createAccount(AccountDetails account);

    AccountDetails getAccount(Integer id);

    AccountDetails updateAccount(AccountDetails account);

    void deleteAccount(Integer id);

    List<AccountDetails> getAllAccounts();

}
