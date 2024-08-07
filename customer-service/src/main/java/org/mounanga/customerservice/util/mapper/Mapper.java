package org.mounanga.customerservice.util.mapper;

import org.jetbrains.annotations.NotNull;
import org.mounanga.customerservice.dto.CustomerRequestDTO;
import org.mounanga.customerservice.dto.CustomerResponseDTO;
import org.mounanga.customerservice.entity.Customer;

import java.util.List;

public class Mapper {

    private Mapper(){
        super();
    }

    public static @NotNull Customer fromCustomerRequestDTO(final @NotNull CustomerRequestDTO dto){
        final Customer customer = new Customer();
        customer.setFirstname(dto.getFirstname());
        customer.setLastname(dto.getLastname());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setPlaceOfBirth(dto.getPlaceOfBirth());
        customer.setGender(dto.getGender());
        customer.setNationality(dto.getNationality());
        customer.setCin(dto.getCin());
        customer.setEmail(dto.getEmail());
        return customer;
    }

    public static @NotNull CustomerResponseDTO fromCustomer(final @NotNull Customer customer){
        final CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setFirstname(customer.getFirstname());
        dto.setLastname(customer.getLastname());
        dto.setPlaceOfBirth(customer.getPlaceOfBirth());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setGender(customer.getGender());
        dto.setNationality(customer.getNationality());
        dto.setCin(customer.getCin());
        dto.setEmail(customer.getEmail());
        dto.setCreatedDate(customer.getCreatedDate());
        dto.setCreatedBy(customer.getCreatedBy());
        dto.setLastModifiedDate(customer.getLastModifiedDate());
        dto.setLastModifiedBy(customer.getLastModifiedBy());
        return dto;
    }

    public static List<CustomerResponseDTO> fromListOfCustomers(final @NotNull List<Customer> customers){
        return customers.stream().map(Mapper::fromCustomer).toList();
    }
}
