package org.mounanga.customerservice.service;

import org.mounanga.customerservice.dto.CustomerPageResponseDTO;
import org.mounanga.customerservice.dto.CustomerRequestDTO;
import org.mounanga.customerservice.dto.CustomerResponseDTO;

public interface CustomerService {

    CustomerResponseDTO getCustomerById(String id);
    CustomerResponseDTO getCustomerByCin(String cin);
    CustomerPageResponseDTO getAllCustomers(int page, int size);
    CustomerPageResponseDTO searchCustomers(String keyword, int page, int size);
    CustomerResponseDTO createCustomer(CustomerRequestDTO dto);
    CustomerResponseDTO updateCustomer(String id, CustomerRequestDTO dto);
    void deleteCustomerById(String id);
}
