package com.hms.controller;

import com.hms.dto.CustomerDto;
import com.hms.exception.IllegalInputException;
import com.hms.responsebody.StandardMessageBody;
import com.hms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Contains endpoints related to the Customer resource.
 */

@RestController
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(value = "/customer", produces = "application/json")
    public ResponseEntity<CustomerDto> postCustomer(@Validated @RequestBody CustomerDto customer) {
        return ResponseEntity.ok().body(customerService.postCustomer(customer));
    }

    @GetMapping(value = "/customer", produces = "application/json")
    public ResponseEntity<CustomerDto[]> getCustomers(@Validated @RequestParam String pageNumber, @RequestParam String pageSize) {
        int pageNumberInt, pageSizeInt;
        try{
            pageNumberInt = Integer.parseInt(pageNumber);
            pageSizeInt = Integer.parseInt(pageSize);
            if(pageNumberInt < 0 || pageNumberInt > pageSizeInt || pageSizeInt < 1)
                throw new IllegalInputException("Customer", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Customer", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        }
        return ResponseEntity.ok().body(customerService.getCustomers(pageNumberInt, pageSizeInt));
    }

    @PutMapping(value = "/customer", produces = "application/json")
    public ResponseEntity<CustomerDto> putCustomer(@Validated @RequestBody CustomerDto customer) {
        return ResponseEntity.ok().body(customerService.replaceCustomer(customer));
    }

    @PatchMapping(value = "/customer", produces = "application/json")
    public ResponseEntity<CustomerDto> patchCustomer(@Validated @RequestBody CustomerDto customer) {
        return ResponseEntity.ok().body(customerService.modifyCustomer(customer));
    }

    @DeleteMapping(value = "/customer/{customer_id}", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> deleteCustomer(@Validated @PathVariable String customer_id) {
        int id;
        try{
            id = Integer.parseInt(customer_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Customer", "customer_id", customer_id);
        }
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().body(new StandardMessageBody("Deleted successfully.").getContent());
    }

    @GetMapping(value = "/customer/{customer_id}", produces = "application/json")
    public ResponseEntity<CustomerDto> getCustomer(@Validated @PathVariable String customer_id) {
        int id;
        try{
            id = Integer.parseInt(customer_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Customer", "customer_id", customer_id);
        }
        return ResponseEntity.ok().body(customerService.getCustomerById(id));
    }
}
