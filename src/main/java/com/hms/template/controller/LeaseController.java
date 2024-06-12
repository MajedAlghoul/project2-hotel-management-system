package com.hms.template.controller;

import com.inventorymanagementsystem.dto.LeaseDto;
import com.hms.exception.IllegalInputException;
import com.hms.responsebody.StandardMessageBody;
import com.inventorymanagementsystem.service.LeaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Contains endpoints related to the Lease resource.
 */
@RestController
public class LeaseController {
    private final LeaseService leaseService;

    @Autowired
    public LeaseController(LeaseService leaseService) {
        this.leaseService = leaseService;
    }

    @PostMapping(value = "/employee/{employee_id}/lease", produces = "application/json")
    public ResponseEntity<LeaseDto> postLease(@Validated @PathVariable String employee_id, @Validated @RequestBody LeaseDto lease) {
        int employee_id_int;
        try{
            employee_id_int = Integer.parseInt(employee_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Lease", "lease_id", employee_id);
        }
        lease.setEmployee_id(employee_id_int);
        return ResponseEntity.ok().body(leaseService.postLease(lease));
    }

    @GetMapping(value = "/employee/{employee_id}/lease", produces = "application/json")
    public ResponseEntity<LeaseDto[]> getInventories(@Validated @RequestParam String pageNumber, @RequestParam String pageSize) {
        int pageNumberInt, pageSizeInt;
        try{
            pageNumberInt = Integer.parseInt(pageNumber);
            pageSizeInt = Integer.parseInt(pageSize);
            if(pageNumberInt < 0 || pageNumberInt > pageSizeInt || pageSizeInt < 1)
                throw new IllegalInputException("Lease", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Lease", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        }
        return ResponseEntity.ok().body(leaseService.getLeases(pageNumberInt, pageSizeInt));
    }

    @PutMapping(value = "/employee/{employee_id}/lease", produces = "application/json")
    public ResponseEntity<LeaseDto> putLease(@Validated @RequestBody LeaseDto lease) {
        return ResponseEntity.ok().body(leaseService.replaceLease(lease));
    }

    @PatchMapping(value = "/employee/{employee_id}/lease", produces = "application/json")
    public ResponseEntity<LeaseDto> patchLease(@Validated @RequestBody LeaseDto lease) {
        return ResponseEntity.ok().body(leaseService.modifyLease(lease));
    }

    @DeleteMapping(value = "/employee/{employee_id}/lease/{lease_id}", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> deleteLease(@Validated @PathVariable String lease_id) {
        int lease_id_int;
        try{
            lease_id_int = Integer.parseInt(lease_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Lease", "lease_id", lease_id);
        }
        leaseService.deleteLease(lease_id_int);
        return ResponseEntity.ok().body(new StandardMessageBody("Deleted successfully.").getContent());
    }

    @GetMapping(value = "/employee/{employee_id}/lease/{lease_id}", produces = "application/json")
    public ResponseEntity<LeaseDto> getLease(@Validated @PathVariable String lease_id) {
        int lease_id_int;
        try{
            lease_id_int = Integer.parseInt(lease_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Lease", "lease_id", lease_id);
        }
        return ResponseEntity.ok().body(leaseService.getLeaseById(lease_id_int));
    }
}