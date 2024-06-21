package com.hms.service;

import com.hms.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto postCustomer(CustomerDto customer);
    List<CustomerDto> getCustomers();
    CustomerDto replaceCustomer(CustomerDto customer);
    CustomerDto modifyCustomer(CustomerDto customer);
    void deleteCustomer(Long customer_id);
    CustomerDto getCustomerById(Long customer_id);
}
