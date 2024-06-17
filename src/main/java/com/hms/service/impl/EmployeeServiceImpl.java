package com.hms.service.impl;

import com.hms.dto.EmployeeDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.model.Customer;
import com.hms.model.Employee;
import com.hms.model.Role;
import com.hms.repository.EmployeeRepository;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.RoleRepository;
import com.hms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
    }

    public void postEmployee(EmployeeDto employee) {
        if(employeeRepository.existsByEmployeeEmail(employee.getEmployeeEmail()))
            throw new DuplicateResourceException("Employee", "employee_id", employee.getEmployeeEmail());
        Employee newEmployee = mapToEntity(employee);
        Role role = roleRepository.findByName("ROLE_EMPLOYEE").get();
        newEmployee.setRole(Collections.singleton(role));
        employeeRepository.save(newEmployee);
        //return mapToDto(customerRepository.save(newCustomer));
    }

    @Override
    public EmployeeDto[] getEmployees(Integer pageNumber, Integer pageSize) {
        Object[] employeesObjects = employeeRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().toArray();
        EmployeeDto[] employeesDtos = new EmployeeDto[employeesObjects.length];
        for(int i = 0; i < employeesObjects.length; i++)
            if (employeesObjects[i] instanceof Employee)
                employeesDtos[i] = mapToDto((Employee) employeesObjects[i]);
        if(employeesDtos.length == 0)
            throw new ResourceNotFoundException("Employee", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        return employeesDtos;
    }

    @Override
    public EmployeeDto replaceEmployee(EmployeeDto employee) {
        if(!employeeRepository.existsByEmployeeEmail(employee.getEmployeeEmail()))
            throw new ResourceNotFoundException("Employee", "employee_id", String.valueOf(employee.getEmployeeEmail()));
        return mapToDto(employeeRepository.save(mapToEntity(employee)));
    }

    @Override
    public EmployeeDto modifyEmployee(EmployeeDto partialEmployee) {
        Employee originalEmployee = employeeRepository.findByEmployeeEmail(partialEmployee.getEmployeeEmail()).orElseThrow(() -> new ResourceNotFoundException("Employee", "employee_id", String.valueOf(partialEmployee.getEmployeeEmail())));
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
                .employeeEmail(employee.getEmployeeEmail())
                .name(employee.getName())
                .passwordHash(employee.getPasswordHash())
                .build();
    }

    private Employee mapToEntity(EmployeeDto employeeDto) {
        return Employee.builder()
                .employeeEmail(employeeDto.getEmployeeEmail())
                .name(employeeDto.getName())
                .passwordHash(employeeDto.getPasswordHash())
                .build();
    }
}
