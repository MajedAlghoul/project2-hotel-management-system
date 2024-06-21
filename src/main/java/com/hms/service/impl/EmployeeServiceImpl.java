package com.hms.service.impl;

import com.hms.dto.EmployeeDto;
import com.hms.dto.EmployeeDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.NoContentException;
import com.hms.model.Employee;
import com.hms.model.Role;
import com.hms.model.Employee;
import com.hms.repository.EmployeeRepository;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.RoleRepository;
import com.hms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public EmployeeDto postEmployee(EmployeeDto employee) {
        if(employeeRepository.existsByEmail(employee.getEmail()))
            throw new DuplicateResourceException("Employee", "employee_id", employee.getEmail());
        Employee newEmployee = mapToEntity(employee);
        Role role1 = roleRepository.findByName("ROLE_EMPLOYEE").get();
        Role role2 = roleRepository.findByName("ROLE_CUSTOMER").get();
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);
        newEmployee.setRole(roles);
        return mapToDto(employeeRepository.save(newEmployee));
    }

    @Override
    public List<EmployeeDto> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            throw new NoContentException("No employees registered yet");
        }
        return employees.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public EmployeeDto replaceEmployee(EmployeeDto employee) {
        if(!employeeRepository.existsById(employee.getId()))
            throw new ResourceNotFoundException("Employee", "employee_id", String.valueOf(employee.getEmail()));
        return mapToDto(employeeRepository.save(mapToEntity(employee)));
    }

    @Override
    public EmployeeDto modifyEmployee(EmployeeDto partialEmployee) {
        Employee originalEmployee = employeeRepository.findByEmail(partialEmployee.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Employee", "email", String.valueOf(partialEmployee.getEmail())));
        if(!partialEmployee.getEmail().isEmpty())
            originalEmployee.setEmail(partialEmployee.getEmail());
        if(!partialEmployee.getName().isEmpty())
            originalEmployee.setName(partialEmployee.getName());
        employeeRepository.save(originalEmployee);
        return mapToDto(originalEmployee);
    }


    @Override
    public void deleteEmployee(Long employee_id) {
        if(!employeeRepository.existsById(employee_id))
            throw new ResourceNotFoundException("Employee", "employee_id", String.valueOf(employee_id));
        employeeRepository.deleteById(employee_id);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employee_id) {
        return mapToDto(employeeRepository.findById(employee_id).orElseThrow(() -> new ResourceNotFoundException("Employee", "employee_id", String.valueOf(employee_id))));
    }

    private EmployeeDto mapToDto(Employee employee) {
        return EmployeeDto.builder()
                .email(employee.getEmail())
                .name(employee.getName())
                .passwordHash(employee.getPasswordHash())
                .id(employee.getId())
                .build();
    }

    private Employee mapToEntity(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setEmail(employeeDto.getEmail());
        employee.setName(employeeDto.getName());
        employee.setPasswordHash(passwordEncoder.encode(employeeDto.getPasswordHash()));
        return employee;
    }
}
