package com.mounanga.customerqueryservice.service;

import com.mounanga.customerqueryservice.dto.CustomerResponseDTO;
import com.mounanga.customerqueryservice.entity.Customer;
import com.mounanga.customerqueryservice.query.GetCustomerByEmailQuery;
import com.mounanga.customerqueryservice.query.GetCustomerByIdQuery;
import com.mounanga.customerqueryservice.query.GetCustomerByNipQuery;
import com.mounanga.customerqueryservice.query.GetCustomersQuery;
import com.mounanga.customerqueryservice.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerQueryHandlerService {

    private final CustomerRepository repository;

    public CustomerQueryHandlerService(CustomerRepository repository) {
        this.repository = repository;
    }

    @QueryHandler
    public CustomerResponseDTO handle(@NotNull GetCustomerByIdQuery query) {
        Customer customer = repository.findById(query.getId()).orElse(null);
        if(customer == null) {
            log.warn("Customer with id {} not found", query.getId());
            return null;
        }
        log.debug("Customer with id {} found", query.getId());
        return new CustomerResponseDTO(customer);
    }

    @QueryHandler
    public CustomerResponseDTO handle(@NotNull GetCustomerByNipQuery query) {
        Customer customer = repository.findByNip(query.getNip()).orElse(null);
        if(customer == null) {
            log.warn("Customer with NIP {} not found", query.getNip());
            return null;
        }
        log.debug("Customer with NIP {} found", query.getNip());
        return new CustomerResponseDTO(customer);
    }

    @QueryHandler
    public CustomerResponseDTO handle(@NotNull GetCustomerByEmailQuery query) {
        Customer customer = repository.findByEmail(query.getEmail()).orElse(null);
        if(customer == null) {
            log.warn("Customer with EMAIL {} not found", query.getEmail());
            return null;
        }
        log.debug("Customer with EMAIL {} found", query.getEmail());
        return new CustomerResponseDTO(customer);
    }

    @QueryHandler
    public List<CustomerResponseDTO> handle(@NotNull GetCustomersQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize());
        Page<@NotNull Customer> customers = repository.findByKeyword(
                "%"+query.getKeyword()+"%", pageable
        );
        return customers.getContent()
                .stream()
                .map(CustomerResponseDTO::new)
                .toList();

    }
}
