package com.hms.service.impl;

import com.hms.dto.EmployeeDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.model.Employee;
import com.hms.repository.EmployeeRepository;
import com.hms.exception.ResourceNotFoundException;
import com.hms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeDto postEmployee(EmployeeDto employee) {
        if(employeeRepository.existsById(employee.getEmployee_email()))
            throw new DuplicateResourceException("Employee", "employee_id", employee.getEmployee_email());
        return mapToDto(employeeRepository.save(mapToEntity(employee)));
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
        if(!employeeRepository.existsById(employee.getEmployee_email()))
            throw new ResourceNotFoundException("Employee", "employee_id", String.valueOf(employee.getEmployee_email()));
        return mapToDto(employeeRepository.save(mapToEntity(employee)));
    }

    @Override
    public EmployeeDto modifyEmployee(EmployeeDto partialEmployee) {
        Employee originalEmployee = employeeRepository.findById(partialEmployee.getEmployee_email()).orElseThrow(() -> new ResourceNotFoundException("Employee", "employee_id", String.valueOf(partialEmployee.getEmployee_email())));
        employeeRepository.save(originalEmployee);
        return mapToDto(originalEmployee);
    }

    @Override
    public void deleteEmployee(String employee_id) {
        if(!employeeRepository.existsById(employee_id))
            throw new ResourceNotFoundException("Employee", "employee_id", String.valueOf(employee_id));
        employeeRepository.deleteById(employee_id);
    }

    @Override
    public EmployeeDto getEmployeeById(String employee_id) {
        return mapToDto(employeeRepository.findById(employee_id).orElseThrow(() -> new ResourceNotFoundException("Employee", "employee_id", String.valueOf(employee_id))));
    }

    private EmployeeDto mapToDto(Employee employee) {
        return EmployeeDto.builder()
                .employee_email(employee.getEmployee_email())
                .name(employee.getName())
                .password_hash(employee.getPassword_hash())
                .build();
    }

    private Employee mapToEntity(EmployeeDto employeeDto) {
        return Employee.builder()
                .employee_email(employeeDto.getEmployee_email())
                .name(employeeDto.getName())
                .password_hash(employeeDto.getPassword_hash())
                .build();
    }
}
