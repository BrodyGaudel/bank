package org.mounanga.accountservice.query.service;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.common.event.*;
import org.mounanga.accountservice.query.entity.Account;
import org.mounanga.accountservice.query.entity.Operation;
import org.mounanga.accountservice.query.enums.Type;
import org.mounanga.accountservice.query.exception.AccountNotFoundException;
import org.mounanga.accountservice.query.repository.AccountRepository;
import org.mounanga.accountservice.query.repository.OperationRepository;
import org.mounanga.accountservice.query.web.restclient.Notification;
import org.mounanga.accountservice.query.web.restclient.NotificationRestClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class AccountEventHandlerService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final NotificationRestClient notificationRestClient;

    public AccountEventHandlerService(AccountRepository accountRepository, OperationRepository operationRepository, NotificationRestClient notificationRestClient) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
        this.notificationRestClient = notificationRestClient;
    }

    @EventHandler
    public void handle(@NotNull AccountCreatedEvent event) {
        log.info("AccountCreatedEvent received");
        Account account = Account.builder()
                .id(event.getId())
                .status(event.getStatus())
                .customerId(event.getCustomerId())
                .currency(event.getCurrency())
                .balance(event.getBalance())
                .createdBy(event.getCreatedBy())
                .createdDate(event.getDateTime())
                .build();
        Account accountSaved = accountRepository.save(account);
        log.info("Account saved with id '{}' at '{}' by {}", accountSaved.getId(), accountSaved.getCreatedBy(), accountSaved.getCreatedDate());
    }

    @EventHandler
    public void handle(@NotNull AccountActivatedEvent event) {
        log.info("AccountActivatedEvent received");
        Account account = getAccountById(event.getId());
        account.setStatus(event.getStatus());
        account.setLastModifiedBy(event.getActivatedBy());
        account.setLastModifiedDate(event.getDateTime());
        Account accountActivated = accountRepository.save(account);
        log.info("Account with id '{}' activated at '{}' by {}", accountActivated.getId(), accountActivated.getLastModifiedDate(), accountActivated.getLastModifiedBy());
    }

    @EventHandler
    public void handle(@NotNull AccountSuspendedEvent event) {
        log.info("AccountSuspendedEvent received");
        Account account = getAccountById(event.getId());
        account.setStatus(event.getStatus());
        account.setLastModifiedBy(event.getSuspendedBy());
        account.setLastModifiedDate(event.getDateTime());
        Account accountActivated = accountRepository.save(account);
        log.info("Account with id '{}' suspended at '{}' by {}", accountActivated.getId(), accountActivated.getLastModifiedDate(), accountActivated.getLastModifiedBy());
    }

    @EventHandler
    public void handle(@NotNull AccountDeletedEvent event) {
        log.info("AccountDeletedEvent received");
        accountRepository.deleteById(event.getId());
        log.info("Account successfully deleted.");
    }

    @EventHandler
    public void handle(@NotNull AccountCreditedEvent event) {
        log.info("AccountCreditedEvent received");
        Account account = getAccountById(event.getId());
        account.setLastModifiedBy(event.getCreditedBy());
        account.setLastModifiedDate(event.getDateTime());
        account.setBalance(account.getBalance().add(event.getAmount()));
        Account creditedAccount = accountRepository.save(account);
        log.info("Account with id '{}' credited with amount '{}' at '{}' by {}", creditedAccount.getId(), event.getAmount(), creditedAccount.getLastModifiedDate(), creditedAccount.getLastModifiedBy());
        Operation operation = Operation.builder()
                .account(creditedAccount)
                .type(Type.CREDIT)
                .amount(event.getAmount())
                .dateTime(event.getDateTime())
                .description(event.getDescription())
                .operator(event.getCreditedBy())
                .build();
        Operation savedOperation = operationRepository.save(operation);
        log.info("Credit Operation saved with id '{}'", savedOperation.getId());
        sendNotification(savedOperation, creditedAccount);
    }

    @EventHandler
    public void handle(@NotNull AccountDebitedEvent event) {
        log.info("AccountDebitedEvent received");
        Account account = getAccountById(event.getId());
        account.setLastModifiedBy(event.getDebitedBy());
        account.setLastModifiedDate(event.getDateTime());
        account.setBalance(account.getBalance().subtract(event.getAmount()));
        Account debitedAccount = accountRepository.save(account);
        log.info("Account with id '{}' debited with amount '{}' at '{}' by {}", debitedAccount.getId(), event.getAmount(), debitedAccount.getLastModifiedDate(), debitedAccount.getLastModifiedBy());
        Operation operation = Operation.builder()
                .account(debitedAccount)
                .type(Type.DEBIT)
                .amount(event.getAmount())
                .dateTime(event.getDateTime())
                .description(event.getDescription())
                .operator(event.getDebitedBy())
                .build();
        Operation savedOperation = operationRepository.save(operation);
        log.info("Debit Operation saved with id '{}'", savedOperation.getId());
        sendNotification(savedOperation, debitedAccount);
    }

    private Account getAccountById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id '" + id + "' not found"));
    }

    private void sendNotification(@NotNull Operation operation, @NotNull Account account) {
        Notification notification = new Notification(
                operation.getAccount().getId(),
                operation.getType(),
                operation.getAmount(),
                operation.getDescription(),
                operation.getDateTime(),
                account.getCustomerId()
        );
        notificationRestClient.postNotification(notification);
    }

}
