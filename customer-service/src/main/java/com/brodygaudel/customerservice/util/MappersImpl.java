package com.brodygaudel.customerservice.util;

import com.brodygaudel.customerservice.dto.CustomerDTO;
import com.brodygaudel.customerservice.entity.Customer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The {@code Mappers} class implements methods for mapping between {@link Customer} and {@link CustomerDTO} objects.
 * Implementing classes should provide concrete implementations for these methods to facilitate the conversion process.
 * <p>
 * The typical usage involves transforming a {@code Customer} instance to a {@code CustomerDTO} instance and vice versa.
 * </p>
 *
 * @implSpec
 * Implementing classes should adhere to the contract defined by these methods and ensure proper conversion between
 * {@code Customer} and {@code CustomerDTO} objects.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 *
 * @see Customer
 * @see CustomerDTO
 */
@Service
public class MappersImpl implements Mappers {
    /**
     * Converts a {@link Customer} object to its corresponding {@link CustomerDTO} representation.
     *
     * @param customer The {@code Customer} object to be converted.
     * @return The corresponding {@code CustomerDTO} representation of the input {@code customer}.
     */
    @Override
    public CustomerDTO fromCustomer(@NotNull Customer customer){
        return new CustomerDTO(
                customer.getId(), customer.getFirstname(), customer.getName(),
                customer.getPlaceOfBirth(), customer.getDateOfBirth(),
                customer.getNationality(), customer.getSex(), customer.getCin(),
                customer.getEmail(), customer.getPhone(), customer.getCreation(),
                customer.getLastUpdate()
        );
    }

    /**
     * Converts a {@link CustomerDTO} object to its corresponding {@link Customer} representation.
     *
     * @param customerDTO The {@code CustomerDTO} object to be converted.
     * @return The corresponding {@code Customer} representation of the input {@code customerDTO}.
     */
    @Override
    public Customer fromCustomerDTO(@NotNull CustomerDTO customerDTO) {
        return Customer.builder()
                .id(customerDTO.id()).firstname(customerDTO.firstname())
                .name(customerDTO.name()).placeOfBirth(customerDTO.placeOfBirth())
                .dateOfBirth(customerDTO.dateOfBirth()).nationality(customerDTO.nationality())
                .sex(customerDTO.sex()).cin(customerDTO.cin()).email(customerDTO.email())
                .phone(customerDTO.phone()).creation(customerDTO.creation())
                .lastUpdate(customerDTO.lastUpdate())
                .build();
    }


    /**
     * Converts a list of {@link Customer} objects to a list of {@link CustomerDTO} objects using the {@link Mappers#fromCustomer} method.
     *
     * @param customers The list of {@link Customer} objects to be converted.
     * @return A new {@link List} containing the converted {@link CustomerDTO} objects.
     * @throws NullPointerException if the input {@code customers} list is {@code null}.
     */
    @Override
    public List<CustomerDTO> fromListOfCustomers(@NotNull List<Customer> customers){
        return customers.stream().map(this::fromCustomer).toList();
    }

    public MappersImpl(){
        super();
    }
}
