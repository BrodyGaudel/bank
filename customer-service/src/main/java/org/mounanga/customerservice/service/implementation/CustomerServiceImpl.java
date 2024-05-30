package org.mounanga.customerservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mounanga.customerservice.dto.CustomerExistResponse;
import org.mounanga.customerservice.service.CustomerService;
import org.mounanga.customerservice.dto.CustomerPageResponse;
import org.mounanga.customerservice.dto.CustomerRequest;
import org.mounanga.customerservice.dto.CustomerResponse;
import org.mounanga.customerservice.entity.Customer;
import org.mounanga.customerservice.exception.CinAlreadyExistException;
import org.mounanga.customerservice.exception.CustomerNotFoundException;
import org.mounanga.customerservice.repository.CustomerRepository;
import org.mounanga.customerservice.util.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
        log.info("In getCustomerById");
        Customer customer = findCustomerById(id);
        log.info("customer with id '{}' found", customer.getId());
        return Mappers.fromCustomer(customer);
    }

    @Override
    public CustomerResponse getCustomerByCin(String cin) {
        log.info("In getCustomerByCin");
        Customer customer = customerRepository.findByCin(cin)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with CIN '" + cin + "' not found"));
        log.info("customer with CIN '{}' found", customer.getCin());
        return Mappers.fromCustomer(customer);
    }

    @Override
    public CustomerPageResponse getAllCustomers(int page, int size) {
        log.info("In getAllCustomers");
        Page<Customer> customers = customerRepository.findAll(PageRequest.of(page, size));
        log.info("{} customers found in page: {}", customers.getNumberOfElements(), page);
        return Mappers.fromPageOfCustomers(customers, page);
    }

    @Override
    public CustomerPageResponse searchCustomers(String keyword, int page, int size) {
        log.info("In searchCustomers");
        Page<Customer> customers = customerRepository.search("%" + keyword + "%", PageRequest.of(page, size));
        log.info("{} customers found by keyword: {}", customers.getNumberOfElements(), keyword);
        return Mappers.fromPageOfCustomers(customers, page);
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        log.info("In createCustomer");
        validateBeforeCreate(request);
        Customer customer = Mappers.fromCustomerRequest(request);
        Customer savedCustomer = customerRepository.save(customer);
        log.info("customer with id '{}' created at '{}' , by '{}'.", savedCustomer.getId(), savedCustomer.getCreatedDate(), savedCustomer.getCreator());
        return Mappers.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerResponse updateCustomer(String id, CustomerRequest request) {
        log.info("In updateCustomer");
        Customer customer = findCustomerById(id);
        validateBeforeUpdate(request, customer);
        customer.setFirstname(request.getFirstname());
        customer.setLastname(request.getLastname());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setCin(request.getCin());
        customer.setPlaceOfBirth(request.getPlaceOfBirth());
        customer.setSex(request.getSex());
        customer.setNationality(request.getNationality());
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("customer with id '{}' updated at '{}' , by '{}'.", updatedCustomer.getId(), updatedCustomer.getLastModifiedDate(), updatedCustomer.getLastModifier());
        return Mappers.fromCustomer(customer);
    }

    @Override
    public void deleteCustomer(String id) {
        log.info("In deleteCustomer");
        try{
            customerRepository.deleteById(id);
            log.info("customer with id '{}' deleted.", id);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public CustomerExistResponse checkCustomerExist(String id) {
        log.info("In checkCustomerExist");
        boolean exists = customerRepository.existsById(id);
        log.info("customer with id '{}' exists : {}.", id, exists);
        return new CustomerExistResponse(exists);
    }

    private Customer findCustomerById(String id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new CustomerNotFoundException("customer with id '" + id + "' not found.")
        );
    }

    private void validateBeforeCreate(@NotNull CustomerRequest customer) {
        if(customerRepository.existsByCin(customer.getCin())) {
            throw new CinAlreadyExistException("cin already exists");
        }
    }

    private void validateBeforeUpdate(@NotNull CustomerRequest request, @NotNull Customer existingCustomer) {
        if(!existingCustomer.getCin().equals(request.getCin()) && customerRepository.existsByCin(request.getCin())) {
                throw new CinAlreadyExistException("cin already exists");
        }
    }
}
