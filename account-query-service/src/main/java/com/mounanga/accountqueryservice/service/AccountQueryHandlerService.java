package com.mounanga.accountqueryservice.service;


import com.mounanga.accountqueryservice.entity.Account;
import com.mounanga.accountqueryservice.entity.AccountResponseDTO;
import com.mounanga.accountqueryservice.entity.Operation;
import com.mounanga.accountqueryservice.entity.OperationResponseDTO;
import com.mounanga.accountqueryservice.query.GetAccountByCustomerIdQuery;
import com.mounanga.accountqueryservice.query.GetAccountByIdQuery;
import com.mounanga.accountqueryservice.query.GetOperationByAccountIdQuery;
import com.mounanga.accountqueryservice.query.GetOperationByIdQuery;
import com.mounanga.accountqueryservice.repository.AccountRepository;
import com.mounanga.accountqueryservice.repository.OperationRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public AccountResponseDTO handle(@NotNull GetAccountByIdQuery query) {
        Account account = accountRepository.findById(query.getId()).orElse(null);
        if (account == null) {
            log.warn("account not found");
            return null;
        }
        return new AccountResponseDTO(account);
    }

    @QueryHandler
    public List<AccountResponseDTO> handle(@NotNull GetAccountByCustomerIdQuery query) {
        return accountRepository.findAllByCustomerId(query.getCustomerId())
                .stream()
                .filter(Objects::nonNull)
                .map(AccountResponseDTO::new)
                .toList();
    }

    @QueryHandler
    public OperationResponseDTO handle(@NotNull GetOperationByIdQuery query) {
        Operation operation = operationRepository.findById(query.getId()).orElse(null);
        if (operation == null) {
            log.warn("operation not found");
            return null;
        }
        return new OperationResponseDTO(operation);
    }

    @QueryHandler
    public List<OperationResponseDTO> handle(@NotNull GetOperationByAccountIdQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize());
        return operationRepository.findAllByAccountId(query.getAccountId(),pageable)
                .getContent()
                .stream()
                .map(OperationResponseDTO::new)
                .toList();
    }


}
