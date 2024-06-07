package org.mounanga.accountservice.query.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.accountservice.common.enums.Currency;
import org.mounanga.accountservice.common.enums.Status;
import org.mounanga.accountservice.common.event.*;
import org.mounanga.accountservice.query.entity.Account;
import org.mounanga.accountservice.query.entity.Operation;
import org.mounanga.accountservice.query.enums.Type;
import org.mounanga.accountservice.query.repository.AccountRepository;
import org.mounanga.accountservice.query.repository.OperationRepository;
import org.mounanga.accountservice.query.web.restclient.Notification;
import org.mounanga.accountservice.query.web.restclient.NotificationRestClient;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountEventHandlerServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private NotificationRestClient notificationRestClient;

    @InjectMocks
    private AccountEventHandlerService accountEventHandlerService;

    @BeforeEach
    void setUp() {
        this.accountEventHandlerService = new AccountEventHandlerService(accountRepository, operationRepository, notificationRestClient);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testHandleAccountCreatedEvent() {
        AccountCreatedEvent event = new AccountCreatedEvent(
                "id", Status.CREATED, BigDecimal.ZERO, Currency.TND, "customerId", LocalDateTime.now(), "createdBy"
        );
        Account account = Account.builder().id(event.getId()).status(event.getStatus()).currency(event.getCurrency())
                .balance(event.getBalance()).createdBy(event.getCreatedBy()).customerId(event.getCustomerId()).createdDate(event.getDateTime())
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountEventHandlerService.handle(event);
        verify(accountRepository, times(1)).save(any(Account.class));

    }

    @Test
    void testHandleAccountActivatedEvent() {
        AccountActivatedEvent event = new AccountActivatedEvent("id", Status.ACTIVATED, LocalDateTime.now(), "activatedBy");

        Account accountFound = Account.builder().id(event.getId()).status(Status.SUSPENDED).build();
        Account accountActivated = Account.builder().id(event.getId()).status(event.getStatus()).lastModifiedBy(event.getActivatedBy()).lastModifiedDate(event.getDateTime()).build();

        when(accountRepository.findById(anyString())).thenReturn(Optional.of(accountFound));
        when(accountRepository.save(any(Account.class))).thenReturn(accountActivated);
        accountEventHandlerService.handle(event);

        verify(accountRepository, times(1)).findById(event.getId());
        verify(accountRepository, times(1)).save(accountFound);
    }

    @Test
    void testHandleAccountSuspendedEvent() {
        AccountActivatedEvent event = new AccountActivatedEvent("id", Status.SUSPENDED, LocalDateTime.now(), "activatedBy");

        Account accountFound = Account.builder().id(event.getId()).status(Status.ACTIVATED).build();
        Account accountSuspended = Account.builder().id(event.getId()).status(event.getStatus()).lastModifiedBy(event.getActivatedBy()).lastModifiedDate(event.getDateTime()).build();

        when(accountRepository.findById(anyString())).thenReturn(Optional.of(accountFound));
        when(accountRepository.save(any(Account.class))).thenReturn(accountSuspended);
        accountEventHandlerService.handle(event);

        verify(accountRepository, times(1)).findById(event.getId());
        verify(accountRepository, times(1)).save(accountFound);
    }

    @Test
    void testHandleAccountDeletedEvent() {
        AccountDeletedEvent event = new AccountDeletedEvent("id");

        doNothing().when(accountRepository).deleteById(event.getId());

        accountEventHandlerService.handle(event);

        verify(accountRepository, times(1)).deleteById(event.getId());
    }

    @Test
    void testHandleAccountCreditedEvent() {
        AccountCreditedEvent event = new AccountCreditedEvent("1", "description", BigDecimal.TEN, LocalDateTime.now(), "creditedBy");
        Account accountFound = Account.builder().id(event.getId()).status(Status.ACTIVATED).balance(BigDecimal.TEN).build();
        Account accountCredited = Account.builder().id(event.getId()).status(Status.ACTIVATED).balance(BigDecimal.valueOf(20)).lastModifiedDate(event.getDateTime()).lastModifiedBy(event.getCreditedBy()).build();
        Operation operationSaved = Operation.builder()
                .account(accountCredited)
                .type(Type.CREDIT)
                .id(UUID.randomUUID().toString())
                .amount(event.getAmount())
                .dateTime(event.getDateTime())
                .description(event.getDescription())
                .operator(event.getCreditedBy())
                .build();

        when(accountRepository.findById(anyString())).thenReturn(Optional.of(accountFound));
        when(accountRepository.save(any(Account.class))).thenReturn(accountCredited);
        when(operationRepository.save(any(Operation.class))).thenReturn(operationSaved);
        doNothing().when(notificationRestClient).postNotification(any(Notification.class));
        accountEventHandlerService.handle(event);

        verify(accountRepository, times(1)).findById(event.getId());
        verify(accountRepository, times(1)).save(accountFound);
        verify(operationRepository, times(1)).save(any(Operation.class));
        verify(notificationRestClient, times(1)).postNotification(any(Notification.class));

    }

    @Test
    void testHandleAccountDebitedEvent() {
        AccountDebitedEvent event = new AccountDebitedEvent("1", "description", BigDecimal.TEN, LocalDateTime.now(), "creditedBy");
        Account accountFound = Account.builder().id(event.getId()).status(Status.ACTIVATED).balance(BigDecimal.valueOf(1000)).build();
        Account accountDebited = Account.builder().id(event.getId()).status(Status.ACTIVATED).balance(BigDecimal.valueOf(980)).lastModifiedDate(event.getDateTime()).lastModifiedBy(event.getDebitedBy()).build();
        Operation operationSaved = Operation.builder()
                .account(accountDebited)
                .type(Type.CREDIT)
                .id(UUID.randomUUID().toString())
                .amount(event.getAmount())
                .dateTime(event.getDateTime())
                .description(event.getDescription())
                .operator(event.getDebitedBy())
                .build();

        when(accountRepository.findById(anyString())).thenReturn(Optional.of(accountFound));
        when(accountRepository.save(any(Account.class))).thenReturn(accountDebited);
        when(operationRepository.save(any(Operation.class))).thenReturn(operationSaved);
        doNothing().when(notificationRestClient).postNotification(any(Notification.class));
        accountEventHandlerService.handle(event);

        verify(accountRepository, times(1)).findById(event.getId());
        verify(accountRepository, times(1)).save(accountFound);
        verify(operationRepository, times(1)).save(any(Operation.class));
        verify(notificationRestClient, times(1)).postNotification(any(Notification.class));
    }
}