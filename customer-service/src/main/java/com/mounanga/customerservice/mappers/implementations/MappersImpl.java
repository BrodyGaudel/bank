package com.mounanga.customerservice.mappers.implementations;

import com.mounanga.customerservice.dtos.CustomerDTO;
import com.mounanga.customerservice.entities.Customer;
import com.mounanga.customerservice.entities.buiders.CustomerBuilder;
import com.mounanga.customerservice.mappers.Mappers;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MappersImpl implements Mappers {
    /**
     * Converts a {@link CustomerDTO} object to a corresponding {@link Customer} object.
     *
     * @param customerDTO The {@link CustomerDTO} object to convert.
     * @return The resulting {@link Customer} object.
     */
    @Override
    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        if(customerDTO == null){
            return null;
        }
        return new CustomerBuilder()
                .id(customerDTO.id())
                .firstname(customerDTO.firstname())
                .name(customerDTO.name())
                .dateOfBirth(customerDTO.dateOfBirth())
                .placeOfBirth(customerDTO.placeOfBirth())
                .nationality(customerDTO.nationality())
                .sex(customerDTO.sex())
                .cin(customerDTO.cin())
                .email(customerDTO.email())
                .phone(customerDTO.phone())
                .lastUpdate(customerDTO.lastUpdate())
                .build();
    }

    /**
     * Converts a {@link Customer} object to a corresponding {@link CustomerDTO} object.
     *
     * @param customer The {@link Customer} object to convert.
     * @return The resulting {@link CustomerDTO} object.
     */
    @Override
    public CustomerDTO fromCustomer(Customer customer) {
        if(customer == null){
            return null;
        }
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
     * Converts a list of {@link Customer} objects to a list of corresponding {@link CustomerDTO} objects.
     *
     * @param customers The list of {@link Customer} objects to convert.
     * @return The resulting list of {@link CustomerDTO} objects.
     */
    @Override
    public List<CustomerDTO> fromListOfCustomers(List<Customer> customers) {
        if(customers == null){
            return Collections.emptyList();
        }
        return customers.stream().map(this::fromCustomer).toList();
    }
}
