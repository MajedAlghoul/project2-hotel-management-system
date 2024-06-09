package com.jed9h3.inventorymanagementsystem.controller;

import com.jed9h3.inventorymanagementsystem.dto.CustomerDto;
import com.jed9h3.inventorymanagementsystem.exception.BadRequestException;
import com.jed9h3.inventorymanagementsystem.service.CustomerService;
//import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        if (customerDto.getCustomerId() != null) {
            throw new BadRequestException("Bad Request: ID provided");
        }
        return new ResponseEntity<>(customerService.createCustomer(customerDto), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllCustomers() {
        customerService.deleteAllCustomers();
        return new ResponseEntity<>("All customers has been deleted successfully.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(
            @PathVariable(name = "id") long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomerById(@Valid @RequestBody CustomerDto customerDto
                    , @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(customerService.updateCustomerById(customerDto, id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerDto> partiallyUpdateCustomerById(@Valid @RequestBody CustomerDto customerDto
            , @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(customerService.partiallyUpdateCustomerById(customerDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable(name = "id") long id) {
        customerService.deleteCustomerById(id);
        return new ResponseEntity<>("Customer number "+id+" has been Deleted successfully.", HttpStatus.OK);
    }
}
