package com.hms.controller;

import com.hms.dto.BillDto;
import com.hms.exception.IllegalInputException;
import com.hms.responsebody.StandardMessageBody;
import com.hms.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Contains endpoints related to the Bill resource.
 */
@RequestMapping("/api/v2/bill")
@RestController
@Validated
@Tag(name = "Bill")
public class BillController {
    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @Operation(
            description = "Endpoint for creating a Bill",
            summary = "Create Bill",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"

                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    ),
            }
    )
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> registerBill (@Validated @RequestBody BillDto billDto){

        return ResponseEntity.ok().body(billService.postBill(billDto));
    }

    @Operation(
            description = "Endpoint for fetching a list of Bills",
            summary = "Fetch Bills",
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
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<BillDto>> getBills() {
        return ResponseEntity.ok().body(billService.getBills());
    }

    @Operation(
            description = "Endpoint for replacing an Bill",
            summary = "Replace Bill",
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
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PutMapping(produces = "application/json")
    public ResponseEntity<BillDto> putBill(@Validated @RequestBody BillDto bill) {
        return ResponseEntity.ok().body(billService.replaceBill(bill));
    }

    @Operation(
            description = "Endpoint for partially updating an Bill",
            summary = "Partially update Bill",
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
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PatchMapping(produces = "application/json")
    public ResponseEntity<BillDto> patchBill(@Validated @RequestBody BillDto bill) {
        return ResponseEntity.ok().body(billService.modifyBill(bill));
    }

    @Operation(
            description = "Endpoint for deleting an Bill",
            summary = "Delete Bill",
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
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping(value = "/{bill_id}", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> deleteBill(@Validated @PathVariable String bill_id) {
        long id;
        try{
            id = Integer.parseInt(bill_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Bill", "bill_id", bill_id);
        }
        billService.deleteBill(id);
        return ResponseEntity.ok().body(new StandardMessageBody("Deleted successfully.").getContent());
    }

    @Operation(
            description = "Endpoint for getting an Bill",
            summary = "Get Bill",
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
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping(value = "/{bill_id}", produces = "application/json")
    public ResponseEntity<BillDto> getBill(@Validated @PathVariable String bill_id) {
        long id;
        try{
            id = Integer.parseInt(bill_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Bill", "bill_id", bill_id);
        }
        return ResponseEntity.ok().body(billService.getBillById(id));
    }
}
