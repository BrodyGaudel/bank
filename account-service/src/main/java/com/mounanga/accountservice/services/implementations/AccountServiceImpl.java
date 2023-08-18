package com.mounanga.accountservice.services.implementations;

import com.mounanga.accountservice.dtos.*;
import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.enums.Status;
import com.mounanga.accountservice.exceptions.AccountNotActivatedException;
import com.mounanga.accountservice.exceptions.AccountNotFoundException;
import com.mounanga.accountservice.exceptions.AlreadyExistException;
import com.mounanga.accountservice.exceptions.CustomerNotFoundException;
import com.mounanga.accountservice.mappers.Mappers;
import com.mounanga.accountservice.repositories.AccountRepository;
import com.mounanga.accountservice.restclients.CustomerRestClient;
import com.mounanga.accountservice.services.AccountService;
import com.mounanga.accountservice.utils.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    private static final String ACCOUNT_NOT_FOUND = "account not found";

    private final AccountRepository accountRepository;
    private final IdGenerator idGenerator;
    private final Mappers mappers;
    private final CustomerRestClient customerRestClient;

    public AccountServiceImpl(AccountRepository accountRepository, IdGenerator idGenerator, Mappers mappers, CustomerRestClient customerRestClient) {
        this.accountRepository = accountRepository;
        this.idGenerator = idGenerator;
        this.mappers = mappers;
        this.customerRestClient = customerRestClient;
    }

    /**
     * Creates a new account based on the provided account information.
     *
     * @param accountDTO The {@link AccountDTO} containing account details to create.
     * @return The newly created {@link AccountDTO}.
     * @throws CustomerNotFoundException If the associated customer is not found.
     * @throws AlreadyExistException If Customer already have an account
     */
    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) throws CustomerNotFoundException, AlreadyExistException {
        log.info("In createAccount()");
        if(accountDTO == null){
            throw new IllegalArgumentException("account must not be null. this happens because accoutDTO is null");
        }
        CustomerDTO customer = getCustomerById(accountDTO.customerId())
                .orElseThrow( () -> new CustomerNotFoundException(ACCOUNT_NOT_FOUND));

        if(Boolean.TRUE.equals(accountRepository.checkIfCustomerIdExists(customer.id()))){
            throw new AlreadyExistException("A customer with ID '" + customer.id() + "' already have an account");
        }

        Account account = new Account();
        account.setStatus(Status.CREATED);
        account.setCustomerId(customer.id());
        account.setCurrency(accountDTO.currency());
        account.setLastUpdate( null);

        if( (accountDTO.balance() == null) || (accountDTO.balance().compareTo(BigDecimal.ZERO) < 0)){
            account.setBalance( BigDecimal.valueOf(0));
        }else {
            account.setBalance(accountDTO.balance());
        }

        account.setId(
                idGenerator.autoGenerate()
        );
        account.setCreation( new Date());
        Account accountSaved = accountRepository.save(account);
        log.info("account saved successfully");
        return mappers.fromAccount(accountSaved);
    }

    /**
     * Retrieves an account by its unique identifier.
     *
     * @param id The unique identifier of the account.
     * @return The {@link AccountDTO} associated with the given ID.
     * @throws AccountNotFoundException If the account with the specified ID is not found.
     */
    @Override
    public AccountDTO getAccountById(String id) throws AccountNotFoundException {
        log.info("In getAccountById()");
        Account account = accountRepository.findById(id)
                .orElseThrow( () -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));
        log.info("account found");
        return mappers.fromAccount(account);
    }

    /**
     * Retrieves an account by its associated customer's unique identifier.
     *
     * @param customerId The unique identifier of the customer associated with the account.
     * @return The {@link AccountDTO} associated with the given customer ID.
     * @throws AccountNotFoundException If no account is found for the specified customer ID.
     */
    @Override
    public AccountDTO getAccountByCustomerId(String customerId) throws AccountNotFoundException {
        log.info("In getAccountByCustomerId()");
        Account account = accountRepository.findByCustomerId(customerId);
        if(account == null){
            throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);
        }
        log.info("account found");
        return mappers.fromAccount(account);
    }

    /**
     * Modifies the balance of an account based on the provided update form.
     *
     * @param form The {@link UpdateBalanceForm} containing account ID and balance update details.
     * @return The updated {@link AccountDTO}.
     * @throws AccountNotFoundException If the account specified in the update form is not found.
     * @throws AccountNotActivatedException If account status is not set to ACTIVATED
     */
    @Override
    public AccountDTO modifyAccountBalance(UpdateBalanceForm form) throws AccountNotFoundException, AccountNotActivatedException {
        log.info("In modifyAccountBalance()");
        if(form == null){
            throw new IllegalArgumentException("form must not be null");
        }
        Account account = accountRepository.findById(form.accountId())
                .orElseThrow( () -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));

        if(account.getStatus().equals(Status.SUSPENDED) || account.getStatus().equals(Status.BLOCKED) || account.getStatus().equals(Status.CREATED)){
            throw  new AccountNotActivatedException("Account with ID '" + account.getId() + "' is not activated. Actual status is '"+account.getStatus() + " '.'");
        }
        if(form.amount() == null){
            account.setBalance( BigDecimal.valueOf(0));
        }else {
            account.setBalance(form.amount());
        }
        account.setLastUpdate( new Date());
        Account accountUpdated = accountRepository.save(account);
        log.info("account updated successfully");
        return mappers.fromAccount(accountUpdated);
    }


    /**
     * Modifies the status of an account based on the provided update form.
     *
     * @param form The {@link UpdateStatusForm} containing account ID and status update details.
     * @return The updated {@link AccountDTO}.
     * @throws AccountNotFoundException If the account specified in the update form is not found.
     */
    @Override
    public AccountDTO modifyAccountStatus(UpdateStatusForm form) throws AccountNotFoundException {
        log.info("In modifyAccountStatus()");
        if(form == null){
            throw new IllegalArgumentException("form must not be null");
        }
        Account account = accountRepository.findById(form.accountId())
                .orElseThrow( () -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));
        account.setStatus(form.status());
        account.setLastUpdate( new Date());
        Account accountUpdated = accountRepository.save(account);
        log.info("account updated successfully");
        return mappers.fromAccount(accountUpdated);
    }

    /**
     * Deletes an account based on its unique identifier.
     *
     * @param id The unique identifier of the account to be deleted.
     */
    @Override
    public void deleteAccountById(String id) {
        log.info("In deleteAccountById()");
        accountRepository.deleteById(id);
    }

    /**
     * Retrieves customer information by its unique identifier.
     *
     * @param id The unique identifier of the customer.
     * @return The {@link CustomerDTO} associated with the given ID, or {@code null} if an exception occurs.
     */
    private Optional<CustomerDTO> getCustomerById(String id) {
        try{
            CustomerDTO customerDTO = customerRestClient.getCustomerById(id);
            return Optional.of(customerDTO);
        }catch (Exception e){
            return Optional.empty();
        }

    }


}
