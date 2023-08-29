package com.mounanga.customerservice.services.implementations;

import com.mounanga.customerservice.dtos.CustomerDTO;
import com.mounanga.customerservice.entities.Customer;
import com.mounanga.customerservice.exceptions.AlreadyExistException;
import com.mounanga.customerservice.exceptions.CustomerNotFoundException;
import com.mounanga.customerservice.mappers.Mappers;
import com.mounanga.customerservice.repositories.CustomerRepository;
import com.mounanga.customerservice.services.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private static final String CUSTOMER_FOUND = "customer(s) found";
    private static final String CUSTOMER_NOT_FOUND = "customer(s) not found";
    private static final String ALREADY_EXIST = "' already exists.";

    private final CustomerRepository customerRepository;
    private final Mappers mappers;

    public CustomerServiceImpl(CustomerRepository customerRepository, Mappers mappers) {
        this.customerRepository = customerRepository;
        this.mappers = mappers;
    }


    /**
     * Retrieves a customer by their unique identifier.
     *
     * @param id The unique identifier of the customer.
     * @return The {@link CustomerDTO} representing the retrieved customer.
     * @throws CustomerNotFoundException If the customer with the given ID does not exist.
     */
    @Override
    public CustomerDTO getCustomerById(String id) throws CustomerNotFoundException {
        log.info("In getCustomerById()");
        Customer customer = customerRepository.findById(id)
                .orElseThrow( () -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND));
        log.info(CUSTOMER_FOUND);
        return mappers.fromCustomer(customer);
    }

    /**
     * Retrieves a customer by their CIN number.
     *
     * @param cin The CIN number of the customer.
     * @return The {@link CustomerDTO} representing the retrieved customer.
     * @throws CustomerNotFoundException If a customer with the given CIN number does not exist.
     */
    @Override
    public CustomerDTO getCustomerByCin(String cin) throws CustomerNotFoundException {
        log.info("In getCustomerByCin()");
        Customer customer = customerRepository.findByCin(cin);
        if(customer == null){
            throw new CustomerNotFoundException(CUSTOMER_NOT_FOUND);
        }
        log.info(CUSTOMER_FOUND);
        return mappers.fromCustomer(customer);
    }

    /**
     * Retrieves a list of customers with pagination.
     *
     * @param size The number of customers per page.
     * @param page The page number.
     * @return A list of {@link CustomerDTO} representing the retrieved customers.
     */
    @Override
    public List<CustomerDTO> getAllCustomers(int size, int page) {
        log.info("In getAllCustomers()");
        Page<Customer> customers = customerRepository.getAllByPage(PageRequest.of(page,size));
        if(customers.isEmpty()){
            log.info(CUSTOMER_NOT_FOUND);
            return Collections.emptyList();
        }
        log.info(CUSTOMER_FOUND);
        return customers.getContent()
                .stream()
                .map(mappers::fromCustomer)
                .toList();
    }

    /**
     * Searches for customers based on a keyword with pagination.
     *
     * @param keyword The search keyword.
     * @param size    The number of customers per page.
     * @param page    The page number.
     * @return A list of {@link CustomerDTO} representing the searched customers.
     */
    @Override
    public List<CustomerDTO> searchCustomers(String keyword, int size, int page) {
        log.info("In searchCustomers()");
        Page<Customer> customers = customerRepository.searchByFirstnameOrName(keyword, PageRequest.of(page, size));
        if (customers.isEmpty()){
            log.info(CUSTOMER_NOT_FOUND);
            return Collections.emptyList();
        }
        log.info(CUSTOMER_FOUND);
        return customers.getContent()
                .stream()
                .map(mappers::fromCustomer)
                .toList();
    }

    /**
     * Saves a new customer.
     *
     * @param customerDTO The {@link CustomerDTO} containing the customer information to be saved.
     * @return The {@link CustomerDTO} representing the saved customer.
     * @throws AlreadyExistException If the CIN, email, or phone number of the customer already exists.
     */
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) throws AlreadyExistException {
        log.info("In saveCustomer()");
        if (customerDTO == null) {
            throw new IllegalArgumentException("customerDTO must not be null");
        }
        checkIfCustomerExists(customerDTO);
        Customer customer = mappers.fromCustomerDTO(customerDTO);
        if (customer == null) {
            throw new IllegalArgumentException("customer must not be null, check mappers");
        }

        customer.setLastUpdate(null);
        customer.setId(UUID.randomUUID().toString());
        Customer customerSaved = customerRepository.save(customer);
        log.info("Customer saved successfully");
        return mappers.fromCustomer(customerSaved);
    }


    /**
     * Updates an existing customer's information.
     *
     * @param id          The unique identifier of the customer to be updated.
     * @param customerDTO The {@link CustomerDTO} containing the updated customer information.
     * @return The {@link CustomerDTO} representing the updated customer.
     * @throws CustomerNotFoundException If the customer with the given ID does not exist.
     * @throws AlreadyExistException     If the updated CIN, email, or phone number already exists for another customer.
     */
    @Override
    public CustomerDTO updateCustomer(String id, CustomerDTO customerDTO) throws CustomerNotFoundException, AlreadyExistException {
        log.info("In updateCustomer()");
        Customer customer = customerRepository.findById(id)
                .orElseThrow( () -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND));
        if (customerDTO == null){
            throw new IllegalArgumentException("customerDTO must not be null");
        }
        checkEmailPhoneAndCinBeforeUpdate(customer, customerDTO);

        customer.setName(customerDTO.name());
        customer.setFirstname(customerDTO.firstname());
        customer.setCin(customerDTO.cin());
        customer.setPhone(customerDTO.nationality());
        customer.setEmail(customerDTO.email());
        customer.setNationality(customerDTO.nationality());
        customer.setDateOfBirth(customerDTO.dateOfBirth());
        customer.setPlaceOfBirth(customerDTO.placeOfBirth());
        customer.setSex(customerDTO.sex());
        customer.setLastUpdate( new Date());

        Customer customerUpdated = customerRepository.save(customer);
        log.info("customer updated successfully");
        return mappers.fromCustomer(customerUpdated);
    }

    /**
     * Deletes a customer by their unique identifier.
     *
     * @param id The unique identifier of the customer to be deleted.
     */
    @Override
    public void deleteCustomerById(String id) {
        log.info("In deleteCustomerById()");
        customerRepository.deleteById(id);
        log.info("customer deleted successfully");
    }

    /**
     * check if customer already exist in database before save it.
     * @param customerDTO The new customer information request
     * @throws AlreadyExistException if customer's phone, cin or email already exist
     */
    private void checkIfCustomerExists(CustomerDTO customerDTO) throws AlreadyExistException {
        if (customerDTO != null){
            if(Boolean.TRUE.equals(customerRepository.checkIfCinExists(customerDTO.cin()))){
                throw new AlreadyExistException("A customer with CIN '" + customerDTO.cin() + ALREADY_EXIST);
            } else if (Boolean.TRUE.equals(customerRepository.checkIfEmailExists(customerDTO.email()))) {
                throw new AlreadyExistException("A customer with email '" + customerDTO.email() + ALREADY_EXIST);
            } else if (Boolean.TRUE.equals(customerRepository.checkIfPhoneExists(customerDTO.phone()))) {
                throw new AlreadyExistException("A customer with phone number '" + customerDTO.phone() + ALREADY_EXIST);
            }
        }
    }

    /**
     * Checks CIN, email, and phone number information before updating a customer.
     *
     * @param customer The existing customer whose information is currently stored.
     * @param customerDTO The new customer information in the form of a Data Transfer Object (DTO).
     * @throws AlreadyExistException If a match is found in the database for the provided CIN, email, or phone number.
     */
    private void checkEmailPhoneAndCinBeforeUpdate(Customer customer, CustomerDTO customerDTO) throws AlreadyExistException {
        if (customer != null && customerDTO != null) {
            if (!customer.getCin().equals(customerDTO.cin()) && (Boolean.TRUE.equals(customerRepository.checkIfCinExists(customerDTO.cin())))) {
                throw new AlreadyExistException("A customer with CIN '" + customerDTO.cin() + ALREADY_EXIST);
            } else if (!customer.getEmail().equals(customerDTO.email()) && (Boolean.TRUE.equals(customerRepository.checkIfEmailExists(customerDTO.email())))) {
                throw new AlreadyExistException("A customer with email '" + customerDTO.email() + ALREADY_EXIST);
            } else if (!customer.getPhone().equals(customerDTO.phone()) && (Boolean.TRUE.equals(customerRepository.checkIfPhoneExists(customerDTO.phone())))) {
                throw new AlreadyExistException("A customer with phone number '" + customerDTO.phone() + ALREADY_EXIST);
            }
        }
    }


}
