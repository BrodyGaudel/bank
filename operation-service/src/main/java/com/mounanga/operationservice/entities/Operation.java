package com.mounanga.operationservice.entities;

import com.mounanga.operationservice.enums.Currency;
import com.mounanga.operationservice.enums.Type;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Operation {

    @Id
    @Column(unique = true, updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, updatable = false)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Type type;

    @Column(nullable = false, updatable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Currency currency;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(unique = true, updatable = false, nullable = false)
    private String accountId;

    public Operation() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", currency=" + currency +
                ", amount=" + amount +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}
