package com.brodygaudel.accountservice.service.implementations;

import com.brodygaudel.accountservice.dtos.AccountDTO;
import com.brodygaudel.accountservice.dtos.CustomerDTO;
import com.brodygaudel.accountservice.entities.Account;
import com.brodygaudel.accountservice.entities.Compter;
import com.brodygaudel.accountservice.enums.AccountStatus;
import com.brodygaudel.accountservice.exceptions.AccountNotFoundException;
import com.brodygaudel.accountservice.exceptions.CustomerNotFoundException;
import com.brodygaudel.accountservice.mappers.Mappers;
import com.brodygaudel.accountservice.repositories.AccountRepository;
import com.brodygaudel.accountservice.repositories.CompterRepository;
import com.brodygaudel.accountservice.service.AccountService;
import com.brodygaudel.accountservice.web.restclients.CustomerRestClient;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final Mappers mappers;
    private final CustomerRestClient customerRestClient;
    private final CompterRepository compterRepository;

    public AccountServiceImpl(AccountRepository accountRepository, Mappers mappers, CustomerRestClient customerRestClient, CompterRepository compterRepository) {
        this.accountRepository = accountRepository;
        this.mappers = mappers;
        this.customerRestClient = customerRestClient;
        this.compterRepository = compterRepository;
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
     *
     */
    @Transactional
    @Override
    public AccountDTO save(@NotNull AccountDTO accountDTO) throws CustomerNotFoundException {
        log.info("In save() :");
        CustomerDTO customer = getCustomerById(accountDTO.customerId());
        if(customer == null){
            throw new CustomerNotFoundException("Customer with id '"+accountDTO.customerId()+"not found");
        }
        String accountId = autoGenerate();
        Account account = Account.builder()
                .customerId(customer.id()).id(accountId).operations( new ArrayList<>())
                .balance(BigDecimal.valueOf(0)).status(AccountStatus.ACTIVATED)
                .currency(accountDTO.currency()).lastUpdate(null).creation(LocalDateTime.now())
                .build();
        Account accountSaved = accountRepository.save(account);
        log.info("account created successfully with id '"+accountSaved.getId()+"'.");
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
     * automatically generate a unique bank account number
     *
     * @return id a unique bank account number
     */
    private @NotNull String autoGenerate() {
        List<Compter> compters = compterRepository.findAll();
        Compter compter;
        if(compters.isEmpty()) {
            compter = new Compter(9999999L);
        }
        else {
            compter = compters.get(compters.size() - 1);
            compterRepository.deleteById(compter.getId());
        }
        Long increment = compter.getId()+1;
        compterRepository.save(new Compter(increment));
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        return currentDate.format(formatter)+increment;
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
