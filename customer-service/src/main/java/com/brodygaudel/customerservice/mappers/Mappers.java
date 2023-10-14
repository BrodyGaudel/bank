package com.brodygaudel.customerservice.mappers;

import com.brodygaudel.customerservice.dtos.CustomerDTO;
import com.brodygaudel.customerservice.entities.Customer;

/**
 * The {@code Mappers} interface defines methods for mapping between {@link Customer} and {@link CustomerDTO} objects.
 * Implementing classes should provide concrete implementations for these methods to facilitate the conversion process.
 * <p>
 * The typical usage involves transforming a {@code Customer} instance to a {@code CustomerDTO} instance and vice versa.
 * </p>
 *
 * @implSpec
 * Implementing classes should adhere to the contract defined by these methods and ensure proper conversion between
 * {@code Customer} and {@code CustomerDTO} objects.
 *
 * @author Brody Gaudel
 *
 * @see Customer
 * @see CustomerDTO
 */
public interface Mappers {

    /**
     * Converts a {@link Customer} object to its corresponding {@link CustomerDTO} representation.
     *
     * @param customer The {@code Customer} object to be converted.
     * @return The corresponding {@code CustomerDTO} representation of the input {@code customer}.
     */
    CustomerDTO fromCustomer(Customer customer);

    /**
     * Converts a {@link CustomerDTO} object to its corresponding {@link Customer} representation.
     *
     * @param customerDTO The {@code CustomerDTO} object to be converted.
     * @return The corresponding {@code Customer} representation of the input {@code customerDTO}.
     */
    Customer fromCustomerDTO(CustomerDTO customerDTO);
}

