package com.mounanga.accountservice.entities.builders;

import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.enums.Currency;
import com.mounanga.accountservice.enums.Status;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The AccountBuilder class provides a convenient way to construct instances of the Account class.
 * It allows for step-by-step construction of Account objects with various attributes.
 */
public class AccountBuilder {

    private final Account account = new Account();

    /**
     * Sets the ID of the account being built.
     *
     * @param id The ID to set for the account.
     * @return This AccountBuilder instance, allowing for method chaining.
     */
    public AccountBuilder id(String id) {
        account.setId(id);
        return this;
    }

    /**
     * Sets the currency of the account being built.
     *
     * @param currency The Currency to set for the account.
     * @return This AccountBuilder instance, allowing for method chaining.
     */
    public AccountBuilder currency(Currency currency) {
        account.setCurrency(currency);
        return this;
    }

    /**
     * Sets the balance of the account being built.
     *
     * @param balance The balance to set for the account.
     * @return This AccountBuilder instance, allowing for method chaining.
     */
    public AccountBuilder balance(BigDecimal balance) {
        account.setBalance(balance);
        return this;
    }

    /**
     * Sets the status of the account being built.
     *
     * @param status The Status to set for the account.
     * @return This AccountBuilder instance, allowing for method chaining.
     */
    public AccountBuilder status(Status status) {
        account.setStatus(status);
        return this;
    }

    /**
     * Sets the customer ID associated with the account being built.
     *
     * @param customerId The customer ID to set for the account.
     * @return This AccountBuilder instance, allowing for method chaining.
     */
    public AccountBuilder customerId(String customerId) {
        account.setCustomerId(customerId);
        return this;
    }

    /**
     * Sets the last update date of the account being built.
     *
     * @param lastUpdate The last update date to set for the account.
     * @return This AccountBuilder instance, allowing for method chaining.
     */
    public AccountBuilder lastUpdate(Date lastUpdate) {
        account.setLastUpdate(lastUpdate);
        return this;
    }

    /**
     * Builds and returns the fully constructed Account instance.
     * Also sets the creation date to the current date and time.
     *
     * @return The fully constructed Account instance.
     */
    public Account build() {
        account.setCreation(new Date());
        return account;
    }
}
