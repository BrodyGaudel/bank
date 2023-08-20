package com.mounanga.accountservice.mappers;

import com.mounanga.accountservice.dtos.AccountDTO;
import com.mounanga.accountservice.dtos.OperationDTO;
import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.Operation;

/**
 * This interface provides mapping methods to convert between different data transfer objects (DTOs)
 * and domain objects for the Account and Operation classes.
 */
public interface Mappers {

    /**
     * Converts an {@link AccountDTO} object to an {@link Account} object.
     *
     * @param accountDTO The {@link AccountDTO} to be converted.
     * @return The corresponding {@link Account} object.
     */
    Account fromAccountDTO(AccountDTO accountDTO);

    /**
     * Converts an {@link Account} object to an {@link AccountDTO} object.
     *
     * @param account The {@link Account} to be converted.
     * @return The corresponding {@link AccountDTO} object.
     */
    AccountDTO fromAccount(Account account);

    /**
     * Converts an {@link Operation} object to an {@link OperationDTO} object.
     *
     * @param operation The {@link Operation} to be converted.
     * @return The corresponding {@link OperationDTO} object.
     */
    OperationDTO fromOperation(Operation operation);
}

