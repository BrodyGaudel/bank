package com.mounanga.accountservice.repositories;

import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.builders.AccountBuilder;
import com.mounanga.accountservice.enums.Currency;
import com.mounanga.accountservice.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    void testFindByCustomerId() {

        Account account = accountRepository.save(
                new AccountBuilder()
                        .id("id")
                        .customerId("customerId")
                        .lastUpdate( new Date())
                        .balance(BigDecimal.valueOf(1000))
                        .currency(Currency.EUR)
                        .status(Status.ACTIVATED)
                        .build()
        );

        Account response = accountRepository.findByCustomerId(account.getCustomerId());
        assertNotNull(account);
        assertEquals(response.getCustomerId(), account.getCustomerId());
        assertEquals(response.getId(), account.getId());
    }

    @Test
    void testCheckIfCustomerIdExists() {
        Account response = accountRepository.save(
                new AccountBuilder()
                        .id("id")
                        .customerId("customerId")
                        .lastUpdate( new Date())
                        .balance(BigDecimal.valueOf(1000))
                        .currency(Currency.EUR)
                        .status(Status.ACTIVATED)
                        .build()
        );

        Boolean check1 = accountRepository.checkIfCustomerIdExists(response.getCustomerId());
        Boolean check2 = accountRepository.checkIfCustomerIdExists("check");

        assertEquals(Boolean.TRUE, check1);
        assertNotEquals(Boolean.TRUE, check2);
    }
}