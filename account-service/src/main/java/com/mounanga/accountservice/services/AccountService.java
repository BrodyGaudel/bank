package com.mounanga.accountservice.services;

import com.mounanga.accountservice.dtos.AccountDTO;
import com.mounanga.accountservice.dtos.UpdateBalanceForm;
import com.mounanga.accountservice.dtos.UpdateCurrencyForm;
import com.mounanga.accountservice.dtos.UpdateStatusForm;
import com.mounanga.accountservice.exceptions.AccountNotActivatedException;
import com.mounanga.accountservice.exceptions.AccountNotFoundException;
import com.mounanga.accountservice.exceptions.AlreadyExistException;
import com.mounanga.accountservice.exceptions.CustomerNotFoundException;

/**
 * The <code>AccountService</code> interface provides methods for managing account-related operations.
 */
public interface AccountService {

    /**
     * Creates a new account based on the provided account information.
     *
     * @param accountDTO The {@link AccountDTO} containing account details to create.
     * @return The newly created {@link AccountDTO}.
     * @throws CustomerNotFoundException If the associated customer is not found.
     * @throws AlreadyExistException If Customer already have an account
     */
    AccountDTO createAccount(AccountDTO accountDTO) throws CustomerNotFoundException, AlreadyExistException;

    /**
     * Retrieves an account by its unique identifier.
     *
     * @param id The unique identifier of the account.
     * @return The {@link AccountDTO} associated with the given ID.
     * @throws AccountNotFoundException If the account with the specified ID is not found.
     */
    AccountDTO getAccountById(String id) throws AccountNotFoundException;

    /**
     * Retrieves an account by its associated customer's unique identifier.
     *
     * @param customerId The unique identifier of the customer associated with the account.
     * @return The {@link AccountDTO} associated with the given customer ID.
     * @throws AccountNotFoundException If no account is found for the specified customer ID.
     */
    AccountDTO getAccountByCustomerId(String customerId) throws AccountNotFoundException;

    /**
     * Modifies the balance of an account based on the provided update form.
     *
     * @param form The {@link UpdateBalanceForm} containing account ID and balance update details.
     * @return The updated {@link AccountDTO}.
     * @throws AccountNotFoundException If the account specified in the update form is not found.
     * @throws AccountNotActivatedException If account status is not set to ACTIVATED
     */
    AccountDTO modifyAccountBalance(UpdateBalanceForm form) throws AccountNotFoundException, AccountNotActivatedException;

    /**
     * Modifies the status of an account based on the provided update form.
     *
     * @param form The {@link UpdateStatusForm} containing account ID and status update details.
     * @return The updated {@link AccountDTO}.
     * @throws AccountNotFoundException If the account specified in the update form is not found.
     */
    AccountDTO modifyAccountStatus(UpdateStatusForm form) throws AccountNotFoundException;

    /**
     * Deletes an account based on its unique identifier.
     *
     * @param id The unique identifier of the account to be deleted.
     */
    void deleteAccountById(String id);
}

