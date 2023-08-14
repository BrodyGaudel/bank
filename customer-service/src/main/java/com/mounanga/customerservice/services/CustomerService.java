package com.mounanga.customerservice.services;

import com.mounanga.customerservice.dtos.CustomerDTO;
import com.mounanga.customerservice.exceptions.AlreadyExistException;
import com.mounanga.customerservice.exceptions.CustomerNotFoundException;

import java.util.List;

/**
 * Service interface for managing customer data.
 */
public interface CustomerService {

    /**
     * Retrieves a customer by their unique identifier.
     *
     * @param id The unique identifier of the customer.
     * @return The {@link CustomerDTO} representing the retrieved customer.
     * @throws CustomerNotFoundException If the customer with the given ID does not exist.
     */
    CustomerDTO getCustomerById(String id) throws CustomerNotFoundException;

    /**
     * Retrieves a customer by their CIN number.
     *
     * @param cin The CIN number of the customer.
     * @return The {@link CustomerDTO} representing the retrieved customer.
     * @throws CustomerNotFoundException If a customer with the given CIN number does not exist.
     */
    CustomerDTO getCustomerByCin(String cin) throws CustomerNotFoundException;

    /**
     * Retrieves a list of customers with pagination.
     *
     * @param size The number of customers per page.
     * @param page The page number.
     * @return A list of {@link CustomerDTO} representing the retrieved customers.
     */
    List<CustomerDTO> getAllCustomers(int size, int page);

    /**
     * Searches for customers based on a keyword with pagination.
     *
     * @param keyword The search keyword.
     * @param size The number of customers per page.
     * @param page The page number.
     * @return A list of {@link CustomerDTO} representing the searched customers.
     */
    List<CustomerDTO> searchCustomers(String keyword, int size, int page);

    /**
     * Saves a new customer.
     *
     * @param customerDTO The {@link CustomerDTO} containing the customer information to be saved.
     * @return The {@link CustomerDTO} representing the saved customer.
     * @throws AlreadyExistException If the CIN, email, or phone number of the customer already exists.
     */
    CustomerDTO saveCustomer(CustomerDTO customerDTO) throws AlreadyExistException;

    /**
     * Updates an existing customer's information.
     *
     * @param id The unique identifier of the customer to be updated.
     * @param customerDTO The {@link CustomerDTO} containing the updated customer information.
     * @return The {@link CustomerDTO} representing the updated customer.
     * @throws CustomerNotFoundException If the customer with the given ID does not exist.
     * @throws AlreadyExistException If the updated CIN, email, or phone number already exists for another customer.
     */
    CustomerDTO updateCustomer(String id, CustomerDTO customerDTO) throws CustomerNotFoundException, AlreadyExistException;

    /**
     * Deletes a customer by their unique identifier.
     *
     * @param id The unique identifier of the customer to be deleted.
     */
    void deleteCustomerById(String id);
}

