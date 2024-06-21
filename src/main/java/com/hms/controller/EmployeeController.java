package com.hms.controller;

import com.hms.dto.EmployeeDto;
import com.hms.exception.IllegalInputException;
import com.hms.responsebody.StandardMessageBody;
import com.hms.service.EmployeeService;
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
 * Contains endpoints related to the Employee resource.
 */
@RequestMapping("/api/v1/employee")
@RestController
@Validated
@Tag(name = "Employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
            description = "Endpoint for creating a Employee",
            summary = "Create Employee",
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> registerEmployee (@Validated @RequestBody EmployeeDto employeeDto){

        return ResponseEntity.ok().body(employeeService.postEmployee(employeeDto));
    }

    @Operation(
            description = "Endpoint for fetching a list of Employees",
            summary = "Fetch Employees",
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<EmployeeDto>> getEmployees() {
        return ResponseEntity.ok().body(employeeService.getEmployees());
    }

    @Operation(
            description = "Endpoint for replacing an Employee",
            summary = "Replace Employee",
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(produces = "application/json")
    public ResponseEntity<EmployeeDto> putEmployee(@Validated @RequestBody EmployeeDto employee) {
        return ResponseEntity.ok().body(employeeService.replaceEmployee(employee));
    }

    @Operation(
            description = "Endpoint for partially updating an Employee",
            summary = "Partially update Employee",
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(produces = "application/json")
    public ResponseEntity<EmployeeDto> patchEmployee(@Validated @RequestBody EmployeeDto employee) {
        return ResponseEntity.ok().body(employeeService.modifyEmployee(employee));
    }

    @Operation(
            description = "Endpoint for deleting an Employee",
            summary = "Delete Employee",
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
    @DeleteMapping(value = "/{employee_id}", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> deleteEmployee(@Validated @PathVariable String employee_id) {
        long id;
        try{
            id = Integer.parseInt(employee_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Employee", "employee_id", employee_id);
        }
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().body(new StandardMessageBody("Deleted successfully.").getContent());
    }

    @Operation(
            description = "Endpoint for getting an Employee",
            summary = "Get Employee",
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
    @GetMapping(value = "/{employee_id}", produces = "application/json")
    public ResponseEntity<EmployeeDto> getEmployee(@Validated @PathVariable String employee_id) {
        long id;
        try{
            id = Integer.parseInt(employee_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Employee", "employee_id", employee_id);
        }
        return ResponseEntity.ok().body(employeeService.getEmployeeById(id));
    }
}
