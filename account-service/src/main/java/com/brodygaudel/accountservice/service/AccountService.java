package com.brodygaudel.accountservice.service;


import com.brodygaudel.accountservice.dto.AccountDTO;
import com.brodygaudel.accountservice.exception.AccountNotFoundException;
import com.brodygaudel.accountservice.exception.CustomerAlreadyHaveAccountException;
import com.brodygaudel.accountservice.exception.CustomerNotFoundException;

/**
 * Service interface for managing accounts.
 *
 * <p>Author: Brody Gaudel</p>
 */
public interface AccountService {

    /**
     * Retrieve an account by its ID.
     *
     * @param id The ID of the account to retrieve.
     * @return The account details as a Data Transfer Object (DTO).
     * @throws AccountNotFoundException If the account with the given ID is not found.
     */
    AccountDTO getById(String id) throws AccountNotFoundException;

    /**
     * Retrieve an account by its customer ID.
     *
     * @param customerId The customer ID associated with the account.
     * @return The account details as a Data Transfer Object (DTO).
     * @throws AccountNotFoundException If the account for the given customer ID is not found.
     */
    AccountDTO getByCustomerId(String customerId) throws AccountNotFoundException;

    /**
     * Create a new account.
     *
     * @param accountDTO The account details to create.
     * @return The created account details as a Data Transfer Object (DTO).
     * @throws CustomerNotFoundException If the customer associated with the account is not found.
     * @throws CustomerAlreadyHaveAccountException If customer already have an account
     */
    AccountDTO save(AccountDTO accountDTO) throws CustomerNotFoundException, CustomerAlreadyHaveAccountException;

    /**
     * Delete an account by its ID.
     *
     * @param id The ID of the account to delete.
     */
    void deleteById(String id);

    /**
     * Update Account Status
     * @param accountDTO The account details to update status
     * @return The Updated Account details as a Data Transfer Object (DTO).
     * @throws AccountNotFoundException If account not found
     */
    AccountDTO updateStatus(AccountDTO accountDTO) throws AccountNotFoundException;
}
