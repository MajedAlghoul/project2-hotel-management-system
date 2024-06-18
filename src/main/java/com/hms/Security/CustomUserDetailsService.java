package com.hms.Security;

import com.hms.model.Customer;
import com.hms.model.Role;
import com.hms.model.Employee;
import com.hms.repository.CustomerRepository;
import com.hms.repository.EmployeeRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository,EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Attempt to find the user as a Customer
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerEmail(email);

        if (optionalCustomer.isPresent()) {
            // If found as a Customer, return the UserDetails for the Customer
            Customer customer = optionalCustomer.get();
            return new org.springframework.security.core.userdetails.User(
                    customer.getCustomerEmail(),
                    customer.getPasswordHash(),
                    mapRolesToAuthorities(customer.getRole())
            );
        } else {
            // If not found as a Customer, attempt to find the user as an Employee
            Employee employee = employeeRepository.findByEmployeeEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            // Return the UserDetails for the Employee
            return new org.springframework.security.core.userdetails.User(
                    employee.getEmployeeEmail(),
                    employee.getPasswordHash(),
                    mapRolesToAuthorities(employee.getRole())
            );
        }
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
