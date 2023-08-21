package com.mounanga.accountservice.services;

import com.mounanga.accountservice.dtos.*;
import com.mounanga.accountservice.exceptions.AccountNotFoundException;
import com.mounanga.accountservice.exceptions.BalanceNotSufficientException;
import com.mounanga.accountservice.exceptions.OperationNotFoundException;

/**
 * Service interface for managing banking operations (Credit, Debit, Transfer) for an account.
 */
public interface OperationService {

    /**
     * Credit funds to an account.
     *
     * @param creditDTO The credit details.
     * @return The updated account details after the credit operation.
     * @throws AccountNotFoundException If the account for the credit operation is not found.
     * @throws BalanceNotSufficientException If the account balance is not sufficient for the credit operation.
     */
    CreditDTO creditAccount(CreditDTO creditDTO) throws AccountNotFoundException, BalanceNotSufficientException;

    /**
     * Debit funds from an account.
     *
     * @param debitDTO The debit details.
     * @return The updated account details after the debit operation.
     * @throws AccountNotFoundException If the account for the debit operation is not found.
     * @throws BalanceNotSufficientException If the account balance is not sufficient for the debit operation.
     */
    DebitDTO debitAccount(DebitDTO debitDTO) throws AccountNotFoundException, BalanceNotSufficientException;


    /**
     * Retrieve an operation by its ID.
     *
     * @param id The ID of the operation to retrieve.
     * @return The operation details.
     * @throws OperationNotFoundException If the operation with the given ID is not found.
     */
    OperationDTO getOperationById(String id) throws OperationNotFoundException;

    /**
     * Retrieve the history of operations for a specific account.
     *
     * @param accountId The ID of the account to retrieve the history for.
     * @param page The page number for pagination.
     * @param size The number of operations per page.
     * @return The history of operations for the account.
     * @throws AccountNotFoundException if account not found
     */
    HistoryDTO getAccountHistory(String accountId, int page, int size) throws AccountNotFoundException;
}
