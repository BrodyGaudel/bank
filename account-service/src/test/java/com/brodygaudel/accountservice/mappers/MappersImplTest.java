package com.brodygaudel.accountservice.mappers;

import com.brodygaudel.accountservice.dtos.AccountDTO;
import com.brodygaudel.accountservice.dtos.OperationDTO;
import com.brodygaudel.accountservice.entities.Account;
import com.brodygaudel.accountservice.entities.Operation;
import com.brodygaudel.accountservice.enums.AccountStatus;
import com.brodygaudel.accountservice.enums.Currency;
import com.brodygaudel.accountservice.enums.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MappersImplTest {

    private MappersImpl mappers;

    @BeforeEach
    void setUp() {
        mappers = new MappersImpl();
    }

    @Test
    void fromAccountDTO() {
        AccountDTO accountDTO = new AccountDTO(
                "id",
                "customerId",
                BigDecimal.valueOf(5000),
                Currency.EUR,
                AccountStatus.ACTIVATED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Account account = mappers.fromAccountDTO(accountDTO);
        assertNotNull(account);
        assertEquals(accountDTO.id(), account.getId());
        assertEquals(accountDTO.customerId(), account.getCustomerId());
        assertEquals(accountDTO.balance(), account.getBalance());
        assertEquals(accountDTO.currency(), account.getCurrency());
        assertEquals(accountDTO.status(), account.getStatus());
        assertEquals(accountDTO.lastUpdate(), account.getLastUpdate());
        assertEquals(accountDTO.creation(), account.getCreation());
    }

    @Test
    void fromAccount() {
        Account account = Account.builder()
                .id("id")
                .customerId("customerId")
                .balance(BigDecimal.valueOf(5000))
                .currency(Currency.EUR)
                .status(AccountStatus.ACTIVATED)
                .creation(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .operations(new ArrayList<>())
                .build();
        AccountDTO accountDTO = mappers.fromAccount(account);
        assertNotNull(account);
        assertEquals(accountDTO.id(), account.getId());
        assertEquals(accountDTO.customerId(), account.getCustomerId());
        assertEquals(accountDTO.balance(), account.getBalance());
        assertEquals(accountDTO.currency(), account.getCurrency());
        assertEquals(accountDTO.status(), account.getStatus());
        assertEquals(accountDTO.lastUpdate(), account.getLastUpdate());
        assertEquals(accountDTO.creation(), account.getCreation());
    }

    @Test
    void fromOperationDTO() {
        OperationDTO operationDTO = new OperationDTO(
                "id",
                LocalDateTime.now(),
                OperationType.CREDIT,
                BigDecimal.valueOf(5000),
                Currency.EUR,
                "DESCRIPTION",
                "accountId"
        );
        Operation operation = mappers.fromOperationDTO(operationDTO);
        assertNotNull(operation);
        assertEquals(operation.getId(), operationDTO.id());
        assertEquals(operation.getCurrency(), operationDTO.currency());
        assertEquals(operation.getDescription(), operationDTO.description());
        assertEquals(operation.getType(), operationDTO.type());
        assertEquals(operation.getAmount(), operationDTO.amount());
        assertEquals(operation.getCurrency(), operationDTO.currency());
        assertEquals(operation.getDate(), operationDTO.date());
        assertEquals(operation.getAccount().getId(), operationDTO.accountId());
    }

    @Test
    void fromOperation() {
        Account account = new Account();
        account.setId("accountId");
        Operation operation = Operation.builder()
                .id("id")
                .amount(BigDecimal.valueOf(5000))
                .currency(Currency.EUR)
                .type(OperationType.CREDIT)
                .account(account)
                .date(LocalDateTime.now())
                .description("DESCRIPTION")
                .build();

        OperationDTO operationDTO = mappers.fromOperation(operation);
        assertNotNull(operation);
        assertEquals(operation.getId(), operationDTO.id());
        assertEquals(operation.getCurrency(), operationDTO.currency());
        assertEquals(operation.getDescription(), operationDTO.description());
        assertEquals(operation.getType(), operationDTO.type());
        assertEquals(operation.getAmount(), operationDTO.amount());
        assertEquals(operation.getCurrency(), operationDTO.currency());
        assertEquals(operation.getDate(), operationDTO.date());
        assertEquals(operation.getAccount().getId(), operationDTO.accountId());
    }
}