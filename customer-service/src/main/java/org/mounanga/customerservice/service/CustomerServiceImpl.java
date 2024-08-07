package org.mounanga.customerservice.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mounanga.customerservice.dto.CustomerExistResponseDTO;
import org.mounanga.customerservice.dto.CustomerRequestDTO;
import org.mounanga.customerservice.dto.CustomerResponseDTO;
import org.mounanga.customerservice.entity.Customer;
import org.mounanga.customerservice.exception.CustomerNotFoundException;
import org.mounanga.customerservice.exception.FieldError;
import org.mounanga.customerservice.exception.FieldValidationException;
import org.mounanga.customerservice.repository.CustomerRepository;
import org.mounanga.customerservice.util.mapper.Mapper;
import org.mounanga.customerservice.web.restclient.AccountRestClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRestClient accountRestClient;

    public CustomerServiceImpl(CustomerRepository customerRepository, AccountRestClient accountRestClient) {
        this.customerRepository = customerRepository;
        this.accountRestClient = accountRestClient;
    }

    @Override
    public CustomerResponseDTO getCustomerById(String id) {
        log.info("In getCustomerById()");
        Customer customer = findCustomerById(id);
        log.info("Customer with id: '{}' found", customer);
        return Mapper.fromCustomer(customer);
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers(int page, int size) {
        log.info("In findAllCustomers()");
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.findAll(pageable);
        log.info("Found {} customer(s).", customers.getTotalElements());
        return Mapper.fromListOfCustomers(customers.getContent());
    }

    @Override
    public List<CustomerResponseDTO> searchCustomers(String keyword, int page, int size) {
        log.info("In searchCustomers()");
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.search("%" + keyword + "%", pageable);
        log.info("Found {} customer(s)", customers.getTotalElements());
        return Mapper.fromListOfCustomers(customers.getContent());
    }

    @Override
    public CustomerExistResponseDTO checkCustomerExist(String id) {
        log.info("In checkCustomerExist()");
        Customer customer = findCustomerById(id);
        log.info("Found customer with id : {}", customer.getId());
        return new CustomerExistResponseDTO(customer.getId(), customer.getEmail());
    }

    @Transactional
    @Override
    public CustomerResponseDTO createCustomer(@NotNull CustomerRequestDTO dto) {
        log.info("In createCustomer()");
        validationBeforeCreateCustomer(dto.getEmail(), dto.getCin());
        Customer customer = Mapper.fromCustomerRequestDTO(dto);
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer saved with id '{}' at '{}' by '{}'", savedCustomer.getId(), savedCustomer.getCreatedDate(), savedCustomer.getCreatedBy());
        return Mapper.fromCustomer(savedCustomer);
    }

    @Transactional
    @Override
    public CustomerResponseDTO updateCustomer(String id, @NotNull CustomerRequestDTO dto) {
        log.info("In updateCustomer()");
        Customer customer = findCustomerById(id);
        validationBeforeUpdateCustomer(customer.getCin(), customer.getEmail(), dto.getCin(), dto.getEmail());
        updateCustomerItems(customer, dto);
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer with id '{}' updated at '{}' by '{}'", updatedCustomer.getId(), updatedCustomer.getLastModifiedDate(), updatedCustomer.getLastModifiedBy());
        return Mapper.fromCustomer(customer);
    }

    @Transactional
    @Override
    public void deleteCustomerById(String id) {
        log.info("In deleteCustomerById()");
        Customer customer = findCustomerById(id);
        accountRestClient.deleteAccountByCustomerId(customer.getId());
        log.info("associated account deleted");
        customerRepository.deleteById(customer.getId());
        log.info("Customer with id: '{}' deleted", customer.getId());
    }

    private Customer findCustomerById(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%s' not found", id)));
    }

    private void validationBeforeCreateCustomer(String email, String cin) {
        Set<FieldError> errors = new HashSet<>();
        if(customerRepository.existsByEmail(email)) {
            errors.add(new FieldError("email", String.format("Customer with email '%s' already exists.", email)));
        }
        if(customerRepository.existsByCin(cin)) {
            errors.add(new FieldError("cin", String.format("Customer with CIN: '%s' already exists.", cin)));
        }
        if(!errors.isEmpty()) {
            throw new FieldValidationException("Validation error", errors);
        }
    }

    private void validationBeforeUpdateCustomer(@NotNull String oldCin, @NotNull String oldEmail, @NotNull String newCin, @NotNull String newEmail) {
        Set<FieldError> errors = new HashSet<>();

        if(!oldCin.equals(newCin) && customerRepository.existsByCin(newCin)) {
                errors.add(new FieldError("cin", String.format("Customer with CIN: '%s' already exists.", newCin)));
        }
        if(!oldEmail.equals(newEmail) && customerRepository.existsByEmail(newEmail)) {
            errors.add(new FieldError("email", String.format("Customer with email '%s' already exists.", newEmail)));
        }
        if(!errors.isEmpty()) {
            throw new FieldValidationException("Validation error", errors);
        }
    }

    private void updateCustomerItems(@NotNull Customer customer, @NotNull CustomerRequestDTO dto) {
        customer.setCin(dto.getCin());
        customer.setEmail(dto.getEmail());
        customer.setFirstname(dto.getFirstname());
        customer.setLastname(dto.getLastname());
        customer.setPlaceOfBirth(dto.getPlaceOfBirth());
        customer.setGender(dto.getGender());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setNationality(dto.getNationality());
    }
}
