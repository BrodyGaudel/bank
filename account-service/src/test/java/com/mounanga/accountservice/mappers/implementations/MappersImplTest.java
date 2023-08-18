package com.mounanga.accountservice.mappers.implementations;

import com.mounanga.accountservice.dtos.AccountDTO;
import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.builders.AccountBuilder;
import com.mounanga.accountservice.enums.Currency;
import com.mounanga.accountservice.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MappersImplTest {

    private MappersImpl mappers;

    @BeforeEach
    void setUp() {
        mappers = new MappersImpl();
    }

    @Test
    void testFromAccountDTO() {
        AccountDTO  accountDTO = new AccountDTO(
                "id",
                Currency.EUR,
                BigDecimal.valueOf(1000),
                Status.CREATED,
                "customerId",
                new Date(),
                new Date()
        );

        Account account = mappers.fromAccountDTO(accountDTO);
        Account account2 = mappers.fromAccountDTO(null);

        assertNotNull(account);
        assertEquals(accountDTO.id(), account.getId());
        assertEquals(accountDTO.currency(), account.getCurrency());
        assertEquals(accountDTO.balance(), account.getBalance());
        assertEquals(accountDTO.status(), account.getStatus());
        assertEquals(accountDTO.customerId(), account.getCustomerId());
        assertEquals(accountDTO.lastUpdate(), account.getLastUpdate());
        assertNull(account2);
    }

    @Test
    void testFromAccount() {
        Account account = new AccountBuilder()
                .customerId("customerId")
                .lastUpdate( new Date())
                .balance(BigDecimal.valueOf(1000))
                .currency(Currency.EUR)
                .status(Status.ACTIVATED)
                .build();

        AccountDTO accountDTO = mappers.fromAccount(account);
        AccountDTO accountDTO2 = mappers.fromAccount(null);

        assertNotNull(account);
        assertEquals(accountDTO.id(), account.getId());
        assertEquals(accountDTO.currency(), account.getCurrency());
        assertEquals(accountDTO.balance(), account.getBalance());
        assertEquals(accountDTO.status(), account.getStatus());
        assertEquals(accountDTO.customerId(), account.getCustomerId());
        assertEquals(accountDTO.lastUpdate(), account.getLastUpdate());
        assertNull(accountDTO2);
    }
}