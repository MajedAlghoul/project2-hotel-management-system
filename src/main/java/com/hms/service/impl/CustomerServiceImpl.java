package com.hms.service.impl;

import com.hms.dto.CustomerDto;
import com.hms.dto.CustomerDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.NoContentException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Customer;
import com.hms.model.Role;
import com.hms.model.Customer;
import com.hms.repository.CustomerRepository;
import com.hms.repository.RoleRepository;
import com.hms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerDto postCustomer(CustomerDto customer) {
        if(customerRepository.existsByEmail(customer.getEmail()))
            throw new DuplicateResourceException("Customer", "customer_id", customer.getEmail());
        Customer newCustomer = mapToEntity(customer);
        Role role = roleRepository.findByName("ROLE_CUSTOMER").get();
        newCustomer.setRole(Collections.singleton(role));
        return mapToDto(customerRepository.save(newCustomer));
    }

    @Override
    public List<CustomerDto> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            throw new NoContentException("No customers registered yet");
        }
        return customers.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CustomerDto replaceCustomer(CustomerDto customer) {
        if(!customerRepository.existsById(customer.getId()))
            throw new ResourceNotFoundException("Customer", "customer_id", String.valueOf(customer.getEmail()));
        return mapToDto(customerRepository.save(mapToEntity(customer)));
    }

    @Override
    public CustomerDto modifyCustomer(CustomerDto partialCustomer) {
        Customer originalCustomer = customerRepository.findById(partialCustomer.getId()).orElseThrow(() -> new ResourceNotFoundException("Customer", "email", String.valueOf(partialCustomer.getId())));
        if(!partialCustomer.getEmail().isEmpty())
            originalCustomer.setEmail(partialCustomer.getEmail());
        if(!partialCustomer.getName().isEmpty())
            originalCustomer.setName(partialCustomer.getName());
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
                .email(customer.getEmail())
                .name(customer.getName())
                .passwordHash(customer.getPasswordHash())
                .id(customer.getId())
                .build();
    }

    private Customer mapToEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setEmail(customerDto.getEmail());
        customer.setName(customerDto.getName());
        customer.setPasswordHash(passwordEncoder.encode(customerDto.getPasswordHash()));
        return customer;
    }
}
