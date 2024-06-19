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
@RequestMapping("/api/customer")
@RestController
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(produces = "application/json")
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

    @PutMapping(produces = "application/json")
    public ResponseEntity<CustomerDto> putCustomer(@Validated @RequestBody CustomerDto customer) {
        return ResponseEntity.ok().body(customerService.replaceCustomer(customer));
    }

    @PatchMapping(produces = "application/json")
    public ResponseEntity<CustomerDto> patchCustomer(@Validated @RequestBody CustomerDto customer) {
        return ResponseEntity.ok().body(customerService.modifyCustomer(customer));
    }

    @DeleteMapping(value = "/{customer_id}", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> deleteCustomer(@Validated @PathVariable Long customer_id) {
        customerService.deleteCustomer(customer_id);
        return ResponseEntity.ok().body(new StandardMessageBody("Deleted successfully.").getContent());
    }

    @GetMapping(value = "/{customer_id}", produces = "application/json")
    public ResponseEntity<CustomerDto> getCustomer(@Validated @PathVariable Long customer_id) {
        return ResponseEntity.ok().body(customerService.getCustomerById(customer_id));
    }
}
