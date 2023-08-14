package com.mounanga.customerservice.mappers;

import com.mounanga.customerservice.dtos.CustomerDTO;
import com.mounanga.customerservice.entities.Customer;

import java.util.List;

/**
 * This interface defines methods for mapping between {@link Customer} and {@link CustomerDTO} objects.
 */
public interface Mappers {

    /**
     * Converts a {@link CustomerDTO} object to a corresponding {@link Customer} object.
     *
     * @param customerDTO The {@link CustomerDTO} object to convert.
     * @return The resulting {@link Customer} object.
     */
    Customer fromCustomerDTO(CustomerDTO customerDTO);

    /**
     * Converts a {@link Customer} object to a corresponding {@link CustomerDTO} object.
     *
     * @param customer The {@link Customer} object to convert.
     * @return The resulting {@link CustomerDTO} object.
     */
    CustomerDTO fromCustomer(Customer customer);

    /**
     * Converts a list of {@link Customer} objects to a list of corresponding {@link CustomerDTO} objects.
     *
     * @param customers The list of {@link Customer} objects to convert.
     * @return The resulting list of {@link CustomerDTO} objects.
     */
    List<CustomerDTO> fromListOfCustomers(List<Customer> customers);
}

