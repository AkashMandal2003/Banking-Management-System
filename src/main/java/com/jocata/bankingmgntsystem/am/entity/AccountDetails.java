package com.jocata.bankingmgntsystem.am.entity;


import com.jocata.bankingmgntsystem.um.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "account")
public class AccountDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    @Column(name = "account_number",unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "account_type",unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private BigDecimal balance;

    @Column(name = "account_status",unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
