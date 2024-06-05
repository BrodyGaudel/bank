package org.mounanga.accountservice.query.util;

import org.mounanga.accountservice.query.dto.*;
import org.mounanga.accountservice.query.entity.Account;
import org.mounanga.accountservice.query.entity.Operation;

import java.util.ArrayList;
import java.util.List;

public class Mappers {

    private Mappers() {
        super();
    }

    public static AccountResponse fromAccount(final Account account) {
        if(account == null){
            return null;
        }
        return AccountResponse.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .customerId(account.getCustomerId())
                .createdBy(account.getCreatedBy())
                .createdDate(account.getCreatedDate())
                .lastModifiedBy(account.getLastModifiedBy())
                .lastModifiedDate(account.getLastModifiedDate())
                .status(account.getStatus())
                .build();
    }

    public static List<AccountResponse> fromListOfAccounts(final List<Account> accounts) {
        if(accounts == null || accounts.isEmpty()){
           return new ArrayList<>();
        }
        return accounts.stream().map(Mappers::fromAccount).toList();
    }

    public static OperationResponse fromOperation(final Operation operation) {
        if(operation == null){
            return null;
        }
        return OperationResponse.builder()
                .id(operation.getId())
                .operator(operation.getOperator())
                .type(operation.getType())
                .description(operation.getDescription())
                .amount(operation.getAmount())
                .dateTime(operation.getDateTime())
                .accountId(getAccountId(operation.getAccount()))
                .build();
    }

    public static List<OperationResponse> fromListOfOperations(final List<Operation> operations) {
        if(operations == null || operations.isEmpty()){
            return new ArrayList<>();
        }
        return operations.stream().map(Mappers::fromOperation).toList();
    }


    private static String getAccountId(final Account account) {
        if(account == null){
            return null;
        }
        return account.getId();
    }

}
