package com.mounanga.accountservice.mappers.implementations;

import com.mounanga.accountservice.dtos.AccountDTO;
import com.mounanga.accountservice.dtos.OperationDTO;
import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.Operation;
import com.mounanga.accountservice.entities.builders.AccountBuilder;
import com.mounanga.accountservice.mappers.Mappers;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MappersImpl implements Mappers {

    /**
     * Converts an {@link AccountDTO} object to an {@link Account} object.
     *
     * @param accountDTO The {@link AccountDTO} to be converted.
     * @return The corresponding {@link Account} object.
     */
    @Override
    public Account fromAccountDTO(AccountDTO accountDTO) {
        if(accountDTO == null){
            return null;
        }
        return new AccountBuilder()
                .setId(accountDTO.id())
                .setCustomerId(accountDTO.customerId())
                .setBalance(accountDTO.balance())
                .setCurrency(accountDTO.currency())
                .setStatus(accountDTO.status())
                .setOperations( new ArrayList<>())
                .setCreation(accountDTO.creation())
                .setLastUpdate(accountDTO.lastUpdate())
                .build();
    }

    /**
     * Converts an {@link Account} object to an {@link AccountDTO} object.
     *
     * @param account The {@link Account} to be converted.
     * @return The corresponding {@link AccountDTO} object.
     */
    @Override
    public AccountDTO fromAccount(Account account) {
        if(account == null){
            return null;
        }
        return new AccountDTO(
                account.getId(),
                account.getCustomerId(),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus(),
                account.getCreation(),
                account.getLastUpdate()
        );
    }

    /**
     * Converts an {@link Operation} object to an {@link OperationDTO} object.
     *
     * @param operation The {@link Operation} to be converted.
     * @return The corresponding {@link OperationDTO} object.
     */
    @Override
    public OperationDTO fromOperation(Operation operation) {
        if(operation == null){
            return null;
        }
        return new OperationDTO(
                operation.getId(),
                operation.getDate(),
                operation.getType(),
                operation.getAmount(),
                operation.getCurrency(),
                operation.getDescription(),
                operation.getAccount().getId()
        );
    }
}
