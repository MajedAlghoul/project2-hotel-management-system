package com.jed9h3.inventorymanagementsystem.service;

import com.jed9h3.inventorymanagementsystem.dto.CustomerDto;
import com.jed9h3.inventorymanagementsystem.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();
    CustomerDto createCustomer(CustomerDto customerDto);
    void deleteAllCustomers();
    CustomerDto getCustomerById(long id);
    Customer getRawCustomerById(long id);
    CustomerDto updateCustomerById(CustomerDto customerDto, long id);
    CustomerDto partiallyUpdateCustomerById(CustomerDto customerDto, long id);
    void deleteCustomerById(long id);
    CustomerDto convertToDto(Customer customer);
    Customer convertToEntity(CustomerDto customerDto);
}
