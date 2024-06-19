package com.hms.service.impl;

import com.hms.dto.CustomerDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Customer;
import com.hms.model.Role;
import com.hms.model.User;
import com.hms.repository.CustomerRepository;
import com.hms.repository.CustomerRepository;
import com.hms.repository.RoleRepository;
import com.hms.service.CustomerService;
import com.hms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerDto postCustomer(CustomerDto customer) {
        if(customerRepository.existsByEmail(customer.getCustomerEmail()))
            throw new DuplicateResourceException("Customer", "customer_id", customer.getCustomerEmail());
        Customer newCustomer = mapToEntity(customer);
        Role role = roleRepository.findByName("ROLE_CUSTOMER").get();
        newCustomer.setRole(Collections.singleton(role));
        //customerRepository.save(newCustomer);
        return mapToDto(customerRepository.save(newCustomer));
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
        if(!customerRepository.existsByEmail(customer.getCustomerEmail()))
            throw new ResourceNotFoundException("Customer", "customer_id", String.valueOf(customer.getCustomerEmail()));
        return mapToDto(customerRepository.save(mapToEntity(customer)));
    }

    @Override
    public CustomerDto modifyCustomer(CustomerDto partialCustomer) {
        Customer originalCustomer = customerRepository.findByEmail(partialCustomer.getCustomerEmail()).orElseThrow(() -> new ResourceNotFoundException("Customer", "customer_id", String.valueOf(partialCustomer.getCustomerEmail())));
        customerRepository.save(originalCustomer);
        return mapToDto(originalCustomer);
    }

    @Override
    public void deleteCustomer(Long customer_id) {
        if(!customerRepository.existsById(customer_id))
            throw new ResourceNotFoundException("Customer", "customer_id", String.valueOf(customer_id));
        customerRepository.deleteById(customer_id);
    }

    @Override
    public CustomerDto getCustomerById(Long customer_id) {
        return mapToDto(customerRepository.findById(customer_id).orElseThrow(() -> new ResourceNotFoundException("Customer", "customer_id", String.valueOf(customer_id))));
    }

    private CustomerDto mapToDto(Customer customer) {
        return CustomerDto.builder()
                .customerEmail(customer.getEmail())
                .name(customer.getName())
                .passwordHash(customer.getPasswordHash())
                .build();
    }

    private Customer mapToEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setEmail(customerDto.getCustomerEmail());
        customer.setName(customerDto.getName());
        customer.setPasswordHash(passwordEncoder.encode(customerDto.getPasswordHash()));
        return customer;
    }
}
