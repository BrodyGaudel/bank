package com.brodygaudel.accountservice.service.implementation;

import com.brodygaudel.accountservice.controller.CustomerRestClient;
import com.brodygaudel.accountservice.service.AccountService;

import com.brodygaudel.accountservice.dto.AccountDTO;
import com.brodygaudel.accountservice.dto.CustomerDTO;
import com.brodygaudel.accountservice.entity.Account;
import com.brodygaudel.accountservice.enums.AccountStatus;
import com.brodygaudel.accountservice.exception.AccountNotFoundException;
import com.brodygaudel.accountservice.exception.CustomerAlreadyHaveAccountException;
import com.brodygaudel.accountservice.exception.CustomerNotFoundException;
import com.brodygaudel.accountservice.repository.AccountRepository;


import com.brodygaudel.accountservice.util.IdGenerator;
import com.brodygaudel.accountservice.util.Mappers;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final Mappers mappers;
    private final CustomerRestClient customerRestClient;
    private final IdGenerator idGenerator;

    public AccountServiceImpl(AccountRepository accountRepository, Mappers mappers, CustomerRestClient customerRestClient, IdGenerator idGenerator) {
        this.accountRepository = accountRepository;
        this.mappers = mappers;
        this.customerRestClient = customerRestClient;
        this.idGenerator = idGenerator;
    }

    /**
     * Retrieve an account by its ID.
     *
     * @param id The ID of the account to retrieve.
     * @return The account details as a Data Transfer Object (DTO).
     * @throws AccountNotFoundException If the account with the given ID is not found.
     */
    @Override
    public AccountDTO getById(String id) throws AccountNotFoundException {
        log.info("In getById() :");
        Account account = accountRepository.findById(id)
                .orElseThrow( () -> new AccountNotFoundException("Account not found with id '"+id+"'."));
        log.info("account found with id '"+id+"' found successfully.");
        return mappers.fromAccount(account);
    }

    /**
     * Retrieve an account by its customer ID.
     *
     * @param customerId The customer ID associated with the account.
     * @return The account details as a Data Transfer Object (DTO).
     * @throws AccountNotFoundException If the account for the given customer ID is not found.
     */
    @Override
    public AccountDTO getByCustomerId(String customerId) throws AccountNotFoundException {
        log.info("In getByCustomerId() :");
        Account account = accountRepository.findByCustomerId(customerId);
        if(account == null){
            throw new AccountNotFoundException("Account not found with customerId '"+customerId+"'.");
        }
        log.info("account found successfully with customerId '"+customerId+"'.");
        return mappers.fromAccount(account);
    }

    /**
     * Create a new account.
     *
     * @param accountDTO The account details to create.
     * @return The created account details as a Data Transfer Object (DTO).
     * @throws CustomerNotFoundException If the customer associated with the account is not found.
     * @throws CustomerAlreadyHaveAccountException If customer already have an account
     */
    @Transactional
    @Override
    public AccountDTO save(@NotNull AccountDTO accountDTO) throws CustomerNotFoundException, CustomerAlreadyHaveAccountException {
        log.info("In save() :");
        CustomerDTO customer = getCustomerById(accountDTO.customerId());
        if(customer == null){
            throw new CustomerNotFoundException("Customer with id '"+accountDTO.customerId()+"not found");
        }
        if(Boolean.TRUE.equals(accountRepository.checkIfCustomerIdExists(customer.id()))){
            throw new CustomerAlreadyHaveAccountException("this customer already have an account");
        }
        String accountId = idGenerator.autoGenerate();
        Account account = Account.builder()
                .customerId(customer.id()).id(accountId).operations( new ArrayList<>())
                .balance(BigDecimal.valueOf(0)).status(AccountStatus.ACTIVATED)
                .currency(accountDTO.currency()).lastUpdate(null).creation(LocalDateTime.now())
                .build();
        Account accountSaved = accountRepository.save(account);
        log.info("account created successfully");
        return mappers.fromAccount(accountSaved);
    }


    /**
     * Delete an account by its ID.
     *
     * @param id The ID of the account to delete.
     */
    @Override
    public void deleteById(String id) {
        log.info("In deleteById() :");
        accountRepository.deleteById(id);
        log.info("account deleted successfully");
    }

    /**
     * Update Account Status
     *
     * @param accountDTO The account details to update status
     * @return The Updated Account details as a Data Transfer Object (DTO).
     * @throws AccountNotFoundException If account not found
     */
    @Transactional
    @Override
    public AccountDTO updateStatus(@NotNull AccountDTO accountDTO) throws AccountNotFoundException {
        log.info("In updateStatus() :");
        Account account = accountRepository.findById(accountDTO.id())
                .orElseThrow(() -> new AccountNotFoundException("Account you try to update status not found. id '"+accountDTO.id()+"'."));
        account.setStatus(accountDTO.status());
        account.setLastUpdate(LocalDateTime.now());
        Account accountUpdated = accountRepository.save(account);
        log.info("account updated successfully");
        return mappers.fromAccount(accountUpdated);
    }


    /**
     * Retrieves a customer's details by their unique ID using the CustomerRestClient.
     *
     * <p>This method attempts to retrieve a {@code CustomerDTO} by invoking the {@code getById} method
     * of the {@code CustomerRestClient}. If successful, it returns the obtained customer details;
     * otherwise, it logs an error and returns {@code null}.</p>
     *
     * <p>Author: Brody Gaudel</p>
     *
     * @param customerId The unique identifier of the customer to retrieve.
     * @return The {@code CustomerDTO} containing the customer details, or {@code null} if not found.
     */
    private @Nullable CustomerDTO getCustomerById(String customerId) {
        log.info("In getCustomerById()");
        try{
            CustomerDTO customerDTO = customerRestClient.getById(customerId);
            log.info("customer found");
            return customerDTO;
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

}
