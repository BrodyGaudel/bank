package com.mounanga.accountservice.entities.builders;

import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.Operation;
import com.mounanga.accountservice.enums.AccountStatus;
import com.mounanga.accountservice.enums.Currency;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AccountBuilder {
    private String id;
    private String customerId;
    private BigDecimal balance;
    private Currency currency;
    private AccountStatus status;
    private List<Operation> operations;
    private Date creation;
    private Date lastUpdate;

    public AccountBuilder() {
        super();
    }

    public AccountBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public AccountBuilder setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public AccountBuilder setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public AccountBuilder setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public AccountBuilder setStatus(AccountStatus status) {
        this.status = status;
        return this;
    }

    public AccountBuilder setOperations(List<Operation> operations) {
        this.operations = operations;
        return this;
    }

    public AccountBuilder setCreation(Date creation) {
        this.creation = creation;
        return this;
    }

    public AccountBuilder setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public Account build() {
        Account account = new Account();
        account.setId(id);
        account.setCustomerId(customerId);
        account.setBalance(balance);
        account.setCurrency(currency);
        account.setStatus(status);
        account.setOperations(operations);
        account.setCreation(creation);
        account.setLastUpdate(lastUpdate);
        return account;
    }
}
