package com.jed9h3.inventorymanagementsystem.service.imp;

import com.jed9h3.inventorymanagementsystem.dto.CustomerDto;
import com.jed9h3.inventorymanagementsystem.entity.Customer;
import com.jed9h3.inventorymanagementsystem.exception.BadRequestException;
import com.jed9h3.inventorymanagementsystem.exception.NoContentException;
import com.jed9h3.inventorymanagementsystem.exception.NotFoundException;
import com.jed9h3.inventorymanagementsystem.repository.CustomerRepository;
import com.jed9h3.inventorymanagementsystem.service.CustomerService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImp(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            throw new NoContentException("No customers registered yet");
        }
        return customers.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = convertToEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    @Override
    public void deleteAllCustomers() {
        if (customerRepository.count()==0){
            throw new NoContentException("No customers registered yet to delete");
        }else{
            customerRepository.deleteAll();
        }
    }

    @Override
    public CustomerDto getCustomerById(long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer with id "+id+" doesnt exist"));
        return convertToDto(customer);
    }

    @Override
    public Customer getRawCustomerById(long id) {
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer with id "+id+" doesnt exist"));
    }

    @Override
    public CustomerDto updateCustomerById(CustomerDto customerDto, long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer with id "+id+" doesnt exist"));
        String nm=customerDto.getCustomerName();
        BigDecimal blc=customerDto.getBalance();
        if (nm==null || nm.isEmpty() || blc==null){
            throw new BadRequestException("Request missing required attributes");
        }
        customer.setCustomerName(nm);
        customer.setBalance(blc);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    @Override
    public CustomerDto partiallyUpdateCustomerById(CustomerDto customerDto, long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer with id "+id+" doesnt exist"));
        String nm=customerDto.getCustomerName();
        BigDecimal blc=customerDto.getBalance();
        if (nm==null && blc==null){
            throw new NoContentException("No attributes to be updated");
        }
        if(nm!=null){
            customer.setCustomerName(customerDto.getCustomerName());
        }
        if(blc!=null){
            customer.setBalance(customerDto.getBalance());
        }
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    @Override
    public void deleteCustomerById(long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer with id "+id+" doesnt exist"));
        customerRepository.delete(customer);
    }
    @Override
    public CustomerDto convertToDto(Customer customer) {
        CustomerDto result = new CustomerDto();
        result.setCustomerId(customer.getCustomerId());
        result.setCustomerName(customer.getCustomerName());
        result.setBalance(customer.getBalance());
        return result;
    }
    @Override
    public Customer convertToEntity(CustomerDto customerDto) {
        Customer result = new Customer();
        result.setCustomerName(customerDto.getCustomerName());
        result.setBalance(customerDto.getBalance());
        return result;
    }
}
