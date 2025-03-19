package com.jocata.bankingmgntsystem.am.dao.impl;

import com.jocata.bankingmgntsystem.am.dao.AccountDao;
import com.jocata.bankingmgntsystem.am.entity.AccountDetails;
import com.jocata.bankingmgntsystem.config.HibernateConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountDaoImpl implements AccountDao {

    private final HibernateConfig hibernateConfig;

    public AccountDaoImpl(HibernateConfig hibernateConfig) {
        this.hibernateConfig = hibernateConfig;
    }

    @Override
    public AccountDetails createAccount(AccountDetails account) {
        return hibernateConfig.saveEntity(account);
    }

    @Override
    public AccountDetails getAccount(Integer id) {
        return hibernateConfig.findEntityById(AccountDetails.class, id);
    }

    @Override
    public AccountDetails updateAccount(AccountDetails account) {
        return hibernateConfig.updateEntity(account);
    }

    @Override
    public void  deleteAccount(Integer id) {
        hibernateConfig.deleteEntity(AccountDetails.class, id);
    }

    @Override
    public List<AccountDetails> getAllAccounts() {
        return hibernateConfig.loadEntitiesByCriteria(AccountDetails.class);
    }
}
