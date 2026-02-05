package com.mounanga.accountqueryservice.service.implementation;

import com.mounanga.accountqueryservice.entity.Account;
import com.mounanga.accountqueryservice.entity.Operation;
import com.mounanga.accountqueryservice.enums.AccountStatus;
import com.mounanga.accountqueryservice.enums.OperationType;
import com.mounanga.accountqueryservice.event.*;
import ga.aninf.accountqueryservice.event.*;
import com.mounanga.accountqueryservice.repository.AccountRepository;
import com.mounanga.accountqueryservice.repository.OperationRepository;
import com.mounanga.accountqueryservice.service.AccountEventHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@Transactional
@Slf4j
public class AccountEventHandlerServiceImpl implements AccountEventHandlerService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    public AccountEventHandlerServiceImpl(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @Override
    public void handle(AccountCreatedEvent event) {
        log.info("Processing AccountCreatedEvent for account ID: {}", event.getId());

        Account account = new Account(event);
        accountRepository.save(account);

        log.info("Successfully created account with ID: {}", event.getId());
    }

    @Override
    public void handle(@NotNull AccountActivatedEvent event) {
        log.info("Processing AccountActivatedEvent for account ID: {}", event.getId());

        Account account = findAccountById(event.getId());
        account.setStatus(AccountStatus.ACTIVATED);
        accountRepository.save(account);

        log.info("Successfully activated account ID: {}", event.getId());
    }

    @Override
    public void handle(@NotNull AccountDeactivatedEvent event) {
        log.info("Processing AccountDeactivatedEvent for account ID: {}", event.getId());

        Account account = findAccountById(event.getId());
        account.setStatus(AccountStatus.DEACTIVATED);
        accountRepository.save(account);

        log.info("Successfully deactivated account ID: {}", event.getId());
    }

    @Override
    public void handle(@NotNull AccountCreditedEvent event) {
        log.info("Processing AccountCreditedEvent for account ID: {}, amount: {}",
                event.getId(), event.getAmount());

        Account account = findAccountById(event.getId());
        BigDecimal oldBalance = account.getBalance();
        BigDecimal newBalance = oldBalance.add(event.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        log.debug("Updated balance for account ID: {} from {} to {}.", event.getId(), oldBalance, newBalance);

        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setAuthor(event.getAuthor());
        operation.setAmount(event.getAmount());
        operation.setRemark(event.getDescription());
        operation.setDateTime(event.getDateTime());
        operation.setType(OperationType.CREDIT);
        operationRepository.save(operation);

        log.info("Successfully credited account ID: {}, new balance: {}",
                event.getId(), newBalance);
    }

    @Override
    public void handle(@NotNull AccountDebitedEvent event) {
        log.info("Processing AccountDebitedEvent for account ID: {}, amount: {}", event.getId(), event.getAmount());

        Account account = findAccountById(event.getId());
        BigDecimal oldBalance = account.getBalance();
        BigDecimal newBalance = oldBalance.subtract(event.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        log.debug("Updated balance for account ID: {} from {} to {}", event.getId(), oldBalance, newBalance);

        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setAuthor(event.getAuthor());
        operation.setAmount(event.getAmount());
        operation.setRemark(event.getDescription());
        operation.setDateTime(event.getDateTime());
        operation.setType(OperationType.DEBIT);
        operationRepository.save(operation);

        log.info("Successfully debited account ID: {}, new balance: {}", event.getId(), newBalance);
    }


    private Account findAccountById(String id) {
        log.debug("Fetching account with ID: {}", id);

        return accountRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Account not found with ID: {}", id);
                    return new ResourceNotFoundException("Account not found with id: " + id);
                }
        );
    }
}