package com.hms.controller;

import com.hms.dto.EmployeeDto;
import com.hms.exception.IllegalInputException;
import com.hms.responsebody.StandardMessageBody;
import com.hms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Contains endpoints related to the Employee resource.
 */
@RequestMapping("/api/employee")
@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<EmployeeDto> postEmployee(@Validated @RequestBody EmployeeDto employee) {
        return ResponseEntity.ok().body(employeeService.postEmployee(employee));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<EmployeeDto[]> getEmployees(@Validated @RequestParam String pageNumber, @RequestParam String pageSize) {
        int pageNumberInt, pageSizeInt;
        try{
            pageNumberInt = Integer.parseInt(pageNumber);
            pageSizeInt = Integer.parseInt(pageSize);
            if(pageNumberInt < 0 || pageNumberInt > pageSizeInt || pageSizeInt < 1)
                throw new IllegalInputException("Employee", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Employee", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        }
        return ResponseEntity.ok().body(employeeService.getEmployees(pageNumberInt, pageSizeInt));
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<EmployeeDto> putEmployee(@Validated @RequestBody EmployeeDto employee) {
        return ResponseEntity.ok().body(employeeService.replaceEmployee(employee));
    }

    @PatchMapping(produces = "application/json")
    public ResponseEntity<EmployeeDto> patchEmployee(@Validated @RequestBody EmployeeDto employee) {
        return ResponseEntity.ok().body(employeeService.modifyEmployee(employee));
    }

    @DeleteMapping(value = "/{employee_id}", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> deleteEmployee(@Validated @PathVariable String employee_id) {
        //String id;
        //try{
        //    id = Integer.parseInt(employee_id);
        //} catch(NumberFormatException e){
        //    throw new IllegalInputException("Employee", "employee_id", employee_id);
        //}
        employeeService.deleteEmployee(employee_id);
        return ResponseEntity.ok().body(new StandardMessageBody("Deleted successfully.").getContent());
    }

    @GetMapping(value = "/{employee_id}", produces = "application/json")
    public ResponseEntity<EmployeeDto> getEmployee(@Validated @PathVariable String employee_id) {
        //int id;
        //try{
        //    id = Integer.parseInt(employee_id);
        //} catch(NumberFormatException e){
        //    throw new IllegalInputException("Employee", "employee_id", employee_id);
        //}
        return ResponseEntity.ok().body(employeeService.getEmployeeById(employee_id));
    }
}
