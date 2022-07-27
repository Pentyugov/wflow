package com.pentyugov.wflow.web.rest;


import com.pentyugov.wflow.core.domain.entity.Employee;
import com.pentyugov.wflow.core.dto.EmployeeDto;
import com.pentyugov.wflow.core.service.EmployeeService;
import com.pentyugov.wflow.web.exception.*;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController extends AbstractController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllEmployees() {
        List<EmployeeDto> employees = new ArrayList<>();
        employeeService.getAllEmployees().forEach(employee ->
                employees.add(employeeService.createEmployeeDtoFromEmployee(employee)));
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable String id, Principal principal)
            throws EmployeeNotFoundException, UserNotFoundException {
        Employee employee = employeeService.getEmployeeById(UUID.fromString(id), principal);
        return new ResponseEntity<>(employeeService.createEmployeeDtoFromEmployee(employee), HttpStatus.OK);
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<Object> getEmployeesByDepartments(@PathVariable String id) {
        List<EmployeeDto> employees = new ArrayList<>();
        employeeService.getEmployeesByDepartment(UUID.fromString(id)).forEach(employee ->
                employees.add(employeeService.createEmployeeDtoFromEmployee(employee)));
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addNewEmployee(@RequestBody EmployeeDto employeeDto, Principal principal)
            throws UserNotFoundException, EmployeeExistException, PositionNotFoundException, ValidationException,
            DepartmentNotFoundException {

        Employee employee = employeeService.addNewEmployee(employeeDto, principal);
        return new ResponseEntity<>(employeeService.createEmployeeDtoFromEmployee(employee), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> updateEmployee(@RequestBody EmployeeDto employeeDto, Principal principal)
            throws UserNotFoundException, EmployeeExistException, DepartmentNotFoundException,
            PositionNotFoundException, ValidationException {

        Employee employee = employeeService.updateEmployee(employeeDto, principal);
        return new ResponseEntity<>(employeeService.createEmployeeDtoFromEmployee(employee), HttpStatus.OK);
    }

    @PutMapping("/all")
    public ResponseEntity<Object> updateAllEmployees(@RequestBody List<EmployeeDto> employees)
            throws PositionNotFoundException,
            UserNotFoundException, DepartmentNotFoundException {

        this.employeeService.updateAll(employees);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(UUID.fromString(id));
        String message = String.format("Employee with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

}
