package org.mounanga.customerservice.util;

import org.jetbrains.annotations.NotNull;
import org.mounanga.customerservice.dto.*;
import org.mounanga.customerservice.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public class Mappers {

    private Mappers(){
        super();
    }

    public static CustomerResponse fromCustomer(Customer customer) {
        if(customer == null) {
            return null;
        }
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstname(customer.getFirstname())
                .lastname(customer.getLastname())
                .dateOfBirth(customer.getDateOfBirth())
                .placeOfBirth(customer.getPlaceOfBirth())
                .nationality(customer.getNationality())
                .sex(customer.getSex())
                .cin(customer.getCin())
                .creator(customer.getCreator())
                .lastModifier(customer.getLastModifier())
                .createdDate(customer.getCreatedDate())
                .lastModifiedDate(customer.getLastModifiedDate())
                .email(customer.getEmail())
                .build();
    }

    public static List<CustomerResponse> fromCustomers(List<Customer> customers) {
        if(customers == null || customers.isEmpty()) {
           return List.of();
        }
        return customers.stream().map(Mappers::fromCustomer).toList();
    }

    public static CustomerPageResponse fromPageOfCustomers(Page<Customer> customerPage, int page) {
        if(customerPage == null || customerPage.isEmpty()) {
            return null;
        }
        return new CustomerPageResponse(
                getInfoFromPagination(customerPage, page),
                getStatusFromPagination(customerPage),
                fromCustomers(customerPage.getContent())
        );
    }

    public static Customer fromCustomerRequest(CustomerRequest request) {
        if(request == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setFirstname(request.getFirstname());
        customer.setLastname(request.getLastname());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setPlaceOfBirth(request.getPlaceOfBirth());
        customer.setNationality(request.getNationality());
        customer.setSex(request.getSex());
        customer.setCin(request.getCin());
        customer.setEmail(request.getEmail());
        return customer;
    }

    private static PaginationInfo getInfoFromPagination(@NotNull Page<Customer> customerPage, int page){
        return PaginationInfo.builder()
                .page(page)
                .totalPages(customerPage.getTotalPages())
                .number(customerPage.getNumber())
                .numberOfElements(customerPage.getNumberOfElements())
                .totalElements(customerPage.getTotalElements())
                .size(customerPage.getSize())
                .build();
    }

    private static PageStatus getStatusFromPagination(@NotNull Page<Customer> customerPage){
        return PageStatus.builder()
                .hasContent(customerPage.hasContent())
                .hasNext(customerPage.hasNext())
                .hasPrevious(customerPage.hasPrevious())
                .isFirst(customerPage.isFirst())
                .isLast(customerPage.isLast())
                .build();
    }
}
