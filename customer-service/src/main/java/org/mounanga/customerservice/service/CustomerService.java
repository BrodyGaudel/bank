package org.mounanga.customerservice.service;

import org.mounanga.customerservice.dto.CustomerExistResponse;
import org.mounanga.customerservice.dto.CustomerPageResponse;
import org.mounanga.customerservice.dto.CustomerRequest;
import org.mounanga.customerservice.dto.CustomerResponse;

public interface CustomerService {
    CustomerResponse getCustomerById(String id);
    CustomerResponse getCustomerByCin(String cin);
    CustomerPageResponse getAllCustomers(int page, int size);
    CustomerPageResponse searchCustomers(String keyword, int page, int size);
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse updateCustomer(String id, CustomerRequest request);
    void deleteCustomer(String id);
    CustomerExistResponse checkCustomerExist(String id);
}