package com.mounanga.accountservice.mappers;

import com.mounanga.accountservice.dtos.AccountDTO;
import com.mounanga.accountservice.entities.Account;

/**
 * The <code>Mappers</code> interface defines methods for mapping between different data structures
 * related to accounts.
 *
 * @see Account
 * @see AccountDTO
 */
public interface Mappers {

    /**
     * Converts an {@link AccountDTO} object to an {@link Account} object.
     *
     * @param accountDTO The {@link AccountDTO} to be converted.
     * @return An {@link Account} representing the converted data.
     */
    Account fromAccountDTO(AccountDTO accountDTO);

    /**
     * Converts an {@link Account} object to an {@link AccountDTO} object.
     *
     * @param account The {@link Account} to be converted.
     * @return An {@link AccountDTO} representing the converted data.
     */
    AccountDTO fromAccount(Account account);
}

