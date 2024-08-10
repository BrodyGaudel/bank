package org.mounanga.accountservice.commands.aggregate;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mounanga.accountservice.commands.command.*;
import org.mounanga.accountservice.commands.exception.AccountNotActivatedException;
import org.mounanga.accountservice.commands.exception.BalanceNotSufficientException;
import org.mounanga.accountservice.common.enums.AccountStatus;
import org.mounanga.accountservice.common.enums.Currency;
import org.mounanga.accountservice.common.enums.OperationType;
import org.mounanga.accountservice.common.event.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@SpringBootTest
class AccountAggregateTest {

    private AggregateTestFixture<AccountAggregate> fixture;
    LocalDateTime now;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(AccountAggregate.class);
        now = LocalDateTime.now();
    }


    @Test
    void testCreateAccountCommand() {
        CreateAccountCommand command = new CreateAccountCommand(
                "acc123",
                now,
                "system",
                AccountStatus.ACTIVATED,
                BigDecimal.valueOf(1000),
                Currency.USD,
                "cust123",
                "customer@example.com"
        );

        AccountCreatedEvent accountCreatedEvent = new AccountCreatedEvent(
                "acc123",
                now,
                "system",
                AccountStatus.ACTIVATED,
                BigDecimal.valueOf(1000),
                Currency.USD,
                "cust123",
                "customer@example.com"
        );

        AccountActivatedEvent accountActivatedEvent = new AccountActivatedEvent(
                "acc123", now, "system", AccountStatus.ACTIVATED
        );

        fixture.givenNoPriorActivity()
                .when(command)
                .expectSuccessfulHandlerExecution()
                .expectEvents(accountCreatedEvent, accountActivatedEvent);
    }

    @Test
    void testActivateAccountCommand() {
        AccountCreatedEvent accountCreatedEvent = new AccountCreatedEvent(
                "acc123",
                now,
                "system",
                AccountStatus.ACTIVATED,
                BigDecimal.valueOf(1000),
                Currency.USD,
                "cust123",
                "customer@example.com"
        );

        ActivateAccountCommand command = new ActivateAccountCommand(
                "acc123", now,"system",AccountStatus.ACTIVATED
        );

        AccountActivatedEvent event = new AccountActivatedEvent(
                "acc123",
                now,
                "system",
                AccountStatus.ACTIVATED
        );

        fixture.given(accountCreatedEvent)
                .when(command)
                .expectSuccessfulHandlerExecution()
                .expectEvents(event);
    }

    @Test
    void testSuspendAccountCommand() {
        AccountActivatedEvent accountActivatedEvent = new AccountActivatedEvent(
                "acc123",
                now,
                "system",
                AccountStatus.ACTIVATED
        );

        SuspendAccountCommand command = new SuspendAccountCommand(
                "acc123", now,"system", AccountStatus.SUSPENDED
        );

        AccountSuspendedEvent event = new AccountSuspendedEvent(
                "acc123",
                now,
                "system",
                AccountStatus.SUSPENDED
        );

        fixture.given(accountActivatedEvent).when(command)
                .expectSuccessfulHandlerExecution()
                .expectEvents(event);
    }

    @Test
    void testCreditAccountCommand() {
        CreditAccountCommand command = new CreditAccountCommand("acc123", LocalDateTime.now(), "admin", new BigDecimal("100.00"), OperationType.CREDIT, "Deposit description");
        AccountCreditedEvent expectedEvent = new AccountCreditedEvent(
                "acc123",
                LocalDateTime.now(),
                "admin",
                new BigDecimal("100.00"),
                OperationType.CREDIT,
                "Deposit description"
        );

        AccountCreatedEvent createdEvent = new AccountCreatedEvent(
                "acc123",
                now,
                "system",
                AccountStatus.ACTIVATED,
                BigDecimal.valueOf(1000),
                Currency.USD,
                "cust123",
                "customer@example.com"
        );

        fixture.given(createdEvent).when(command)
                .expectSuccessfulHandlerExecution()
                .expectEvents(expectedEvent);
    }

    @Test
    void testDebitAccountCommandSuccess() {
        AccountCreatedEvent createdEvent = new AccountCreatedEvent(
                "acc123",now,"system",AccountStatus.ACTIVATED,
                BigDecimal.valueOf(200),Currency.USD, "cust123","customer@example.com"
        );
        fixture.given(createdEvent).when(new DebitAccountCommand(
                        "acc123",
                        now,
                        "admin",
                        new BigDecimal("50.00"),
                        OperationType.CREDIT,
                        "Debit for purchase"
                )).expectSuccessfulHandlerExecution().expectEvents(new AccountDebitedEvent(
                        "acc123",
                        now,
                        "admin",
                        new BigDecimal("50.00"),
                        OperationType.CREDIT,
                        "Debit for purchase"
                )
        );
    }

    @Test
    void testDebitAccountCommandInsufficientBalance() {
        AccountCreatedEvent createdEvent = new AccountCreatedEvent(
                "acc123",now,"system",AccountStatus.ACTIVATED,
                BigDecimal.valueOf(10),Currency.USD, "cust123","customer@example.com"
        );
        fixture.given(createdEvent).when(new DebitAccountCommand(
                        "acc123",
                        now,
                        "system",
                        new BigDecimal("50.00"),
                        OperationType.DEBIT,
                        "Debit for purchase"
        )).expectException(BalanceNotSufficientException.class);
    }

    @Test
    void testDebitAccountCommandAccountNotActivated() {
        AccountCreatedEvent createdEvent = new AccountCreatedEvent(
                "acc123",now,"system",AccountStatus.SUSPENDED,
                BigDecimal.valueOf(200),Currency.USD, "cust123","customer@example.com"
        );
        fixture.given(createdEvent).when(new DebitAccountCommand(
                        "acc123",
                        now,
                        "system",
                        new BigDecimal("50.00"),
                        OperationType.DEBIT,
                        "Debit for purchase"
        )).expectException(AccountNotActivatedException.class);
    }


}