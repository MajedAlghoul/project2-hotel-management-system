package com.hms.service.impl;

import com.hms.dto.CustomerDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Customer;
import com.hms.repository.CustomerRepository;
import com.hms.repository.CustomerRepository;
import com.hms.service.CustomerService;
import com.hms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDto postCustomer(CustomerDto customer) {
        if(customerRepository.existsById(customer.getCustomer_email()))
            throw new DuplicateResourceException("Customer", "customer_id", customer.getCustomer_email());
        return mapToDto(customerRepository.save(mapToEntity(customer)));
    }

    @Override
    public CustomerDto[] getCustomers(Integer pageNumber, Integer pageSize) {
        Object[] customersObjects = customerRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().toArray();
        CustomerDto[] customersDtos = new CustomerDto[customersObjects.length];
        for(int i = 0; i < customersObjects.length; i++)
            if (customersObjects[i] instanceof Customer)
                customersDtos[i] = mapToDto((Customer) customersObjects[i]);
        if(customersDtos.length == 0)
            throw new ResourceNotFoundException("Customer", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        return customersDtos;
    }

    @Override
    public CustomerDto replaceCustomer(CustomerDto customer) {
        if(!customerRepository.existsById(customer.getCustomer_email()))
            throw new ResourceNotFoundException("Customer", "customer_id", String.valueOf(customer.getCustomer_email()));
        return mapToDto(customerRepository.save(mapToEntity(customer)));
    }

    @Override
    public CustomerDto modifyCustomer(CustomerDto partialCustomer) {
        Customer originalCustomer = customerRepository.findById(partialCustomer.getCustomer_email()).orElseThrow(() -> new ResourceNotFoundException("Customer", "customer_id", String.valueOf(partialCustomer.getCustomer_email())));
        customerRepository.save(originalCustomer);
        return mapToDto(originalCustomer);
    }

    @Override
    public void deleteCustomer(String customer_id) {
        if(!customerRepository.existsById(customer_id))
            throw new ResourceNotFoundException("Customer", "customer_id", String.valueOf(customer_id));
        customerRepository.deleteById(customer_id);
    }

    @Override
    public CustomerDto getCustomerById(String customer_id) {
        return mapToDto(customerRepository.findById(customer_id).orElseThrow(() -> new ResourceNotFoundException("Customer", "customer_id", String.valueOf(customer_id))));
    }

    private CustomerDto mapToDto(Customer customer) {
        return CustomerDto.builder()
                .customer_email(customer.getCustomer_email())
                .name(customer.getName())
                .password_hash(customer.getPassword_hash())
                .build();
    }

    private Customer mapToEntity(CustomerDto customerDto) {
        return Customer.builder()
                .customer_email(customerDto.getCustomer_email())
                .name(customerDto.getName())
                .password_hash(customerDto.getPassword_hash())
                .build();
    }
}
