package com.hms.controller;

import com.hms.dto.CustomerDto;
import com.hms.exception.IllegalInputException;
import com.hms.responsebody.StandardMessageBody;
import com.hms.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Validated
@Tag(name = "Customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            description = "Endpoint for fetching a list of Customers",
            summary = "Fetch Customers",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
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

    @Operation(
            description = "Endpoint for replacing an Customer",
            summary = "Replace Customer",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @PutMapping(produces = "application/json")
    public ResponseEntity<CustomerDto> putCustomer(@Validated @RequestBody CustomerDto customer) {
        return ResponseEntity.ok().body(customerService.replaceCustomer(customer));
    }

    @Operation(
            description = "Endpoint for partially updating an Customer",
            summary = "Partially update Customer",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @PatchMapping(produces = "application/json")
    public ResponseEntity<CustomerDto> patchCustomer(@Validated @RequestBody CustomerDto customer) {
        return ResponseEntity.ok().body(customerService.modifyCustomer(customer));
    }

    @Operation(
            description = "Endpoint for deleting an Customer",
            summary = "Delete Customer",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @DeleteMapping(value = "/{customer_id}", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> deleteCustomer(@Validated @PathVariable String customer_id) {
        long id;
        try{
            id = Integer.parseInt(customer_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Customer", "customer_id", customer_id);
        }
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().body(new StandardMessageBody("Deleted successfully.").getContent());
    }

    @Operation(
            description = "Endpoint for getting an Customer",
            summary = "Get Customer",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping(value = "/{customer_id}", produces = "application/json")
    public ResponseEntity<CustomerDto> getCustomer(@Validated @PathVariable String customer_id) {
        long id;
        try{
            id = Integer.parseInt(customer_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Customer", "customer_id", customer_id);
        }
        return ResponseEntity.ok().body(customerService.getCustomerById(id));
    }
}
