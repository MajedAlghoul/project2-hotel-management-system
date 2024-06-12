package com.hms.service;

import com.hms.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto postEmployee(EmployeeDto employee);
    EmployeeDto[] getEmployees(Integer pageNumber, Integer pageSize);
    EmployeeDto replaceEmployee(EmployeeDto employee);
    EmployeeDto modifyEmployee(EmployeeDto employee);
    void deleteEmployee(Integer employee_id);
    EmployeeDto getEmployeeById(Integer employee_id);
}
