package com.mounanga.accountservice.services.implementations;

import com.mounanga.accountservice.dtos.AccountDTO;
import com.mounanga.accountservice.dtos.UpdateBalanceForm;
import com.mounanga.accountservice.dtos.UpdateStatusForm;
import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.builders.AccountBuilder;
import com.mounanga.accountservice.enums.Currency;
import com.mounanga.accountservice.enums.Status;
import com.mounanga.accountservice.exceptions.AccountNotActivatedException;
import com.mounanga.accountservice.exceptions.AccountNotFoundException;
import com.mounanga.accountservice.mappers.implementations.MappersImpl;
import com.mounanga.accountservice.repositories.AccountRepository;
import com.mounanga.accountservice.repositories.CompterRepository;
import com.mounanga.accountservice.restclients.CustomerRestClient;
import com.mounanga.accountservice.utils.implementations.IdGeneratorImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceImplTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CompterRepository compterRepository;

    @Autowired
    private CustomerRestClient customerRestClient;

    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        IdGeneratorImpl idGenerator = new IdGeneratorImpl(compterRepository);
        accountService = new AccountServiceImpl(
                accountRepository,
                idGenerator,
                new MappersImpl(),
                customerRestClient
        );
        accountRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void testGetAccountById() throws AccountNotFoundException {
        Account account = accountRepository.save(
                new AccountBuilder()
                        .id("1234567876543210")
                        .lastUpdate(new Date())
                        .status(Status.ACTIVATED)
                        .balance(BigDecimal.valueOf(5222))
                        .currency(Currency.EUR)
                        .customerId("1101-1111-0100-000")
                        .build()
        );

        AccountDTO accountDTO = accountService.getAccountById(account.getId());
        assertNotNull(accountDTO);
        assertEquals(account.getId(), accountDTO.id());
    }

    @Test
    void testGetAccountByCustomerId() throws AccountNotFoundException {
        Account account = accountRepository.save(
                new AccountBuilder()
                        .id("0123456787654321")
                        .lastUpdate(new Date())
                        .status(Status.ACTIVATED)
                        .balance(BigDecimal.valueOf(5222))
                        .currency(Currency.EUR)
                        .customerId("1111-1111-0000-000")
                        .build()
        );
        AccountDTO accountDTO = accountService.getAccountByCustomerId(account.getCustomerId());
        assertNotNull(accountDTO);
        assertEquals(account.getId(), accountDTO.id());
        assertEquals(account.getCustomerId(), accountDTO.customerId());
    }

    @Test
    void testModifyAccountBalance() throws AccountNotFoundException, AccountNotActivatedException {
        Account account = accountRepository.save(
                new AccountBuilder()
                        .id("0123456777654321")
                        .lastUpdate(new Date())
                        .status(Status.ACTIVATED)
                        .balance(BigDecimal.valueOf(5222))
                        .currency(Currency.EUR)
                        .customerId("1111-1110-0000-000")
                        .build()
        );
        AccountDTO accountDTO = accountService.modifyAccountBalance(
               new UpdateBalanceForm(
                       account.getId(),
                       BigDecimal.valueOf(1222)
               )
        );
        assertNotNull(accountDTO);
        assertEquals(account.getId(), accountDTO.id());
        assertNotEquals(account.getBalance(), accountDTO.balance());
    }

    @Test
    void testModifyAccountStatus() throws AccountNotFoundException {
        Account account = accountRepository.save(
                new AccountBuilder()
                        .id("0123856777654321")
                        .lastUpdate(new Date())
                        .status(Status.ACTIVATED)
                        .balance(BigDecimal.valueOf(5222))
                        .currency(Currency.EUR)
                        .customerId("1111-1110-0010-0011")
                        .build()
        );
        AccountDTO accountDTO = accountService.modifyAccountStatus(
                new UpdateStatusForm(
                        account.getId(), Status.SUSPENDED
                )
        );
        assertNotNull(accountDTO);
        assertEquals(account.getId(), accountDTO.id());
        assertNotEquals(account.getStatus(), accountDTO.status());
    }

    @Test
    void testDeleteAccountById() {
        Account account = accountRepository.save(
                new AccountBuilder()
                        .id("0123456077651321")
                        .lastUpdate(new Date())
                        .status(Status.ACTIVATED)
                        .balance(BigDecimal.valueOf(5222))
                        .currency(Currency.EUR)
                        .customerId("1011-1110-0100-000")
                        .build()
        );
        accountService.deleteAccountById(account.getId());
        Account response = accountRepository.findById(account.getId()).orElse(null);
        assertNull(response);
    }
}