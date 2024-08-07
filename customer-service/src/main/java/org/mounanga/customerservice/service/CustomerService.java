package org.mounanga.customerservice.service;

import org.mounanga.customerservice.dto.CustomerExistResponseDTO;
import org.mounanga.customerservice.dto.CustomerRequestDTO;
import org.mounanga.customerservice.dto.CustomerResponseDTO;

import java.util.List;

public interface CustomerService {
    CustomerResponseDTO getCustomerById(String id);
    List<CustomerResponseDTO> getAllCustomers(int page, int size);
    List<CustomerResponseDTO> searchCustomers(String keyword, int page, int size);
    CustomerExistResponseDTO checkCustomerExist(String id);
    CustomerResponseDTO createCustomer(CustomerRequestDTO dto);
    CustomerResponseDTO updateCustomer(String id, CustomerRequestDTO dto);
    void deleteCustomerById(String id);
}
