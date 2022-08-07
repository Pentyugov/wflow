package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Employee;
import com.pentyugov.wflow.core.dto.EmployeeDto;
import com.pentyugov.wflow.web.exception.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    String NAME = "wflow$EmployeeService";

    List<Employee> getAll();

    Employee getById(UUID id) throws EmployeeNotFoundException;

    Employee add(EmployeeDto employeeDto) throws PositionNotFoundException, UserNotFoundException,
            EmployeeExistException, ValidationException, DepartmentNotFoundException;

    Employee update(EmployeeDto employeeDto) throws PositionNotFoundException, UserNotFoundException,
            DepartmentNotFoundException, ValidationException, EmployeeExistException;

    void delete(UUID id);

    List<Employee> getByDepartment(UUID departmentId);

    void updateAll(List<EmployeeDto> employeeDtos) throws PositionNotFoundException, UserNotFoundException,
            DepartmentNotFoundException;

    Employee convert(EmployeeDto employeeDto) throws PositionNotFoundException, DepartmentNotFoundException,
            UserNotFoundException;

    EmployeeDto convert(Employee employee);



}
