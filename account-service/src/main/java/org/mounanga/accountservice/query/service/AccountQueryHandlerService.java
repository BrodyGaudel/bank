package org.mounanga.accountservice.query.service;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.query.dto.AccountResponse;
import org.mounanga.accountservice.query.dto.OperationResponse;
import org.mounanga.accountservice.query.entity.Account;
import org.mounanga.accountservice.query.entity.Operation;
import org.mounanga.accountservice.query.exception.AccountNotFoundException;
import org.mounanga.accountservice.query.exception.OperationNotFoundException;
import org.mounanga.accountservice.query.queries.GetAccountByIdQuery;
import org.mounanga.accountservice.query.queries.GetAllAccountByCustomerIdQuery;
import org.mounanga.accountservice.query.queries.GetAllOperationByAccountIdQuery;
import org.mounanga.accountservice.query.queries.GetOperationByIdQuery;
import org.mounanga.accountservice.query.repository.AccountRepository;
import org.mounanga.accountservice.query.repository.OperationRepository;
import org.mounanga.accountservice.query.util.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccountQueryHandlerService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    public AccountQueryHandlerService(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @QueryHandler
    public AccountResponse handle(@NotNull GetAccountByIdQuery query){
        log.info("Handling query: {}", query);
        Account account = accountRepository.findById(query.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account with id " + query.getAccountId() + " not found"));
        log.info("Account found");
        return Mappers.fromAccount(account);
    }

    @QueryHandler
    public List<AccountResponse> handle(@NotNull GetAllAccountByCustomerIdQuery query){
        log.info("GetAllAccountByCustomerIdQuery handled");
        List<Account> accounts = accountRepository.findByCustomerId(query.getCustomerId());
        log.info("{} accounts found", accounts.size());
        return Mappers.fromListOfAccounts(accounts);
    }

    @QueryHandler
    public OperationResponse handle(@NotNull GetOperationByIdQuery query){
        log.info("GetOperationByIdQuery handled");
        Operation operation = operationRepository.findById(query.getOperationId())
                .orElseThrow( () -> new OperationNotFoundException("Operation with id " + query.getOperationId() + " not found"));
        log.info("operation found");
        return Mappers.fromOperation(operation);
    }

    @QueryHandler
    public List<OperationResponse> handle(@NotNull GetAllOperationByAccountIdQuery query){
        log.info("GetAllOperationByAccountIdQuery handled");
        Page<Operation> operationPage = operationRepository.findByAccountId(
                query.getAccountId(),
                PageRequest.of(query.getPage(), query.getSize())
        );
        log.info("{} operations found", operationPage.getTotalElements());
        return Mappers.fromListOfOperations(operationPage.getContent());
    }
}
