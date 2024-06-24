package com.hms.controller;

import com.hms.Security.JwtAuthResource;
import com.hms.dto.CustomerDto;
import com.hms.dto.EmployeeDto;
import com.hms.dto.JWTAuthResponse;
import com.hms.dto.LoginDto;
import com.hms.Security.JwtTokenProvider;
import com.hms.model.Role;
import com.hms.model.User;
import com.hms.repository.RoleRepository;
import com.hms.repository.UserRepository;
import com.hms.service.CustomerService;
import com.hms.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;


    public AuthenticationController(AuthenticationManager authenticationManager, CustomerService customerService, EmployeeService employeeService, UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResource> authenticateUser(@RequestBody LoginDto loginDto) throws AccessDeniedException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);

        User temp=userRepository.findByEmail(loginDto.getEmail()).get();
        boolean flag =false;
        List<Role> rls=new ArrayList<>(temp.getRole());
        for (Role rl : rls) {
            if (rl.getName().compareTo("ROLE_EMPLOYEE") == 0) {
                flag = true;
                break;
            }
        }
        JwtAuthResource resource = new JwtAuthResource(new JWTAuthResponse(token));

        if (flag){
            Link userBillLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TaskController.class).registerTask (null))
                    .withRel("Create-Task")
                    .withType("POST");
            Link postLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BillController.class).registerBill(null))
                    .withRel("create-Bill")
                    .withType("POST");
            resource.add(userBillLink, postLink);
        }else{
            Link userBillLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BillController.class).getBill(String.valueOf(temp.getId()),null))
                    .withRel("Reservation-Bill")
                    .withType("GET");
            Link postLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).registerReservation(null))
                    .withRel("create-reservation")
                    .withType("POST");
            resource.add(userBillLink, postLink);
        }



        return ResponseEntity.ok(resource);
    }

    @Operation(
            description = "Endpoint for creating an Customer",
            summary = "Create Customer",
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
    @PostMapping("/signup/customer")
    public ResponseEntity<?> registerCustomer (@Validated @RequestBody CustomerDto signUpDto){

        return ResponseEntity.ok().body(customerService.postCustomer(signUpDto));
    }
    @Operation(
            description = "Endpoint for creating an Employee",
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
    @PostMapping("/signup/employee")
    public ResponseEntity<?> registerEmployee (@Validated @RequestBody EmployeeDto signUpDto){

        return ResponseEntity.ok().body(employeeService.postEmployee(signUpDto));
    }
}