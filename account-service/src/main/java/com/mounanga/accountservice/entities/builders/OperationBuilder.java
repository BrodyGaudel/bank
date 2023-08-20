package com.mounanga.accountservice.entities.builders;

import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.Operation;
import com.mounanga.accountservice.enums.Currency;
import com.mounanga.accountservice.enums.OperationType;

import java.math.BigDecimal;
import java.util.Date;

public class OperationBuilder {

    private String id;
    private Date date;
    private OperationType type;
    private BigDecimal amount;
    private Currency currency;
    private String description;
    private Account account;

    public OperationBuilder() {
        super();
    }

    public OperationBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public OperationBuilder setDate(Date date) {
        this.date = date;
        return this;
    }

    public OperationBuilder setType(OperationType type) {
        this.type = type;
        return this;
    }

    public OperationBuilder setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public OperationBuilder setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public OperationBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public OperationBuilder setAccount(Account account) {
        this.account = account;
        return this;
    }

    public Operation build() {
        Operation operation = new Operation();
        operation.setId(id);
        operation.setDate(date);
        operation.setType(type);
        operation.setAmount(amount);
        operation.setCurrency(currency);
        operation.setDescription(description);
        operation.setAccount(account);
        return operation;
    }
}
