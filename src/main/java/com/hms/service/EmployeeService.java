package com.hms.service;

import com.hms.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    EmployeeDto postEmployee(EmployeeDto employee);
    List<EmployeeDto> getEmployees();
    EmployeeDto replaceEmployee(EmployeeDto employee);
    EmployeeDto modifyEmployee(EmployeeDto employee);
    void deleteEmployee(Long employee_id);
    EmployeeDto getEmployeeById(Long employee_id);
}
