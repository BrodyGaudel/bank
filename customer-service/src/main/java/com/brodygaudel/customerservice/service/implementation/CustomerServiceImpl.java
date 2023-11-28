package com.brodygaudel.customerservice.service.implementation;

import com.brodygaudel.customerservice.dto.CustomerDTO;
import com.brodygaudel.customerservice.dto.CustomerPageDTO;
import com.brodygaudel.customerservice.entity.Customer;
import com.brodygaudel.customerservice.exception.CinAlreadyExistException;
import com.brodygaudel.customerservice.exception.CustomerNotFoundException;
import com.brodygaudel.customerservice.exception.EmailAlreadyExistException;
import com.brodygaudel.customerservice.exception.PhoneAlreadyExistException;
import com.brodygaudel.customerservice.repository.CustomerRepository;
import com.brodygaudel.customerservice.service.CustomerService;
import com.brodygaudel.customerservice.util.Mappers;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * The {@code CustomerServiceImpl} class is an implementation of the {@link CustomerService} interface
 * that provides functionality for managing customer-related operations. This service class integrates
 * with the underlying data access layer through the {@link CustomerRepository} and utilizes the
 * {@link Mappers} for mapping between entities and DTOs.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * try {
 *     // Code that calls methods from CustomerServiceImpl
 * } catch (CustomerNotFoundException | CinAlreadyExistException | EmailAlreadyExistException | PhoneAlreadyExistException e) {
 *     // Handle exceptions thrown by the service methods, possibly providing user-friendly error messages
 *     // or taking appropriate actions based on the specific exception type.
 * }
 * }
 * </pre>
 * </p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 *
 * @see CustomerService
 * @see CustomerRepository
 * @see Mappers
 * @see CinAlreadyExistException
 * @see EmailAlreadyExistException
 * @see PhoneAlreadyExistException
 * @see CustomerNotFoundException
 * @see CustomerDTO
 * @see CustomerPageDTO
 */
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {


    private static final String ALREADY_EXIST = "' already exist.";
    private static final String CUSTOMER_WITH_EMAIL = "A customer with email :'";
    private static final String CUSTOMER_WITH_PHONE = "A customer with phone :'";
    private static final String CUSTOMER_WITH_CIN = "A customer with cin :'";

    private final CustomerRepository customerRepository;
    private final Mappers mappers;

    public CustomerServiceImpl(CustomerRepository customerRepository, Mappers mappers) {
        this.customerRepository = customerRepository;
        this.mappers = mappers;
    }

    /**
     * Saves a customer using the provided {@code CustomerDTO}.
     *
     * @param customerDTO The {@code CustomerDTO} containing customer information to be saved.
     * @return The saved {@code CustomerDTO}.
     * @throws CinAlreadyExistException   If the customer's CIN already exists in the system.
     * @throws EmailAlreadyExistException If the customer's email already exists in the system.
     * @throws PhoneAlreadyExistException If the customer's phone number already exists in the system.
     */
    @Transactional
    @Override
    public CustomerDTO save(CustomerDTO customerDTO) throws CinAlreadyExistException, EmailAlreadyExistException, PhoneAlreadyExistException {
        log.info("In save() :");
        checkIfCinOrEmailOrPhoneExistBeforeSave(customerDTO);
        Customer customer = mappers.fromCustomerDTO(customerDTO);
        customer.setLastUpdate(null);
        customer.setId(UUID.randomUUID().toString());
        customer.setCreation(LocalDateTime.now());
        Customer customerSaved = customerRepository.save(customer);
        log.info("customer saved successfully");
        return mappers.fromCustomer(customerSaved);
    }

    /**
     * Updates a customer identified by the given ID with the provided {@code CustomerDTO}.
     *
     * @param id          The ID of the customer to be updated.
     * @param customerDTO The {@code CustomerDTO} containing updated customer information.
     * @return The updated {@code CustomerDTO}.
     * @throws CustomerNotFoundException  If the customer with the specified ID is not found.
     * @throws CinAlreadyExistException   If the updated CIN already exists in the system.
     * @throws EmailAlreadyExistException If the updated email already exists in the system.
     * @throws PhoneAlreadyExistException If the updated phone number already exists in the system.
     */
    @Transactional
    @Override
    public CustomerDTO update(String id, CustomerDTO customerDTO) throws CustomerNotFoundException, CinAlreadyExistException, EmailAlreadyExistException, PhoneAlreadyExistException {
        log.info("In update() :");
        Customer customer = customerRepository.findById(id)
                .orElseThrow( ()-> new CustomerNotFoundException("customer you try to update with id = '"+id+"' not found."));
        checkIfCinOrEmailOrPhoneExistBeforeUpdate(customer, customerDTO);
        updateCustomerFields(customer, customerDTO);
        Customer customerUpdate = customerRepository.save(customer);
        log.info("Customer updated successfully");
        return mappers.fromCustomer(customerUpdate);
    }

    /**
     * Retrieves a customer by the given ID.
     *
     * @param id The ID of the customer to be retrieved.
     * @return The {@code CustomerDTO} representing the retrieved customer.
     * @throws CustomerNotFoundException If the customer with the specified ID is not found.
     */
    @Override
    public CustomerDTO getById(String id) throws CustomerNotFoundException {
        log.info("In getById() :");
        Customer customer = customerRepository.findById(id)
                .orElseThrow( ()-> new CustomerNotFoundException("customer with id = '"+id+"' not found."));
        log.info("customer with id = '"+id+"' found successfully.");
        return mappers.fromCustomer(customer);
    }

    /**
     * Retrieves a list of customer pages.
     *
     * @param size int
     * @param page int
     * @return A list of {@code CustomerPageDTO} representing customer pages.
     */
    @Override
    public CustomerPageDTO getAll(int size, int page) {
        log.info("In getAll() :");
        Page<Customer> customers = customerRepository.getAllByPage(PageRequest.of(page, size));
        if(customers.getContent().isEmpty()){
            return new CustomerPageDTO(page, size, 0, Collections.emptyList());
        }else{
            List<CustomerDTO> customerDTOList = mappers.fromListOfCustomers(customers.getContent());
            log.info("all customers found");
            return new CustomerPageDTO(page, size, customers.getTotalPages(), customerDTOList);
        }

    }

    /**
     * Searches for customer pages based on the provided keyword.
     *
     * @param keyword The keyword to be used for searching.
     * @param page int
     * @param size int
     * @return A list of {@code CustomerPageDTO} representing the search results.
     */
    @Override
    public CustomerPageDTO search(String keyword, int page, int size) {
        log.info("In search() :");
        Page<Customer> customers = customerRepository
                .searchByFirstnameOrNameOrCin("%"+keyword+"%", PageRequest.of(page, size));

        if(customers.getContent().isEmpty()){
            return new CustomerPageDTO(page, size, 0, Collections.emptyList());
        }else{
            List<CustomerDTO> customerDTOList = mappers.fromListOfCustomers(customers.getContent());
            log.info("all customers found for keyword :'"+keyword);
            return new CustomerPageDTO(page, size, customers.getTotalPages(), customerDTOList);
        }

    }

    /**
     * Deletes a customer by the given ID.
     *
     * @param id The ID of the customer to be deleted.
     */
    @Override
    public void deleteById(String id) {
        log.info("In deleteById() :");
        customerRepository.deleteById(id);
        log.info("customer deleted");
    }

    /**
     * Updates the fields of an existing Customer object with the information from a CustomerDTO.
     * This method takes a Customer and a CustomerDTO as parameters and updates the corresponding
     * fields of the Customer with the values from the CustomerDTO. The fields include first name,
     * last name, sex, place of birth, nationality, Cin, email, phone, and last update timestamp.
     *
     * @param customer The existing Customer object to be updated.
     * @param customerDTO The CustomerDTO object containing the new information.
     * @throws NullPointerException If either customer or customerDTO is null.
     */
    private void updateCustomerFields(@NotNull Customer customer, @NotNull CustomerDTO customerDTO){
        customer.setFirstname(customerDTO.firstname());
        customer.setName(customerDTO.name());
        customer.setSex(customerDTO.sex());
        customer.setPlaceOfBirth(customerDTO.placeOfBirth());
        customer.setNationality(customerDTO.nationality());
        customer.setCin(customerDTO.cin());
        customer.setEmail(customerDTO.email());
        customer.setPhone(customerDTO.phone());
        customer.setLastUpdate(LocalDateTime.now());
    }

    /**
     * Checks if the provided Cin, Email, or Phone already exists in the database before saving a new Customer.
     * This method queries the customer repository to verify if the Cin, Email, or Phone provided in the
     * CustomerDTO already exists in the database. If any of them is found to be already in use, corresponding
     * exceptions are thrown to indicate the conflict.
     *
     * @param customerDTO The CustomerDTO object containing the information to be checked before saving.
     * @throws CinAlreadyExistException If the provided Cin already exists in the database.
     * @throws EmailAlreadyExistException If the provided Email already exists in the database.
     * @throws PhoneAlreadyExistException If the provided Phone already exists in the database.
     * @throws NullPointerException If customerDTO is null.
     */
    private void checkIfCinOrEmailOrPhoneExistBeforeSave(@NotNull CustomerDTO customerDTO) throws CinAlreadyExistException, EmailAlreadyExistException, PhoneAlreadyExistException {
        checkIfCinExists(customerDTO.cin());
        checkIfEmailExists(customerDTO.email());
        checkIfPhoneExists(customerDTO.phone());
    }


    /**
     * Checks if the provided customer information conflicts with existing records in the database.
     * This method compares the Cin, Email, and Phone of the given Customer and CustomerDTO
     * and verifies if any of them already exists in the database. If a conflict is detected,
     * the corresponding exception is thrown.
     *
     * @param customer The existing Customer object to compare against.
     * @param customerDTO The CustomerDTO object containing the information to be checked.
     * @throws CinAlreadyExistException If the provided Cin already exists in the database.
     * @throws EmailAlreadyExistException If the provided Email already exists in the database.
     * @throws PhoneAlreadyExistException If the provided Phone already exists in the database.
     * @throws NullPointerException If either customer or customerDTO is null.
     */
    private void checkIfCinOrEmailOrPhoneExistBeforeUpdate(@NotNull Customer customer, @NotNull CustomerDTO customerDTO) throws CinAlreadyExistException, EmailAlreadyExistException, PhoneAlreadyExistException {
        if (!customer.getCin().equals(customerDTO.cin())){
            checkIfCinExists(customerDTO.cin());
        }
        if(!customer.getEmail().equals(customerDTO.email())){
            checkIfEmailExists(customerDTO.email());
        }
        if(!customer.getPhone().equals(customerDTO.phone())){
            checkIfPhoneExists(customerDTO.phone());
        }
    }

    /**
     * Checks if a Customer with the provided Cin already exists in the database.
     * This method queries the customer repository to verify if a Customer with the given Cin
     * already exists. If a match is found, a CinAlreadyExistException is thrown.
     *
     * @param cin The Cin to be checked for existence.
     * @throws CinAlreadyExistException If a Customer with the provided Cin already exists in the database.
     */
    private void checkIfCinExists(String cin) throws CinAlreadyExistException {
        if (Boolean.TRUE.equals(customerRepository.checkIfCinExists(cin))) {
            throw new CinAlreadyExistException(CUSTOMER_WITH_CIN + cin + ALREADY_EXIST);
        }
    }

    /**
     * Checks if a Customer with the provided Email already exists in the database.
     * This method queries the customer repository to verify if a Customer with the given Email
     * already exists. If a match is found, an EmailAlreadyExistException is thrown.
     *
     * @param email The Email to be checked for existence.
     * @throws EmailAlreadyExistException If a Customer with the provided Email already exists in the database.
     */
    private void checkIfEmailExists(String email) throws EmailAlreadyExistException {
        if (Boolean.TRUE.equals(customerRepository.checkIfEmailExists(email))) {
            throw new EmailAlreadyExistException(CUSTOMER_WITH_EMAIL + email + ALREADY_EXIST);
        }
    }

    /**
     * Checks if a Customer with the provided Phone already exists in the database.
     * This method queries the customer repository to verify if a Customer with the given Phone
     * already exists. If a match is found, a PhoneAlreadyExistException is thrown.
     *
     * @param phone The Phone to be checked for existence.
     * @throws PhoneAlreadyExistException If a Customer with the provided Phone already exists in the database.
     */
    private void checkIfPhoneExists(String phone) throws PhoneAlreadyExistException {
        if (Boolean.TRUE.equals(customerRepository.checkIfPhoneExists(phone))) {
            throw new PhoneAlreadyExistException(CUSTOMER_WITH_PHONE + phone + ALREADY_EXIST);
        }
    }
}
