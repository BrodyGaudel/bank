package com.mounanga.customerqueryservice.service;

import com.mounanga.customerqueryservice.entity.Customer;
import com.mounanga.customerqueryservice.event.CustomerCreatedEvent;
import com.mounanga.customerqueryservice.event.CustomerDeletedEvent;
import com.mounanga.customerqueryservice.event.CustomerRestoredEvent;
import com.mounanga.customerqueryservice.event.CustomerUpdatedEvent;
import com.mounanga.customerqueryservice.exception.ResourceNotFoundException;
import com.mounanga.customerqueryservice.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class CustomerEventHandlerService {

    private final CustomerRepository customerRepository;

    public CustomerEventHandlerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void handle(@NotNull CustomerCreatedEvent event) {
        Customer customer = new Customer();
        customer.setId(event.getId());
        customer.setEmail(event.getEmail());
        customer.setFirstname(event.getFirstname());
        customer.setLastname(event.getLastname());
        customer.setPlaceOfBirth(event.getPlaceOfBirth());
        customer.setNationality(event.getNationality());
        customer.setDateOfBirth(event.getDateOfBirth());
        customer.setAuditAuthor(event.getAuthor());
        customer.setAuditDateTime(event.getDateTime());
        customer.setGender(event.getGender());
        customer.setNip(event.getNip());
        customerRepository.save(customer);
    }

    public void handle(@NotNull CustomerUpdatedEvent event) {
        Customer customer = customerRepository.findById(event.getId()).orElse(null);
        if(customer == null) {
            log.error("Customer with id {} not found", event.getId());
            throw new ResourceNotFoundException("Customer with id " + event.getId() + " not found");
        }
        customer.setAuditDateTime(event.getDateTime());
        customer.setAuditAuthor(event.getAuthor());
        customer.setPlaceOfBirth(event.getPlaceOfBirth());
        customer.setNationality(event.getNationality());
        customer.setDateOfBirth(event.getDateOfBirth());
        customer.setEmail(event.getEmail());
        customer.setFirstname(event.getFirstname());
        customer.setLastname(event.getLastname());
        customer.setAuditAuthor(event.getAuthor());
        customer.setAuditDateTime(event.getDateTime());
        customer.setGender(event.getGender());
        customer.setNip(event.getNip());
        customerRepository.save(customer);
        log.info("Customer with id {} updated", event.getId());
    }

    public void handle(@NotNull CustomerDeletedEvent event) {
        Customer customer = customerRepository.findById(event.getId()).orElse(null);
        if (customer == null) {
            log.error("Customer with id {} not found.", event.getId());
            throw new ResourceNotFoundException("Customer with id " + event.getId() + " not found");
        }
        customer.setAuditDateTime(event.getDateTime());
        customer.setAuditAuthor(event.getAuthor());
        customer.setIsDeleted(Boolean.TRUE);
        customerRepository.save(customer);
        log.info("Customer with id {} deleted", event.getId());
    }

    public void handle(@NotNull CustomerRestoredEvent event) {
        Customer customer = customerRepository.findById(event.getId()).orElse(null);
        if (customer == null) {
            log.error("Customer with id {} not found .", event.getId());
            throw new ResourceNotFoundException("Customer with id " + event.getId() + " not found");
        }
        customer.setAuditDateTime(event.getDateTime());
        customer.setAuditAuthor(event.getAuthor());
        customer.setIsDeleted(Boolean.FALSE);
        customerRepository.save(customer);
        log.info("Customer with id {} deleted.", event.getId());
    }
}