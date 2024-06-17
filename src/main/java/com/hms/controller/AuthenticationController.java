package com.hms.controller;

import com.hms.dto.CustomerDto;
import com.hms.dto.EmployeeDto;
import com.hms.dto.JWTAuthResponse;
import com.hms.dto.LoginDto;
import com.hms.Security.JwtTokenProvider;
import com.hms.service.CustomerService;
import com.hms.service.EmployeeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final JwtTokenProvider tokenProvider;


    public AuthenticationController(AuthenticationManager authenticationManager, CustomerService customerService, EmployeeService employeeService, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @PostMapping("/signup/customer")
    public ResponseEntity<?> registerCustomer (@Validated @RequestBody CustomerDto signUpDto){

        customerService.postCustomer(signUpDto);
        //return ResponseEntity.ok().body(customerService.postCustomer(customer));

        return new ResponseEntity<>("Customer registered successfully",
                HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/signup/employee")
    public ResponseEntity<?> registerEmployee (@Validated @RequestBody EmployeeDto signUpDto){

        employeeService.postEmployee(signUpDto);
        //return ResponseEntity.ok().body(customerService.postCustomer(customer));

        return new ResponseEntity<>("Employee registered successfully",
                HttpStatus.OK);
    }
}