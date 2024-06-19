package com.hms.service;

import com.hms.dto.CustomerDto;

public interface CustomerService {
    CustomerDto postCustomer(CustomerDto customer);
    CustomerDto[] getCustomers(Integer pageNumber, Integer pageSize);
    CustomerDto replaceCustomer(CustomerDto customer);
    CustomerDto modifyCustomer(CustomerDto customer);
    void deleteCustomer(Long customer_id);
    CustomerDto getCustomerById(Long customer_id);
}
