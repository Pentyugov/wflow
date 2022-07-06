package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Employee;
import com.pentyugov.wflow.core.dto.EmployeeDto;
import com.pentyugov.wflow.web.exception.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    String NAME = "wflow$EmployeeService";

    List<Employee> getAllEmployees();

    Employee getEmployeeById(UUID id, Principal principal) throws EmployeeNotFoundException, UserNotFoundException;

    Employee addNewEmployee(EmployeeDto employeeDto, Principal principal) throws PositionNotFoundException, UserNotFoundException, EmployeeExistException, ValidationException, DepartmentNotFoundException;

    Employee updateEmployee(EmployeeDto employeeDto, Principal principal) throws PositionNotFoundException, UserNotFoundException, DepartmentNotFoundException, ValidationException, EmployeeExistException;

    void updateAll(List<EmployeeDto> employeeDtos) throws PositionNotFoundException, UserNotFoundException, DepartmentNotFoundException;

    Employee createEmployeeFromDto(EmployeeDto employeeDto) throws PositionNotFoundException, DepartmentNotFoundException, UserNotFoundException;

    EmployeeDto createEmployeeDtoFromEmployee(Employee employee);

    List<Employee> getEmployeesByDepartment(UUID departmentId);

    void deleteEmployee(UUID id);

}
