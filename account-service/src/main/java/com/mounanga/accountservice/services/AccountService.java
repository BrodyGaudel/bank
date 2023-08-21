package com.mounanga.accountservice.services;

import com.mounanga.accountservice.dtos.AccountDTO;
import com.mounanga.accountservice.exceptions.AccountNotFoundException;
import com.mounanga.accountservice.exceptions.CustomerNotFoundException;

/**
 * Bank Account Management Service
 */
public interface AccountService {
    /**
     * Retrieve an account by its ID.
     *
     * @param id The ID of the account to retrieve.
     * @return The account details as a Data Transfer Object (DTO).
     * @throws AccountNotFoundException If the account with the given ID is not found.
     */
    AccountDTO getAccountById(String id) throws AccountNotFoundException;

    /**
     * Retrieve an account by its customer ID.
     *
     * @param customerId The customer ID associated with the account.
     * @return The account details as a Data Transfer Object (DTO).
     * @throws AccountNotFoundException If the account for the given customer ID is not found.
     */
    AccountDTO getAccountByCustomerId(String customerId) throws AccountNotFoundException;

    /**
     * Create a new account.
     *
     * @param accountDTO The account details to create.
     * @return The created account details as a Data Transfer Object (DTO).
     * @throws CustomerNotFoundException If the customer associated with the account is not found.
     */
    AccountDTO createAccount(AccountDTO accountDTO) throws CustomerNotFoundException;

    /**
     * Delete an account by its ID.
     *
     * @param id The ID of the account to delete.
     */
    void deleteAccountById(String id);

    /**
     * Update Account Status
     * @param accountDTO The account details to update status
     * @return The Updated Account details as a Data Transfer Object (DTO).
     * @throws AccountNotFoundException If account not found
     */
    AccountDTO updateAccountStatus(AccountDTO accountDTO) throws AccountNotFoundException;
}

