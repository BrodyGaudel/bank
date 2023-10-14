package com.brodygaudel.customerservice.mappers;

import com.brodygaudel.customerservice.dtos.CustomerDTO;
import com.brodygaudel.customerservice.entities.Customer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class MappersImpl implements Mappers{
    /**
     * Converts a {@link Customer} object to its corresponding {@link CustomerDTO} representation.
     *
     * @param customer The {@code Customer} object to be converted.
     * @return The corresponding {@code CustomerDTO} representation of the input {@code customer}.
     */
    @Override
    public CustomerDTO fromCustomer(@NotNull Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstname(),
                customer.getName(),
                customer.getPlaceOfBirth(),
                customer.getDateOfBirth(),
                customer.getNationality(),
                customer.getSex(),
                customer.getCin(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreation(),
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
                .id(customerDTO.id())
                .firstname(customerDTO.firstname())
                .name(customerDTO.name())
                .placeOfBirth(customerDTO.placeOfBirth())
                .dateOfBirth(customerDTO.dateOfBirth())
                .nationality(customerDTO.nationality())
                .sex(customerDTO.sex())
                .cin(customerDTO.cin())
                .email(customerDTO.email())
                .phone(customerDTO.phone())
                .creation(customerDTO.creation())
                .lastUpdate(customerDTO.lastUpdate())
                .build();
    }
}
