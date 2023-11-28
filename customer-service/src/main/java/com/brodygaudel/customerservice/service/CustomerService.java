package com.brodygaudel.customerservice.service;

import com.brodygaudel.customerservice.dto.CustomerDTO;
import com.brodygaudel.customerservice.dto.CustomerPageDTO;
import com.brodygaudel.customerservice.exception.CinAlreadyExistException;
import com.brodygaudel.customerservice.exception.CustomerNotFoundException;
import com.brodygaudel.customerservice.exception.EmailAlreadyExistException;
import com.brodygaudel.customerservice.exception.PhoneAlreadyExistException;


/**
 * The {@code CustomerService} interface declares methods for managing customer-related operations.
 * Implementing classes should provide concrete implementations for these methods to interact
 * with customer data and perform necessary business logic.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * try {
 *     // Code that calls methods from CustomerService
 * } catch (CustomerNotFoundException | CinAlreadyExistException | EmailAlreadyExistException | PhoneAlreadyExistException e) {
 *     // Handle exceptions thrown by the service methods, possibly providing user-friendly error messages
 *     // or taking appropriate actions based on the specific exception type.
 * }
 * }
 * </pre>
 * </p>
 *
 * @author Brody Gaudel
 *
 * @see CustomerDTO
 * @see CinAlreadyExistException
 * @see EmailAlreadyExistException
 * @see PhoneAlreadyExistException
 * @see CustomerNotFoundException
 * @see CustomerPageDTO
 */
public interface CustomerService {

    /**
     * Saves a customer using the provided {@code CustomerDTO}.
     *
     * @param customerDTO The {@code CustomerDTO} containing customer information to be saved.
     * @return The saved {@code CustomerDTO}.
     * @throws CinAlreadyExistException   If the customer's CIN already exists in the system.
     * @throws EmailAlreadyExistException If the customer's email already exists in the system.
     * @throws PhoneAlreadyExistException If the customer's phone number already exists in the system.
     */
    CustomerDTO save(CustomerDTO customerDTO) throws CinAlreadyExistException, EmailAlreadyExistException, PhoneAlreadyExistException;

    /**
     * Updates a customer identified by the given ID with the provided {@code CustomerDTO}.
     *
     * @param id          The ID of the customer to be updated.
     * @param customerDTO The {@code CustomerDTO} containing updated customer information.
     * @return The updated {@code CustomerDTO}.
     * @throws CustomerNotFoundException If the customer with the specified ID is not found.
     * @throws CinAlreadyExistException   If the updated CIN already exists in the system.
     * @throws EmailAlreadyExistException If the updated email already exists in the system.
     * @throws PhoneAlreadyExistException If the updated phone number already exists in the system.
     */
    CustomerDTO update(String id, CustomerDTO customerDTO) throws CustomerNotFoundException, CinAlreadyExistException, EmailAlreadyExistException, PhoneAlreadyExistException;

    /**
     * Retrieves a customer by the given ID.
     *
     * @param id The ID of the customer to be retrieved.
     * @return The {@code CustomerDTO} representing the retrieved customer.
     * @throws CustomerNotFoundException If the customer with the specified ID is not found.
     */
    CustomerDTO getById(String id) throws CustomerNotFoundException;

    /**
     * Retrieves a list of customer pages.
     *
     * @return A list of {@code CustomerPageDTO} representing customer pages.
     */
    CustomerPageDTO getAll(int size, int page);

    /**
     * Searches for customer pages based on the provided keyword.
     *
     * @param keyword The keyword to be used for searching.
     * @return A list of {@code CustomerPageDTO} representing the search results.
     */
    CustomerPageDTO search(String keyword, int page, int size);

    /**
     * Deletes a customer by the given ID.
     *
     * @param id The ID of the customer to be deleted.
     */
    void deleteById(String id);
}
