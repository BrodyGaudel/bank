package com.brodygaudel.accountservice.util;

import com.brodygaudel.accountservice.dto.AccountDTO;
import com.brodygaudel.accountservice.dto.OperationDTO;
import com.brodygaudel.accountservice.entity.Account;
import com.brodygaudel.accountservice.entity.Operation;

/**
 * This interface defines methods for mapping between entities and their corresponding DTOs.
 *
 * <p>Author: Brody Gaudel</p>
 */
public interface Mappers {
    /**
     * Converts an {@code AccountDTO} to an {@code Account}.
     *
     * @param dto The {@code AccountDTO} to be converted.
     * @return The corresponding {@code Account} object.
     */
    Account fromAccountDTO(AccountDTO dto);

    /**
     * Converts an {@code Account} to an {@code AccountDTO}.
     *
     * @param account The {@code Account} to be converted.
     * @return The corresponding {@code AccountDTO} object.
     */
    AccountDTO fromAccount(Account account);

    /**
     * Converts an {@code OperationDTO} to an {@code Operation}.
     *
     * @param dto The {@code OperationDTO} to be converted.
     * @return The corresponding {@code Operation} object.
     */
    Operation fromOperationDTO(OperationDTO dto);

    /**
     * Converts an {@code Operation} to an {@code OperationDTO}.
     *
     * @param operation The {@code Operation} to be converted.
     * @return The corresponding {@code OperationDTO} object.
     */
    OperationDTO fromOperation(Operation operation);
}
