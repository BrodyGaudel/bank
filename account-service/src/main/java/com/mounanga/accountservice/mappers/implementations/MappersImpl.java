package com.mounanga.accountservice.mappers.implementations;

import com.mounanga.accountservice.dtos.AccountDTO;
import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.builders.AccountBuilder;
import com.mounanga.accountservice.mappers.Mappers;
import org.springframework.stereotype.Service;

@Service
public class MappersImpl implements Mappers {
    /**
     * Converts an {@link AccountDTO} object to an {@link Account} object.
     *
     * @param accountDTO The {@link AccountDTO} to be converted.
     * @return An {@link Account} representing the converted data.
     */
    @Override
    public Account fromAccountDTO(AccountDTO accountDTO) {
        if(accountDTO == null){
            return null;
        }
        return new AccountBuilder()
                .id(accountDTO.id())
                .currency(accountDTO.currency())
                .balance(accountDTO.balance())
                .lastUpdate(accountDTO.lastUpdate())
                .status(accountDTO.status())
                .customerId(accountDTO.customerId())
                .build();
    }

    /**
     * Converts an {@link Account} object to an {@link AccountDTO} object.
     *
     * @param account The {@link Account} to be converted.
     * @return An {@link AccountDTO} representing the converted data.
     */
    @Override
    public AccountDTO fromAccount(Account account) {
        if(account == null){
            return null;
        }
        return new AccountDTO(
               account.getId(),
               account.getCurrency(),
               account.getBalance(),
               account.getStatus(),
               account.getCustomerId(),
               account.getCreation(),
               account.getLastUpdate()
        );
    }
}
