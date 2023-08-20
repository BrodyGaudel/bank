package com.mounanga.accountservice.entities;

import com.mounanga.accountservice.enums.AccountStatus;
import com.mounanga.accountservice.enums.Currency;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class Account {

    @Id
    @Column(unique = true, updatable = false, nullable = false)
    private String id;

    @Column(unique = true, updatable = false, nullable = false)
    private String customerId;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Operation> operations;

    @Column(nullable = false, updatable = false)
    private Date creation;

    private Date lastUpdate;

    public Account() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", balance=" + balance +
                ", currency=" + currency +
                ", status=" + status +
                ", operations=" + operations +
                ", creation=" + creation +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
